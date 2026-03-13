package com.didiermendoza.tandamex.src.features.Home.domain.usecases

import com.didiermendoza.tandamex.src.features.Home.domain.repositories.HomeRepository
import javax.inject.Inject

class SyncTandasUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke() {
        repository.syncTandas()
    }
}