package com.didiermendoza.tandamex.src.features.Home.domain.repositories

import com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getAvailableTandas(): Flow<List<Tanda>>
    suspend fun syncTandas()
}