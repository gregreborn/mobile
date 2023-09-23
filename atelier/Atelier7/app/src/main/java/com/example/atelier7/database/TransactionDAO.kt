package com.example.atelier7.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDao {
    @Insert
    fun insertTransaction(transaction: TransactionBill)

    @Query("DELETE FROM TransactionBill WHERE id = :transactionId")
    fun deleteTransactionById(transactionId: Int)

    @Query("SELECT * FROM TransactionBill")
    fun getAllTransactions(): LiveData<List<TransactionBill>>
}