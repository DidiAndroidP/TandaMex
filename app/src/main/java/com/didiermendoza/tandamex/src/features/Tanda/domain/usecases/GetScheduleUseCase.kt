package com.didiermendoza.tandamex.src.features.Tanda.domain.usecases

import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.ScheduleData
import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.TandaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetScheduleUseCase @Inject constructor(
    private val repository: TandaRepository
) {
    operator fun invoke(tandaId: Int): Flow<ScheduleData?> {
        return repository.getSchedule(tandaId)
    }
}