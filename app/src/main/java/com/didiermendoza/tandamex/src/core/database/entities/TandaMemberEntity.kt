package com.didiermendoza.tandamex.src.core.database.entities

import androidx.room.Entity

@Entity(
    tableName = "tanda_members",
    primaryKeys = ["id", "tandaId"]
)
data class TandaMemberEntity(
    val id: Int,
    val tandaId: Int,
    val name: String,
    val photoUrl: String?,
    val hasPaid: Boolean
)