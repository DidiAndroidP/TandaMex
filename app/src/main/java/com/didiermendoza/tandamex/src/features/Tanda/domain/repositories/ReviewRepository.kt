package com.didiermendoza.tandamex.src.features.Tanda.domain.repositories

interface ReviewRepository {
    suspend fun createReview(tandaId: Int, rating: Int, comment: String): Result<Boolean>
    suspend fun getCreatorReputation(creatorId: Int): Result<Int>
}