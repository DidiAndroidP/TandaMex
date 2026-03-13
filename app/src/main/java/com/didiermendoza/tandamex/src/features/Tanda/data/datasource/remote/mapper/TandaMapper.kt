package com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.mapper

import com.didiermendoza.tandamex.src.core.database.entities.TandaDetailEntity
import com.didiermendoza.tandamex.src.core.database.entities.TandaMemberEntity
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model.TandaDetailDto
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model.TandaMemberDto
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model.TandaSummaryDto
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaSummary

fun TandaDetailDto.toEntity(): TandaDetailEntity {
    return TandaDetailEntity(
        id = this.id,
        name = this.name,
        contributionAmount = this.contributionAmount,
        totalMembers = this.totalMembers,
        currentMembers = this.currentMembers,
        status = this.status,
        frequency = this.paymentFrequency ?: "Semanal",
        isMember = this.isMember,
        isAdmin = this.isAdmin,
        creatorId = this.creatorId
    )
}

fun TandaMemberDto.toEntity(tandaId: Int): TandaMemberEntity {
    return TandaMemberEntity(
        id = this.id,
        tandaId = tandaId,
        name = this.name,
        photoUrl = this.photo,
        hasPaid = this.alreadyPaid
    )
}

fun TandaSummaryDto.toDomain(): TandaSummary {
    return TandaSummary(
        id = this.tandaId,
        name = this.name,
        totalCollected = this.totalCollected,
        activeMembers = this.activeMembers,
        status = this.status
    )
}