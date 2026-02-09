package com.didiermendoza.tandamex.src.features.Tanda.domain.entities

data class TandaDetail(
    val id: Int,
    val name: String,
    val contributionAmount: Double,
    val totalMembers: Int,
    val currentMembers: Int,
    val status: String,
    val frequency: String,
    val isMember: Boolean,
    val isAdmin: Boolean,
    val creatorId: Int
)

data class TandaMember(
    val id: Int,
    val name: String,
    val photoUrl: String?,
    val hasPaid: Boolean
)

data class TandaSummary(
    val id: Int,
    val name: String,
    val totalCollected: Double,
    val activeMembers: Int,
    val status: String
)