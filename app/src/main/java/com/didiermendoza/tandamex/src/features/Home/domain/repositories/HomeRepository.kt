package com.didiermendoza.tandamex.src.features.Home.domain.repositories

import com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda
import com.didiermendoza.tandamex.src.features.wallet.domain.entity.Wallet
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getAvailableTandas(): Flow<List<Tanda>>
    suspend fun syncTandas()
    suspend fun checkWalletExists(userId: Int): Boolean
    suspend fun createDefaultWallet(wallet: Wallet)
}