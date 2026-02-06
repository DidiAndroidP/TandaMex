package com.didiermendoza.tandamex.src.features.Register.data.datasources.remote.model
import com.google.gson.annotations.SerializedName

data class RegisterRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("phone") val phone: String
)

data class RegisterResponseDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("role") val role: String,
    @SerializedName("active") val active: Boolean,
    @SerializedName("createdAt") val createdAt: String
)