package com.didiermendoza.tandamex.src.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tanda_payments")
data class TandaPaymentEntity(
    @PrimaryKey(autoGenerate = true) val paymentId: Int = 0,
    val tandaId: Int,
    val userId: Int,
    val amountPaid: Double,
    val date: String
)
