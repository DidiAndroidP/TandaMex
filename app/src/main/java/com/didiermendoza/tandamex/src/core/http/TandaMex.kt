package com.didiermendoza.tandamex.src.core.http

import com.didiermendoza.tandamex.src.features.Login.data.model.LoginRequest
import com.didiermendoza.tandamex.src.features.Login.data.model.LoginResponse
import com.didiermendoza.tandamex.src.features.Register.data.model.RegisterRequest
import com.didiermendoza.tandamex.src.features.Register.data.model.RegisterResponse
import com.didiermendoza.tandamex.src.features.Tanda.data.model.* import retrofit2.Response
import retrofit2.http.*

interface TandaMexApi {

    @POST("auth/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @GET("users/me")
    suspend fun getMyProfile(): Response<UserResponse>

    @PATCH("users/me")
    suspend fun updateMyProfile(@Body request: UpdateProfileRequest): Response<UserResponse>

    @POST("tandas/")
    suspend fun createTanda(@Body request: CreateTandaRequest): Response<TandaResponse>

    @GET("tandas/available")
    suspend fun getAvailableTandas(): Response<List<TandaResponse>>

    @GET("tandas/{id}")
    suspend fun getTandaDetail(@Path("id") tandaId: Int): Response<TandaDetailResponse>

    @POST("tandas/{id}/join")
    suspend fun joinTanda(@Path("id") tandaId: Int): Response<JoinResponse>

    @POST("tandas/{id}/leave")
    suspend fun leaveTanda(@Path("id") tandaId: Int): Response<JoinResponse>

    @GET("tandas/{id}/summary")
    suspend fun getTandaSummary(@Path("id") tandaId: Int): Response<TandaSummaryResponse>

    @POST("payments/")
    suspend fun registerPayment(@Body request: PaymentRequest): Response<PaymentResponse>
}