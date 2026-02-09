package com.didiermendoza.tandamex.src.features.Login.domain.entities

data class LoginInput(
    val email: String,
    val password: String
)

data class LoginSuccess(
    val token: String,
    val user: User
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val active: Boolean,
    val createdAt: String
)