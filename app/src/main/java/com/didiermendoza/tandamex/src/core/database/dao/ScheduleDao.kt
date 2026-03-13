package com.didiermendoza.tandamex.src.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.didiermendoza.tandamex.src.core.database.entities.ScheduleSummaryEntity
import com.didiermendoza.tandamex.src.core.database.entities.TurnoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScheduleSummary(summary: ScheduleSummaryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTurnos(turnos: List<TurnoEntity>)

    @Query("SELECT * FROM schedule_summaries WHERE tandaId = :tandaId")
    fun getScheduleSummary(tandaId: Int): Flow<ScheduleSummaryEntity?>

    @Query("SELECT * FROM turnos WHERE tandaId = :tandaId ORDER BY numeroTurno ASC")
    fun getTurnos(tandaId: Int): Flow<List<TurnoEntity>>

    @Query("DELETE FROM schedule_summaries WHERE tandaId = :tandaId")
    suspend fun deleteScheduleSummary(tandaId: Int)

    @Query("DELETE FROM turnos WHERE tandaId = :tandaId")
    suspend fun deleteTurnos(tandaId: Int)

    @Transaction
    suspend fun clearAndInsertSchedule(tandaId: Int, summary: ScheduleSummaryEntity, turnos: List<TurnoEntity>) {
        deleteScheduleSummary(tandaId)
        deleteTurnos(tandaId)
        insertScheduleSummary(summary)
        insertTurnos(turnos)
    }
}