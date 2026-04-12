package com.didiermendoza.tandamex.src.features.Profile.data.datasource.remote.api

import com.didiermendoza.tandamex.src.features.Profile.data.datasource.remote.model.UpdateProfileRequestDto
import com.didiermendoza.tandamex.src.features.Profile.data.datasource.remote.model.UserDto
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model.GenericMessageDto
import com.didiermendoza.tandamex.src.features.Profile.data.datasources.remote.model.FcmTokenRequestDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface ProfileApiService {
    @GET("users/me")
    suspend fun getMyProfile(): Response<UserDto>

    @PATCH("users/me")
    suspend fun updateMyProfile(@Body request: UpdateProfileRequestDto): Response<GenericMessageDto>

    @Multipart
    @POST("users/me/photo")
    suspend fun uploadProfilePhoto(
        @Part photo: MultipartBody.Part
    ): Response<GenericMessageDto>

    @PATCH("users/me/fcm-token")
    suspend fun sendFcmToken(@Body request: FcmTokenRequestDto): Response<GenericMessageDto>
}