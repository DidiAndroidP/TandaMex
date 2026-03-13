package com.didiermendoza.tandamex.src.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tanda_details")
data class TandaDetailEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val contributionAmount: Double,
    val totalMembers: Int,
    val currentMembers: Int,
    val status: String,
    val frequency: String,
    val isMember: Boolean,
    val isAdmin: Boolean,
    val creatorId: Int
)