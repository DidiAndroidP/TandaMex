package com.didiermendoza.tandamex.src.features.Register.domain.usecases

import com.didiermendoza.tandamex.src.features.Register.domain.entities.RegisterInput
import com.didiermendoza.tandamex.src.features.Register.domain.entities.User
import com.didiermendoza.tandamex.src.features.Register.domain.repositories.RegisterRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: RegisterRepository
) {
    suspend operator fun invoke(input: RegisterInput): Result<User> {
        return try {
            if (input.email.isBlank() || input.password.isBlank()) {
                return Result.failure(Exception("Campos obligatorios vacíos"))
            }

            val result = repository.registerUser(input)
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}