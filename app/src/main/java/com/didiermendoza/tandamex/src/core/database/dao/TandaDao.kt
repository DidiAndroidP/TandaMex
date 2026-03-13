package com.didiermendoza.tandamex.src.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.didiermendoza.tandamex.src.core.database.entities.TandaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TandaDao {
    @Query("SELECT * FROM tandas")
    fun getAllTandas(): Flow<List<TandaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTandas(tandas: List<TandaEntity>)

    @Query("DELETE FROM tandas")
    suspend fun clearTandas()
}