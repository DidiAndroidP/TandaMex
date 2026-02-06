package com.didiermendoza.tandamex.src.features.Register.domain.entities

data class RegisterInput(
    val name: String,
    val email: String,
    val password: String,
    val phone: String
)