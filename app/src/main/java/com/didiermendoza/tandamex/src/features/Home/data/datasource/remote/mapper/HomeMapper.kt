package com.didiermendoza.tandamex.src.features.Home.data.datasource.remote.mapper

import com.didiermendoza.tandamex.src.core.database.entities.TandaEntity
import com.didiermendoza.tandamex.src.features.Home.data.datasources.remote.model.TandaResponseDto

fun TandaResponseDto.toEntity() = TandaEntity(
    id = id,
    name = name,
    amount = contributionAmount,
    frequency = paymentFrequency,
    progress = 0f,
    totalMembers = totalMembers,
    status = status
)