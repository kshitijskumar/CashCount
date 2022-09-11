package com.example.cashcount.features.addtransaction.data.local

import com.example.cashcount.features.addtransaction.data.model.entity.TransactionEntity
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class TransactionLocalDataSource @Inject constructor(
    private val transactionDao: TransactionDao
) : ITransactionLocalDataSource {

    override suspend fun addOrUpdateTransactionAndAccountBalance(transactionEntity: TransactionEntity) {
        transactionDao.addOrUpdateTransactionAndUpdateAccountBalance(transactionEntity)
    }

}