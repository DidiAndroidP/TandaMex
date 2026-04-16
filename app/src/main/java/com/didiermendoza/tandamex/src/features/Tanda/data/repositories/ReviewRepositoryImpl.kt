package com.didiermendoza.tandamex.src.features.Tanda.data.repositories

import com.didiermendoza.tandamex.src.features.Tanda.data.datasource.remote.api.TandaApiService
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model.ReviewRequestDto
import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val api: TandaApiService
) : ReviewRepository {

    override suspend fun createReview(tandaId: Int, rating: Int, comment: String): Result<Boolean> {
        return try {
            val request = ReviewRequestDto(tandaId, rating, comment)
            val response = api.createReview(request)

            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCreatorReputation(creatorId: Int): Result<Int> {
        return try {
            val response = api.getCreatorReputation(creatorId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.reputation)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}