package com.didiermendoza.tandamex.src.features.Register.data.datasource.remote.api

import com.didiermendoza.tandamex.src.features.Register.data.datasources.remote.model.RegisterRequestDto
import com.didiermendoza.tandamex.src.features.Register.data.datasources.remote.model.RegisterResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApiService {
    @POST("auth/register")
    suspend fun registerUser(@Body request: RegisterRequestDto): Response<RegisterResponseDto>
}