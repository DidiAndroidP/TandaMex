package com.didiermendoza.tandamex.src.features.wallet.data.datasource.local.mapper

import com.didiermendoza.tandamex.src.core.database.entities.WalletEntity
import com.didiermendoza.tandamex.src.features.wallet.domain.entity.Wallet

fun WalletEntity.toDomain(): Wallet {
    return Wallet(
        userId = this.userId,
        balance = this.balance
    )
}

fun Wallet.toEntity(): WalletEntity {
    return WalletEntity(
        userId = this.userId,
        balance = this.balance
    )
}