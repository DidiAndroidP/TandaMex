package com.didiermendoza.tandamex.src.features.Home.domain.entities
data class Tanda(
    val id: Int,
    val name: String,
    val amount: Double,
    val frequency: String,
    val totalMembers: Int,
    val progress: Float
)