package com.didiermendoza.tandamex.src.features.wallet.domain.repository


import com.didiermendoza.tandamex.src.features.wallet.domain.entity.Wallet
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    suspend fun checkWalletExists(userId: Int): Boolean
    suspend fun createDefaultWallet(wallet: Wallet)
    fun getWalletFlow(userId: Int): Flow<Wallet?>
}