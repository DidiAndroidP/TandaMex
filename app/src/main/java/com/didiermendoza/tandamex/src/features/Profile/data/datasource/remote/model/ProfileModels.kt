package com.didiermendoza.tandamex.src.features.Profile.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("active") val active: Any?,
    @SerializedName("createdAt") val createdAt: String?
)

data class UpdateProfileRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String
)