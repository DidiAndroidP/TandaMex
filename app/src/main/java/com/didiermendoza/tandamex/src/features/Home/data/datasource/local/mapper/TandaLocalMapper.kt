package com.didiermendoza.tandamex.src.features.Home.data.datasource.local.mapper

import com.didiermendoza.tandamex.src.core.database.entities.TandaEntity
import com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda

fun TandaEntity.toDomain() = Tanda(
    id = id,
    name = name,
    amount = amount,
    frequency = frequency,
    progress = progress,
    totalMembers = totalMembers,
    status = status
)