package com.example.atelier7.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransactionBill(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var billAmount: String,
    var isTaxAdded: Boolean,
    var tipPercentage: Float,
    var totalBill: Float,
    var numberOfPeople: Int
)