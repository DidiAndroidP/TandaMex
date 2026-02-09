package com.didiermendoza.tandamex.src.features.Profile.data.repositories

import com.didiermendoza.tandamex.src.core.http.TandaMexApi
import com.didiermendoza.tandamex.src.features.Profile.data.datasource.remote.mapper.toDomain
import com.didiermendoza.tandamex.src.features.Profile.data.datasource.remote.model.UpdateProfileRequestDto
import com.didiermendoza.tandamex.src.features.Profile.domain.entities.User
import com.didiermendoza.tandamex.src.features.Profile.domain.repositories.ProfileRepository

class ProfileRepositoryImpl(
    private val api: TandaMexApi
) : ProfileRepository {

    override suspend fun getMyProfile(): Result<User> {
        return try {
            val response = api.getMyProfile()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Error obteniendo perfil: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(name: String, phone: String): Result<String> {
        return try {
            val request = UpdateProfileRequestDto(name = name, phone = phone)
            val response = api.updateMyProfile(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.message)
            } else {
                Result.failure(Exception("Error actualizando perfil: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}