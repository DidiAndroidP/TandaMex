package com.didiermendoza.tandamex.src.features.Tanda.domain.repositories

import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.ScheduleData
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaDetail
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaMember
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaSummary
import kotlinx.coroutines.flow.Flow

interface TandaRepository {
    suspend fun createTanda(name: String, amount: Double, frequency: String, members: Int, tolerance: Int, penalty: Double): Result<Boolean>
    fun getTandaDetail(tandaId: Int): Flow<TandaDetail?>
    fun getTandaMembers(tandaId: Int): Flow<List<TandaMember>>
    fun getSchedule(tandaId: Int): Flow<ScheduleData?>
    suspend fun syncTandaDetailAndMembers(tandaId: Int)
    suspend fun generateSchedule(tandaId: Int): Result<ScheduleData>
    suspend fun joinTanda(tandaId: Int): Result<String>
    suspend fun leaveTanda(tandaId: Int, userId: Int, amountToRefund: Double): Result<String>
    suspend fun startTanda(tandaId: Int): Result<String>
    suspend fun finishTanda(tandaId: Int): Result<String>
    suspend fun deleteTanda(tandaId: Int): Result<String>
    suspend fun getTandaSummary(tandaId: Int): Result<TandaSummary>

    suspend fun createPaymentSession(tandaId: Int, period: Int, amount: Double): Result<String>
}