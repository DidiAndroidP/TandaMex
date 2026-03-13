package com.didiermendoza.tandamex.src.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "turnos")
data class TurnoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tandaId: Int,
    val participanteId: Int,
    val numeroTurno: Int,
    val fechaCobro: String,
    val montoRecibir: Double,
    val estado: String
)