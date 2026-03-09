package com.didiermendoza.tandamex.src.features.Tanda.domain.usecases

import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.TandaRepository
import javax.inject.Inject

class CreateTandaUseCase @Inject constructor(
    private val repository: TandaRepository
) {
    suspend operator fun invoke(name: String, amount: Double, frequency: String, members: Int, tolerance: Int, penalty: Double): Result<Boolean> {
        return repository.createTanda(name, amount, frequency, members, tolerance, penalty)
    }
}