package com.didiermendoza.tandamex.src.features.Profile.data.datasource.remote.mapper

import com.didiermendoza.tandamex.src.features.Profile.data.datasource.remote.model.UserDto
import com.didiermendoza.tandamex.src.features.Profile.domain.entities.User

fun UserDto.toDomain(): User {
    val safeName = this.name ?: "Usuario"
    val safeEmail = this.email ?: ""
    val safePhone = this.phone ?: ""

    val isActive = this.active.toString() == "1" || this.active.toString() == "true"

    return User(
        id = this.id,
        name = safeName,
        email = safeEmail,
        phone = safePhone,
        active = isActive,
        initials = if (safeName.isNotEmpty()) safeName.take(2).uppercase() else "??"
    )
}