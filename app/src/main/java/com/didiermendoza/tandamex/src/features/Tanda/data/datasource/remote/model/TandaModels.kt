package com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class CreateTandaRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("contributionAmount") val contributionAmount: Double,
    @SerializedName("paymentFrequency") val paymentFrequency: String,
    @SerializedName("totalMembers") val totalMembers: Int,
    @SerializedName("delayToleranceDays") val delayToleranceDays: Int,
    @SerializedName("penaltyPerDay") val penaltyPerDay: Double
)

data class TandaDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("contributionAmount") val contributionAmount: Double,
    @SerializedName("totalMembers") val totalMembers: Int,
    @SerializedName("currentMembers") val currentMembers: Int,
    @SerializedName("status") val status: String,
    @SerializedName("paymentFrequency") val paymentFrequency: String?,
    @SerializedName("isMember") val isMember: Boolean = false,
    @SerializedName("creatorId") val creatorId: Int,
    @SerializedName("isAdmin") val isAdmin: Boolean
)

data class TandaMemberDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("photo") val photo: String?,
    @SerializedName("alreadyPaid") val alreadyPaid: Boolean
)

data class JoinResponseDto(
    @SerializedName("message") val message: String
)

data class GenericMessageDto(
    @SerializedName("message") val message: String
)

data class TandaSummaryDto(
    @SerializedName("tandaId") val tandaId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("totalCollected") val totalCollected: Double,
    @SerializedName("activeMembers") val activeMembers: Int,
    @SerializedName("status") val status: String
)