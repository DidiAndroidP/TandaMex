package com.didiermendoza.tandamex.src.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_summaries")
data class ScheduleSummaryEntity(
    @PrimaryKey val tandaId: Int,
    val fechaFin: String,
    val montoTotal: Double
)