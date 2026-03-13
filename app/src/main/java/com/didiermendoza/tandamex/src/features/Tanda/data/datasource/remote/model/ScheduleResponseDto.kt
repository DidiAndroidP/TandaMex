package com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class ScheduleResponseDto(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ScheduleDataDto
)

data class ScheduleDataDto(
    @SerializedName("tandaId") val tandaId: Int,
    @SerializedName("turnosAsignados") val turnos: List<TurnoDto>,
    @SerializedName("resumen") val resumen: ResumenScheduleDto
)

data class TurnoDto(
    @SerializedName("participanteId") val participanteId: Int,
    @SerializedName("numeroTurno") val numeroTurno: Int,
    @SerializedName("fechaCobro") val fechaCobro: String,
    @SerializedName("montoRecibir") val montoRecibir: Double,
    @SerializedName("estado") val estado: String
)

data class ResumenScheduleDto(
    @SerializedName("fechaFinalizacionEstimada") val fechaFin: String,
    @SerializedName("montoTotalPorParticipante") val total: Double
)