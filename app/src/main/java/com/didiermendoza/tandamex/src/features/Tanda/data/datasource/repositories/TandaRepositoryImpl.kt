package com.didiermendoza.tandamex.src.features.Tanda.data.repositories

import com.didiermendoza.tandamex.src.core.http.TandaMexApi
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.mapper.toDomain
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model.CreateTandaRequestDto
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaDetail
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaMember
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaSummary
import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.TandaRepository

class TandaRepositoryImpl(
    private val api: TandaMexApi
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

    override suspend fun getTandaDetail(tandaId: Int): Result<TandaDetail> {
        return try {
            val response = api.getTandaDetail(tandaId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Error al obtener detalle: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTandaMembers(tandaId: Int): Result<List<TandaMember>> {
        return try {
            val response = api.getTandaMembers(tandaId)
            if (response.isSuccessful && response.body() != null) {
                val members = response.body()!!.map { it.toDomain() }
                Result.success(members)
            } else {
                Result.failure(Exception("Error al obtener miembros: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
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

    override suspend fun leaveTanda(tandaId: Int): Result<String> {
        return try {
            val response = api.leaveTanda(tandaId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.message)
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

    override suspend fun generateSchedule(tandaId: Int): Result<String> {
        return try {
            val response = api.generateSchedule(tandaId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.message)
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
}