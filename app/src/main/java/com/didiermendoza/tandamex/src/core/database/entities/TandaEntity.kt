package com.didiermendoza.tandamex.src.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tandas")
data class TandaEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val amount: Double,
    val frequency: String,
    val progress: Float,
    val totalMembers: Int,
    val status: String
)