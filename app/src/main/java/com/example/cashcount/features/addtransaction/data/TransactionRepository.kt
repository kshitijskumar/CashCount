package com.example.cashcount.features.addtransaction.data

import com.example.cashcount.features.addtransaction.data.local.ITransactionLocalDataSource
import com.example.cashcount.features.addtransaction.data.mappers.toTransactionEntity
import com.example.cashcount.features.addtransaction.data.model.appmodel.TransactionAppModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class TransactionRepository @Inject constructor(
    private val localDataSource: ITransactionLocalDataSource
) : ITransactionRepository {

    override suspend fun addOrUpdateTransactionWithAccountBalance(transactionAppModel: TransactionAppModel) {
        localDataSource.addOrUpdateTransactionAndAccountBalance(transactionAppModel.toTransactionEntity())
    }
}