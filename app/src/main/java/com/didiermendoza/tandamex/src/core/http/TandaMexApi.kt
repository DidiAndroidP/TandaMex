package com.didiermendoza.tandamex.src.core.http

import com.didiermendoza.tandamex.src.features.Home.data.datasources.remote.model.TandaResponseDto
import com.didiermendoza.tandamex.src.features.Login.data.datasources.remote.model.LoginRequestDto
import com.didiermendoza.tandamex.src.features.Login.data.datasources.remote.model.LoginResponseDto
import com.didiermendoza.tandamex.src.features.Profile.data.datasource.remote.model.UpdateProfileRequestDto
import com.didiermendoza.tandamex.src.features.Register.data.datasources.remote.model.RegisterRequestDto
import com.didiermendoza.tandamex.src.features.Register.data.datasources.remote.model.RegisterResponseDto
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model.*
import com.didiermendoza.tandamex.src.features.Profile.data.datasource.remote.model.UserDto as ProfileUserDto
import retrofit2.Response
import retrofit2.http.*

interface TandaMexApi {

    @POST("auth/register")
    suspend fun registerUser(@Body request: RegisterRequestDto): Response<RegisterResponseDto>

    @POST("auth/login")
    suspend fun loginUser(@Body request: LoginRequestDto): Response<LoginResponseDto>

    @GET("users/me")
    suspend fun getMyProfile(): Response<ProfileUserDto>

    @PATCH("users/me")
    suspend fun updateMyProfile(@Body request: UpdateProfileRequestDto): Response<GenericMessageDto>

    @POST("tandas/")
    suspend fun createTanda(@Body request: CreateTandaRequestDto): Response<TandaResponseDto>

    @GET("tandas/available")
    suspend fun getAvailableTandas(): Response<List<TandaResponseDto>>

    @GET("tandas/{id}")
    suspend fun getTandaDetail(@Path("id") tandaId: Int): Response<TandaDetailDto>

    @GET("tandas/{id}/members")
    suspend fun getTandaMembers(@Path("id") tandaId: Int): Response<List<TandaMemberDto>>

    @POST("tandas/{id}/join")
    suspend fun joinTanda(@Path("id") tandaId: Int): Response<JoinResponseDto>

    @POST("tandas/{id}/leave")
    suspend fun leaveTanda(@Path("id") tandaId: Int): Response<JoinResponseDto>

    @POST("tandas/{id}/start")
    suspend fun startTanda(@Path("id") tandaId: Int): Response<GenericMessageDto>

    @POST("tandas/{id}/finish")
    suspend fun finishTanda(@Path("id") tandaId: Int): Response<GenericMessageDto>

    @DELETE("tandas/{id}/delete")
    suspend fun deleteTanda(@Path("id") tandaId: Int): Response<GenericMessageDto>

    @POST("tandas/{id}/schedule")
    suspend fun generateSchedule(@Path("id") tandaId: Int): Response<GenericMessageDto>

    @GET("tandas/{id}/summary")
    suspend fun getTandaSummary(@Path("id") tandaId: Int): Response<TandaSummaryDto>
}