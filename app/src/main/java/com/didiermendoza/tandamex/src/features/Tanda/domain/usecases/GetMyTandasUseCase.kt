package com.didiermendoza.tandamex.src.features.Home.domain.usecases

import com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda
import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.TandaRepository
import javax.inject.Inject

class GetMyTandasUseCase @Inject constructor(
    private val repository: TandaRepository
) {
    suspend operator fun invoke(): Result<List<Tanda>> {
        return repository.syncMyTandas()
    }
}