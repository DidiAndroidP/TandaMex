package com.didiermendoza.tandamex.src.features.Tanda.data.repositories

import com.didiermendoza.tandamex.src.core.database.dao.ScheduleDao
import com.didiermendoza.tandamex.src.core.database.dao.TandaDao
import com.didiermendoza.tandamex.src.core.database.dao.TandaDetailDao
import com.didiermendoza.tandamex.src.core.database.dao.TandaMemberDao
import com.didiermendoza.tandamex.src.core.database.dao.TandaPaymentDao
import com.didiermendoza.tandamex.src.core.database.dao.WalletDao
import com.didiermendoza.tandamex.src.features.Tanda.data.datasource.remote.api.TandaApiService
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model.CreateTandaRequestDto
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaDetail
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaMember
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaSummary
import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.TandaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.local.mapper.toDomain
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.local.mapper.toEntity
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.local.mapper.toSummaryEntity
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.mapper.toEntity
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.mapper.toDomain
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model.PaymentSessionRequestDto
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model.ScheduleDataDto
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.ScheduleData

class TandaRepositoryImpl @Inject constructor(
    private val api: TandaApiService,
    private val detailDao: TandaDetailDao,
    private val memberDao: TandaMemberDao,
    private val tandaDao: TandaDao,
    private val scheduleDao: ScheduleDao,
    private val walletDao: WalletDao,
    private val tandaPaymentDao: TandaPaymentDao
) : TandaRepository {

    override suspend fun createTanda(
        name: String, amount: Double, frequency: String, members: Int, tolerance: Int, penalty: Double
    ): Result<Boolean> {
        return try {
            val request = CreateTandaRequestDto(name, amount, frequency, members, tolerance, penalty)
            val response = api.createTanda(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error al crear tanda: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getTandaDetail(tandaId: Int): Flow<TandaDetail?> {
        return detailDao.getTandaDetail(tandaId).map { it?.toDomain() }
    }

    override fun getTandaMembers(tandaId: Int): Flow<List<TandaMember>> {
        return combine(
            memberDao.getTandaMembers(tandaId),
            tandaPaymentDao.getPaymentsByTanda(tandaId)
        ) { apiMembers, localPayments ->
            apiMembers.map { entity ->
                val domain = entity.toDomain()
                val hasPaidLocally = localPayments.any { it.userId == entity.id }
                domain.copy(hasPaid = domain.hasPaid || hasPaidLocally)
            }
        }
    }

    override suspend fun createPaymentSession(tandaId: Int, period: Int, amount: Double): Result<String> {
        return try {
            val request = PaymentSessionRequestDto(tandaId, period, amount)
            val response = api.createPaymentSession(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.url)
            } else {
                Result.failure(Exception("Error al generar pago: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("No se pudo conectar con el servidor de pagos"))
        }
    }

    override fun getSchedule(tandaId: Int): Flow<ScheduleData?> {
        return combine(
            scheduleDao.getScheduleSummary(tandaId),
            scheduleDao.getTurnos(tandaId)
        ) { summary, turnos ->
            if (summary == null) {
                null
            } else {
                ScheduleData(
                    tandaId = summary.tandaId,
                    turnos = turnos.map { it.toDomain() },
                    fechaFin = summary.fechaFin,
                    montoTotal = summary.montoTotal
                )
            }
        }
    }

    override suspend fun syncTandaDetailAndMembers(tandaId: Int) {
        try {
            val detailResponse = api.getTandaDetail(tandaId)
            if (detailResponse.isSuccessful && detailResponse.body() != null) {
                detailDao.insertTandaDetail(detailResponse.body()!!.toEntity())
            }

            val membersResponse = api.getTandaMembers(tandaId)
            if (membersResponse.isSuccessful && membersResponse.body() != null) {
                memberDao.replaceTandaMembers(tandaId, membersResponse.body()!!.map { it.toEntity(tandaId) })
            }

            val scheduleResponse = api.getSchedule(tandaId)
            if (scheduleResponse.isSuccessful && scheduleResponse.body() != null) {
                val scheduleDomain = scheduleResponse.body()!!.data.toDomain()
                scheduleDao.clearAndInsertSchedule(
                    tandaId = tandaId,
                    summary = scheduleDomain.toSummaryEntity(),
                    turnos = scheduleDomain.turnos.map { it.toEntity(tandaId) }
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun joinTanda(tandaId: Int): Result<String> {
        return try {
            val response = api.joinTanda(tandaId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.message)
            } else {
                Result.failure(Exception("Error al unirse: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun leaveTanda(tandaId: Int, userId: Int, amountToRefund: Double): Result<String> {
        return try {
            val response = api.leaveTanda(tandaId)
            if (response.isSuccessful && response.body() != null) {
                if (tandaPaymentDao.hasUserPaid(tandaId, userId)) {
                    walletDao.addBalance(userId, amountToRefund)
                    tandaPaymentDao.deleteUserPayment(tandaId, userId)
                }
                Result.success("Saliste de la tanda. Tu dinero fue reembolsado.")
            } else {
                Result.failure(Exception("Error al salir: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun startTanda(tandaId: Int): Result<String> {
        return try {
            val response = api.startTanda(tandaId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.message)
            } else {
                Result.failure(Exception("Error al iniciar tanda: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun finishTanda(tandaId: Int): Result<String> {
        return try {
            val response = api.finishTanda(tandaId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.message)
            } else {
                Result.failure(Exception("Error al finalizar tanda: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTanda(tandaId: Int): Result<String> {
        return try {
            val response = api.deleteTanda(tandaId)
            if (response.isSuccessful && response.body() != null) {
                detailDao.deleteTandaDetail(tandaId)
                memberDao.deleteMembersByTanda(tandaId)
                scheduleDao.deleteScheduleSummary(tandaId)
                scheduleDao.deleteTurnos(tandaId)
                tandaDao.deleteTanda(tandaId)
                Result.success(response.body()!!.message)
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "No se puede eliminar una tanda iniciada"
                    403 -> "Solo el creador puede eliminar la tanda"
                    404 -> "La tanda no existe"
                    else -> "Error al eliminar: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateSchedule(tandaId: Int): Result<ScheduleData> {
        return try {
            val response = api.generateSchedule(tandaId)
            if (response.isSuccessful && response.body() != null) {
                val scheduleDomain = response.body()!!.data.toDomain()
                scheduleDao.clearAndInsertSchedule(
                    tandaId = tandaId,
                    summary = scheduleDomain.toSummaryEntity(),
                    turnos = scheduleDomain.turnos.map { it.toEntity(tandaId) }
                )
                Result.success(scheduleDomain)
            } else {
                Result.failure(Exception("Error al generar horario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTandaSummary(tandaId: Int): Result<TandaSummary> {
        return try {
            val response = api.getTandaSummary(tandaId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Error al obtener resumen: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncMyTandas(): Result<List<com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda>> {
        return try {
            val response = api.getMyTandas()
            if (response.isSuccessful && response.body() != null) {
                val domainList = response.body()!!.map { dto ->
                    com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda(
                        id = dto.id,
                        name = dto.name,
                        amount = dto.contributionAmount,
                        frequency = dto.paymentFrequency,
                        totalMembers = dto.totalMembers,
                        progress = 0f,
                        status = dto.status
                    )
                }
                Result.success(domainList)
            } else {
                Result.failure(Exception("Error al obtener mis tandas: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun triggerLiveSchedule(tandaId: Int): Result<Unit> {
        return try {
            val response = api.triggerLiveSchedule(tandaId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al iniciar el sorteo: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveScheduleLocal(scheduleDataDto: ScheduleDataDto) {
        val domain = scheduleDataDto.toDomain()
        scheduleDao.clearAndInsertSchedule(
            tandaId = domain.tandaId,
            summary = domain.toSummaryEntity(),
            turnos = domain.turnos.map { it.toEntity(domain.tandaId) }
        )
    }
}