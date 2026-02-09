package com.didiermendoza.tandamex.src.features.Profile.domain.entities

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val active: Boolean,
    val initials: String
)