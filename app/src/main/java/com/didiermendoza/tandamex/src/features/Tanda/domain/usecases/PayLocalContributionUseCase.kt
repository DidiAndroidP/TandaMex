package com.didiermendoza.tandamex.src.features.Tanda.domain.usecases

import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.TandaRepository
import javax.inject.Inject

class PayLocalContributionUseCase @Inject constructor(
    private val repository: TandaRepository
) {
    suspend operator fun invoke(tandaId: Int, userId: Int, amount: Double): Result<String> {
        return repository.payLocalContribution(tandaId, userId, amount)
    }
}