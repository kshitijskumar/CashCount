package com.example.cashcount.features.addtransaction.data

import com.example.cashcount.features.addtransaction.data.model.appmodel.TransactionAppModel

interface ITransactionRepository {

    suspend fun addOrUpdateTransactionWithAccountBalance(transactionAppModel: TransactionAppModel)

}