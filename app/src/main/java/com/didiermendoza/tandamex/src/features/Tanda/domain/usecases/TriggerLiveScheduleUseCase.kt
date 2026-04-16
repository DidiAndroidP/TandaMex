package com.didiermendoza.tandamex.domain.usecase.tanda

import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.TandaRepository
import javax.inject.Inject

class TriggerLiveScheduleUseCase @Inject constructor(
    private val repository: TandaRepository
) {
    suspend operator fun invoke(tandaId: Int): Result<Unit> {
        return repository.triggerLiveSchedule(tandaId)
    }
}