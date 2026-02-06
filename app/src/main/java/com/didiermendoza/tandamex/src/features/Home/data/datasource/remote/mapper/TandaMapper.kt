package com.didiermendoza.tandamex.src.features.Home.data.datasources.remote.mapper

import com.didiermendoza.tandamex.src.features.Home.data.datasources.remote.model.TandaResponseDto
import com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda

fun TandaResponseDto.toDomain(): Tanda {
    return Tanda(
        id = this.id,
        name = this.name,
        amount = this.amount,
        frequency = this.frequency,
        totalMembers = this.totalMembers,
        progress = 0.1f
    )
}