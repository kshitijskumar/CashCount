package com.example.cashcount.features.addtransaction.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cashcount.features.addtransaction.data.model.TransactionCategory
import com.example.cashcount.features.addtransaction.data.model.TransactionType

@Entity(tableName = "transaction_table")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = false) val transactionId: String,
    val transactionCategory: TransactionCategory,
    val transactionType: TransactionType,
    val timestamp: Long,
    val amount: Long,
    val transactionDescription: String,
    val userId: String,
    val accountId: String
)
