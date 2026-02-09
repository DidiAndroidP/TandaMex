package com.didiermendoza.tandamex.src.features.Login.data.repositories

import com.didiermendoza.tandamex.src.core.http.TandaMexApi
import com.didiermendoza.tandamex.src.core.storage.TokenManager
import com.didiermendoza.tandamex.src.features.Login.data.datasources.remote.mapper.toDomain
import com.didiermendoza.tandamex.src.features.Login.data.datasources.remote.model.LoginRequestDto
import com.didiermendoza.tandamex.src.features.Login.domain.entities.LoginInput
import com.didiermendoza.tandamex.src.features.Login.domain.entities.LoginSuccess
import com.didiermendoza.tandamex.src.features.Login.domain.repositories.LoginRepository

class LoginRepositoryImpl(
    private val api: TandaMexApi,
    private val tokenManager: TokenManager
) : LoginRepository {

    override suspend fun login(input: LoginInput): Result<LoginSuccess> {
        return try {
            val request = LoginRequestDto(input.email, input.password)
            val response = api.loginUser(request)

            if (response.isSuccessful && response.body() != null) {
                val domainModel = response.body()!!.toDomain()

                tokenManager.saveToken(domainModel.token)

                Result.success(domainModel)
            } else {
                Result.failure(Exception("Error de Login: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}