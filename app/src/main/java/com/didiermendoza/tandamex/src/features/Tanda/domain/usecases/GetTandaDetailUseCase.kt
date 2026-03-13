package com.didiermendoza.tandamex.src.features.Tanda.domain.usecases

import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaDetail
import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.TandaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTandaDetailUseCase @Inject constructor(
    private val repository: TandaRepository
) {
    operator fun invoke(tandaId: Int): Flow<TandaDetail?> {
        return repository.getTandaDetail(tandaId)
    }
}