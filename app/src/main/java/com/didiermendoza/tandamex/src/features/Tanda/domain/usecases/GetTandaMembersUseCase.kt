package com.didiermendoza.tandamex.src.features.Tanda.domain.usecases

import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaMember
import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.TandaRepository

class GetTandaMembersUseCase(private val repository: TandaRepository) {
    suspend operator fun invoke(tandaId: Int): Result<List<TandaMember>> {
        return repository.getTandaMembers(tandaId)
    }
}