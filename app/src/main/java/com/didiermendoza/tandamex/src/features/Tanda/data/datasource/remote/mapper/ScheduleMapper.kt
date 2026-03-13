package com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.mapper

import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model.ScheduleDataDto
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model.TurnoDto
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.ScheduleData
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.Turno

fun ScheduleDataDto.toDomain(): ScheduleData {
    return ScheduleData(
        tandaId = this.tandaId,
        turnos = this.turnos.map { it.toDomain() },
        fechaFin = this.resumen.fechaFin,
        montoTotal = this.resumen.total
    )
}

fun TurnoDto.toDomain(): Turno {
    return Turno(
        participanteId = this.participanteId,
        numeroTurno = this.numeroTurno,
        fechaCobro = this.fechaCobro,
        montoRecibir = this.montoRecibir,
        estado = this.estado
    )
}