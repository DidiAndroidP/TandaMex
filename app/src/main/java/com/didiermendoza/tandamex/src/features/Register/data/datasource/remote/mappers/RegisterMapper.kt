package com.didiermendoza.tandamex.src.features.Register.data.datasources.remote.mapper

import com.didiermendoza.tandamex.src.features.Register.data.datasources.remote.model.RegisterResponseDto
import com.didiermendoza.tandamex.src.features.Register.domain.entities.User

fun RegisterResponseDto.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email,
        phone = this.phone,
        isActive = this.active
    )
}