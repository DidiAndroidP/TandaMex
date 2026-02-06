package com.didiermendoza.tandamex.src.features.Register.data.repositories

import com.didiermendoza.tandamex.src.core.http.TandaMexApi
import com.didiermendoza.tandamex.src.features.Register.data.datasources.remote.mapper.toDomain
import com.didiermendoza.tandamex.src.features.Register.data.datasources.remote.model.RegisterRequestDto
import com.didiermendoza.tandamex.src.features.Register.domain.entities.User
import com.didiermendoza.tandamex.src.features.Register.domain.repositories.RegisterRepository
import com.didiermendoza.tandamex.src.features.Register.domain.entities.RegisterInput

class RegisterRepositoryImpl(
    private val api: TandaMexApi
) : RegisterRepository {

    override suspend fun registerUser(input: RegisterInput): Result<User> {
        return try {
            val requestDto = RegisterRequestDto(
                name = input.name,
                email = input.email,
                password = input.password,
                phone = input.phone
            )

            val response = api.registerUser(requestDto)

            if (response.isSuccessful && response.body() != null) {
                val userDomain = response.body()!!.toDomain()
                Result.success(userDomain)
            } else {
                Result.failure(Exception("Error al registrar: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}