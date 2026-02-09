package com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.mapper

import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model.TandaDetailDto
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model.TandaMemberDto
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model.TandaSummaryDto
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaDetail
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaMember
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaSummary

fun TandaDetailDto.toDomain(): TandaDetail {
    return TandaDetail(
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

fun TandaMemberDto.toDomain(): TandaMember {
    return TandaMember(
        id = this.id,
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