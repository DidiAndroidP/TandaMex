package com.didiermendoza.tandamex.src.features.Tanda.domain.usecases

import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.ReviewRepository
import javax.inject.Inject

class CreateReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(tandaId: Int, rating: Int, comment: String): Result<Boolean> {
        return reviewRepository.createReview(tandaId, rating, comment)
    }
}