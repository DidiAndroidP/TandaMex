package com.didiermendoza.tandamex.src.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.didiermendoza.tandamex.src.core.database.entities.TandaMemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TandaMemberDao {
    @Query("SELECT * FROM tanda_members WHERE tandaId = :tandaId")
    fun getTandaMembers(tandaId: Int): Flow<List<TandaMemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTandaMembers(members: List<TandaMemberEntity>)

    @Query("DELETE FROM tanda_members WHERE tandaId = :tandaId")
    suspend fun deleteMembersByTanda(tandaId: Int)

    @Transaction
    suspend fun replaceTandaMembers(tandaId: Int, members: List<TandaMemberEntity>) {
        deleteMembersByTanda(tandaId)
        insertTandaMembers(members)
    }
}