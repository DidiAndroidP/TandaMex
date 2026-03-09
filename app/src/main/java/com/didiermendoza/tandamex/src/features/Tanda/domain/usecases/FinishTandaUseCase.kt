package com.didiermendoza.tandamex.src.features.Tanda.domain.usecases

import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.TandaRepository
import javax.inject.Inject

class FinishTandaUseCase @Inject constructor(
    private val repository: TandaRepository
) {
    suspend operator fun invoke(tandaId: Int): Result<String> {
        return repository.finishTanda(tandaId)
    }
}