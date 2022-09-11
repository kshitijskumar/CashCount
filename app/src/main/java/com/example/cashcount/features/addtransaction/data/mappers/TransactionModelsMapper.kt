package com.example.cashcount.features.addtransaction.data.mappers

import com.example.cashcount.features.addtransaction.data.model.appmodel.TransactionAppModel
import com.example.cashcount.features.addtransaction.data.model.entity.TransactionEntity

fun TransactionEntity.toTransactionAppModel(): TransactionAppModel {
    return TransactionAppModel(
        transactionId = transactionId,
        transactionCategory = transactionCategory,
        transactionType = transactionType,
        timestamp = timestamp,
        amount = amount,
        transactionDescription = transactionDescription,
        userId = userId,
        accountId = accountId
    )
}

fun TransactionAppModel.toTransactionEntity(): TransactionEntity {
    return TransactionEntity(
        transactionId = transactionId,
        transactionCategory = transactionCategory,
        transactionType = transactionType,
        timestamp = timestamp,
        amount = amount,
        transactionDescription = transactionDescription,
        userId = userId,
        accountId = accountId
    )
}