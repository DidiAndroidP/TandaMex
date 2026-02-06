package com.didiermendoza.tandamex.src.features.Login.domain.repositories

import com.didiermendoza.tandamex.src.features.Login.domain.entities.LoginInput
import com.didiermendoza.tandamex.src.features.Login.domain.entities.LoginSuccess

interface LoginRepository {
    suspend fun login(input: LoginInput): Result<LoginSuccess>
}