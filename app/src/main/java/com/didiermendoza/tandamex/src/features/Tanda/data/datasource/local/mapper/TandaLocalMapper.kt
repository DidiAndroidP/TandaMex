package com.didiermendoza.tandamex.src.features.Tanda.data.datasources.local.mapper

import com.didiermendoza.tandamex.src.core.database.entities.TandaDetailEntity
import com.didiermendoza.tandamex.src.core.database.entities.TandaMemberEntity
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaDetail
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaMember

fun TandaDetailEntity.toDomain() = TandaDetail(
    id = id,
    name = name,
    contributionAmount = contributionAmount,
    totalMembers = totalMembers,
    currentMembers = currentMembers,
    status = status,
    frequency = frequency,
    isMember = isMember,
    isAdmin = isAdmin,
    creatorId = creatorId
)

fun TandaMemberEntity.toDomain() = TandaMember(
    id = id,
    name = name,
    photoUrl = photoUrl,
    hasPaid = hasPaid
)