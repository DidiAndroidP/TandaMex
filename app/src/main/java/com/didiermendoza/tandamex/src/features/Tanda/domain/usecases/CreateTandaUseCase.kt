package com.didiermendoza.tandamex.src.features.Tanda.domain.usecases

import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.TandaRepository

class CreateTandaUseCase(private val repository: TandaRepository) {
    suspend operator fun invoke(name: String, amount: Double, frequency: String, members: Int, tolerance: Int, penalty: Double): Result<Boolean> {
        return repository.createTanda(name, amount, frequency, members, tolerance, penalty)
    }
}