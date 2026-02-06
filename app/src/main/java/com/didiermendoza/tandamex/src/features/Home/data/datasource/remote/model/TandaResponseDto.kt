package com.didiermendoza.tandamex.src.features.Home.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class TandaResponseDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("contributionAmount") val amount: Double,
    @SerializedName("paymentFrequency") val frequency: String,
    @SerializedName("totalMembers") val totalMembers: Int,
    @SerializedName("status") val status: String
)