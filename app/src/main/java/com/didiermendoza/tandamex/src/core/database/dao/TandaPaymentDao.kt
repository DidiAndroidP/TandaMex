package com.didiermendoza.tandamex.src.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.didiermendoza.tandamex.src.core.database.entities.TandaPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TandaPaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: TandaPaymentEntity)
    
    @Query("SELECT * FROM tanda_payments WHERE tandaId = :tandaId")
    fun getPaymentsByTanda(tandaId: Int): Flow<List<TandaPaymentEntity>>

    @Query("SELECT COUNT(*) > 0 FROM tanda_payments WHERE tandaId = :tandaId AND userId = :userId")
    suspend fun hasUserPaid(tandaId: Int, userId: Int): Boolean

    @Query("DELETE FROM tanda_payments WHERE tandaId = :tandaId")
    suspend fun deletePaymentsByTanda(tandaId: Int)

    @Query("DELETE FROM tanda_payments WHERE tandaId = :tandaId AND userId = :userId")
    suspend fun deleteUserPayment(tandaId: Int, userId: Int)

    @Query("DELETE FROM tanda_payments WHERE tandaId = :tandaId AND userId = :userId")
    suspend fun deletePaymentsByTandaAndUser(tandaId: Int, userId: Int)
}
