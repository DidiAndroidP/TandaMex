package com.didiermendoza.tandamex.src.features.Login.data.datasources.remote.mapper
import com.didiermendoza.tandamex.src.features.Login.data.datasources.remote.model.LoginResponseDto
import com.didiermendoza.tandamex.src.features.Login.domain.entities.LoginSuccess

fun LoginResponseDto.toDomain(): LoginSuccess {
    return LoginSuccess(
        token = this.token,
        userId = this.user.id,
        userName = this.user.name,
        email = this.user.email
    )
}