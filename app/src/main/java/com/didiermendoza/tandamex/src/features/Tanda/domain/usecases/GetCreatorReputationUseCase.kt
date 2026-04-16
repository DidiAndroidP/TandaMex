package com.didiermendoza.tandamex.src.features.Tanda.domain.usecases

import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.ReviewRepository
import javax.inject.Inject

class GetCreatorReputationUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(creatorId: Int): Result<Int> {
        return reviewRepository.getCreatorReputation(creatorId)
    }
}