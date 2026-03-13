package com.didiermendoza.tandamex.src.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.didiermendoza.tandamex.src.core.database.entities.TandaDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TandaDetailDao {
    @Query("SELECT * FROM tanda_details WHERE id = :tandaId LIMIT 1")
    fun getTandaDetail(tandaId: Int): Flow<TandaDetailEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTandaDetail(tandaDetail: TandaDetailEntity)

    @Query("DELETE FROM tanda_details WHERE id = :tandaId")
    suspend fun deleteTandaDetail(tandaId: Int)
}