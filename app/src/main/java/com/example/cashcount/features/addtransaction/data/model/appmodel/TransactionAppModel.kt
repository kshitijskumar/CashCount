package com.example.cashcount.features.addtransaction.data.model.appmodel

import com.example.cashcount.features.addtransaction.data.model.TransactionCategory
import com.example.cashcount.features.addtransaction.data.model.TransactionType

data class TransactionAppModel(
    val transactionId: String,
    val transactionCategory: TransactionCategory,
    val transactionType: TransactionType,
    val timestamp: Long,
    val amount: Long,
    val transactionDescription: String,
    val userId: String,
    val accountId: String
)
