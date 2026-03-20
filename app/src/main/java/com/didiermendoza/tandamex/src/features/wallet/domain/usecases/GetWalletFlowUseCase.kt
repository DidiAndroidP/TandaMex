package com.didiermendoza.tandamex.src.features.wallet.domain.usecases

import com.didiermendoza.tandamex.src.features.wallet.domain.entity.Wallet
import com.didiermendoza.tandamex.src.features.wallet.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWalletFlowUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    operator fun invoke(userId: Int): Flow<Wallet?> {
        return repository.getWalletFlow(userId)
    }
}