package com.didiermendoza.tandamex.src.features.Tanda.domain.usecases

import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.TandaRepository
import javax.inject.Inject

class CreatePaymentSessionUseCase @Inject constructor(
    private val repository: TandaRepository
) {
    suspend operator fun invoke(tandaId: Int, period: Int, amount: Double): Result<String> {
        return repository.createPaymentSession(tandaId, period, amount)
    }
}