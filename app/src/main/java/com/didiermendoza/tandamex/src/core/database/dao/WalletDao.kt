package com.didiermendoza.tandamex.src.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.didiermendoza.tandamex.src.core.database.entities.WalletEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallets WHERE userId = :userId LIMIT 1")
    fun getWalletFlow(userId: Int): Flow<WalletEntity?>

    @Query("SELECT * FROM wallets WHERE userId = :userId LIMIT 1")
    suspend fun getWallet(userId: Int): WalletEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWalletIfNotExists(wallet: WalletEntity)

    @Query("UPDATE wallets SET balance = balance - :amount WHERE userId = :userId")
    suspend fun deductBalance(userId: Int, amount: Double)

    @Query("UPDATE wallets SET balance = balance + :amount WHERE userId = :userId")
    suspend fun addBalance(userId: Int, amount: Double)
}