package com.didiermendoza.tandamex.src.features.Tanda.domain.usecases

import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaMember
import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.TandaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTandaMembersUseCase @Inject constructor(
    private val repository: TandaRepository
) {
    operator fun invoke(tandaId: Int): Flow<List<TandaMember>> {
        return repository.getTandaMembers(tandaId)
    }
}