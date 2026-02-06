package com.didiermendoza.tandamex.src.features.Login.domain.entities

data class LoginInput(
    val email: String,
    val password: String
)

data class LoginSuccess(
    val token: String,
    val userId: Int,
    val userName: String,
    val email: String
)