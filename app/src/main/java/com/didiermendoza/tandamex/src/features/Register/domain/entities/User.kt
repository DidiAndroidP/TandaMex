package com.didiermendoza.tandamex.src.features.Register.domain.entities

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val isActive: Boolean
)