package com.didiermendoza.tandamex.src.features.Login.data.datasource.remote.api

import com.didiermendoza.tandamex.src.features.Login.data.datasources.remote.model.LoginRequestDto
import com.didiermendoza.tandamex.src.features.Login.data.datasources.remote.model.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {
    @POST("auth/login")
    suspend fun loginUser(@Body request: LoginRequestDto): Response<LoginResponseDto>
}