package com.didiermendoza.tandamex.src.features.Tanda.domain.repositories

import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaDetail
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaMember
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaSummary

interface TandaRepository {
    suspend fun createTanda(name: String, amount: Double, frequency: String, members: Int, tolerance: Int, penalty: Double): Result<Boolean>
    suspend fun getTandaDetail(tandaId: Int): Result<TandaDetail>
    suspend fun getTandaMembers(tandaId: Int): Result<List<TandaMember>>
    suspend fun joinTanda(tandaId: Int): Result<String>
    suspend fun leaveTanda(tandaId: Int): Result<String>
    suspend fun startTanda(tandaId: Int): Result<String>
    suspend fun finishTanda(tandaId: Int): Result<String>
    suspend fun deleteTanda(tandaId: Int): Result<String>
    suspend fun generateSchedule(tandaId: Int): Result<String>
    suspend fun getTandaSummary(tandaId: Int): Result<TandaSummary>
}