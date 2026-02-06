package com.didiermendoza.tandamex.src.features.Register.domain.repositories
import com.didiermendoza.tandamex.src.features.Register.domain.entities.RegisterInput
import com.didiermendoza.tandamex.src.features.Register.domain.entities.User

interface RegisterRepository {
    suspend fun registerUser(input: RegisterInput): Result<User>
}