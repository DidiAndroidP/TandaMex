package com.didiermendoza.tandamex.src.features.Tanda.domain.usecases

import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.TandaRepository

class GenerateScheduleUseCase(private val repository: TandaRepository) {
    suspend operator fun invoke(tandaId: Int): Result<String> {
        return repository.generateSchedule(tandaId)
    }
}