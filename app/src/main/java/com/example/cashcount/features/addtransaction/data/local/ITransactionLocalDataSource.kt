package com.example.cashcount.features.addtransaction.data.local

import com.example.cashcount.features.addtransaction.data.model.entity.TransactionEntity

interface ITransactionLocalDataSource {

    suspend fun addOrUpdateTransactionAndAccountBalance(
        transactionEntity: TransactionEntity
    )

}