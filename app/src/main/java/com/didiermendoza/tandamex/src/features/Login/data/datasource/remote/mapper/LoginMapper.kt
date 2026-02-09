package com.didiermendoza.tandamex.src.features.Login.data.datasources.remote.mapper

import com.didiermendoza.tandamex.src.features.Login.data.datasources.remote.model.LoginResponseDto
import com.didiermendoza.tandamex.src.features.Login.data.datasources.remote.model.UserDto
import com.didiermendoza.tandamex.src.features.Login.domain.entities.LoginSuccess
import com.didiermendoza.tandamex.src.features.Login.domain.entities.User

fun LoginResponseDto.toDomain(): LoginSuccess {
    return LoginSuccess(
        token = this.token,
        user = this.user.toDomain()
    )
}

fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email,
        active = this.active == 1,
        createdAt = this.createdAt
    )
}