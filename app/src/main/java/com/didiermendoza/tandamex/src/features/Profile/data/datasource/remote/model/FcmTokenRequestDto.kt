package com.didiermendoza.tandamex.src.features.Profile.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class FcmTokenRequestDto(
    @SerializedName("token") val token: String
)