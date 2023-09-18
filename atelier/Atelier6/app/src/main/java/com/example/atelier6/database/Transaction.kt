package com.example.atelier6.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransactionBill(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val billAmount: String,
    val isTaxAdded: Boolean,
    val tipPercentage: Float,
    val totalBill: Float,
    val numberOfPeople: Int
)
