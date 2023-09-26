package com.example.atelier7.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: TransactionBill)

    @Query("DELETE FROM TransactionBill WHERE id = :transactionId")
    suspend fun deleteTransactionById(transactionId: Int)


    @Query("SELECT * FROM TransactionBill")
    fun getAllTransactions(): LiveData<List<TransactionBill>>

    @Query("SELECT * FROM TransactionBill WHERE id = :transactionId")
    suspend fun getTransactionById(transactionId: Int): TransactionBill

    @Update
    suspend fun updateTransaction(transaction: TransactionBill)
}