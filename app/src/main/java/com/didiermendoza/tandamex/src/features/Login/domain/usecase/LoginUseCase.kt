package com.didiermendoza.tandamex.src.features.Login.domain.usecases

import com.didiermendoza.tandamex.src.features.Login.domain.entities.LoginInput
import com.didiermendoza.tandamex.src.features.Login.domain.entities.LoginSuccess
import com.didiermendoza.tandamex.src.features.Login.domain.repositories.LoginRepository

class LoginUseCase(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(input: LoginInput): Result<LoginSuccess> {
        if (input.email.isBlank() || input.password.isBlank()) {
            return Result.failure(Exception("Por favor llena todos los campos"))
        }
        return repository.login(input)
    }
}