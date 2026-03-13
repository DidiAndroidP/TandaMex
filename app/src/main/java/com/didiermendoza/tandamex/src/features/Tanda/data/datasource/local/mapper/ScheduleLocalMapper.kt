package com.didiermendoza.tandamex.src.features.Tanda.data.datasources.local.mapper

import com.didiermendoza.tandamex.src.core.database.entities.ScheduleSummaryEntity
import com.didiermendoza.tandamex.src.core.database.entities.TurnoEntity
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.ScheduleData
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.Turno

fun ScheduleData.toSummaryEntity() = ScheduleSummaryEntity(
    tandaId = tandaId,
    fechaFin = fechaFin,
    montoTotal = montoTotal
)

fun Turno.toEntity(tandaId: Int) = TurnoEntity(
    tandaId = tandaId,
    participanteId = participanteId,
    numeroTurno = numeroTurno,
    fechaCobro = fechaCobro,
    montoRecibir = montoRecibir,
    estado = estado
)

fun TurnoEntity.toDomain() = Turno(
    participanteId = participanteId,
    numeroTurno = numeroTurno,
    fechaCobro = fechaCobro,
    montoRecibir = montoRecibir,
    estado = estado
)