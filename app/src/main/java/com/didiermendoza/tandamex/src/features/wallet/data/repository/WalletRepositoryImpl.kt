package com.didiermendoza.tandamex.src.features.wallet.data.repository

import com.didiermendoza.tandamex.src.core.database.dao.WalletDao
import com.didiermendoza.tandamex.src.features.wallet.data.datasource.local.mapper.toDomain
import com.didiermendoza.tandamex.src.features.wallet.data.datasource.local.mapper.toEntity
import com.didiermendoza.tandamex.src.features.wallet.domain.entity.Wallet
import com.didiermendoza.tandamex.src.features.wallet.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val walletDao: WalletDao
) : WalletRepository {

    override suspend fun checkWalletExists(userId: Int): Boolean {
        return walletDao.getWallet(userId) != null
    }

    override suspend fun createDefaultWallet(wallet: Wallet) {
        walletDao.insertWalletIfNotExists(wallet.toEntity())
    }

    override fun getWalletFlow(userId: Int): Flow<Wallet?> {
        return walletDao.getWalletFlow(userId).map { entity ->
            entity?.toDomain()
        }
    }
}