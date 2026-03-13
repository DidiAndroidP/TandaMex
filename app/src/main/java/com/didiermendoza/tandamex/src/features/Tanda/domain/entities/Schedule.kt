package com.didiermendoza.tandamex.src.features.Tanda.domain.entities

data class ScheduleData(
    val tandaId: Int,
    val turnos: List<Turno>,
    val fechaFin: String,
    val montoTotal: Double
)

data class Turno(
    val participanteId: Int,
    val numeroTurno: Int,
    val fechaCobro: String,
    val montoRecibir: Double,
    val estado: String
)