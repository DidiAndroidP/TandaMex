package com.didiermendoza.tandamex.src.features.wallet.domain.usecases

import com.didiermendoza.tandamex.src.features.Home.domain.repositories.HomeRepository
import com.didiermendoza.tandamex.src.features.wallet.domain.entity.Wallet
import javax.inject.Inject

class CreateDefaultWalletUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(userId: Int, initialBalance: Double = 10000.0) {
        val newWallet = Wallet(
            userId,
            initialBalance
        )
        repository.createDefaultWallet(newWallet)
    }
}