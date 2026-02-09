package com.didiermendoza.tandamex.src.features.Home.domain.usecases

import com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda
import com.didiermendoza.tandamex.src.features.Home.domain.repositories.HomeRepository

class GetAvailableTandasUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<Tanda>> {
        return repository.getTandas()
    }
}