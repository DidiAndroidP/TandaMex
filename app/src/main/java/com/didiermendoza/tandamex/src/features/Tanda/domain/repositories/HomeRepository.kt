package com.didiermendoza.tandamex.src.features.Home.domain.repositories

import com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda

interface HomeRepository {
    suspend fun getTandas(): Result<List<Tanda>>
}