package com.didiermendoza.tandamex.src.features.Tanda.domain.usecases

import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaDetail
import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.TandaRepository

class GetTandaDetailUseCase(private val repository: TandaRepository) {
    suspend operator fun invoke(tandaId: Int): Result<TandaDetail> {
        return repository.getTandaDetail(tandaId)
    }
}