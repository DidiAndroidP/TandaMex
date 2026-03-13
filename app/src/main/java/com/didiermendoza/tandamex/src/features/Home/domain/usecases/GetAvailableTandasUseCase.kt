package com.didiermendoza.tandamex.src.features.Home.domain.usecases

import com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda
import com.didiermendoza.tandamex.src.features.Home.domain.repositories.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAvailableTandasUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<List<Tanda>> {
        return repository.getAvailableTandas()
    }
}