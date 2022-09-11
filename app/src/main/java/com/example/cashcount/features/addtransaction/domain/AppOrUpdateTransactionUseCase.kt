package com.example.cashcount.features.addtransaction.domain

import com.example.cashcount.datastore.user.UserDataStore
import com.example.cashcount.features.addtransaction.data.ITransactionRepository
import com.example.cashcount.features.addtransaction.data.model.TransactionCategory
import com.example.cashcount.features.addtransaction.data.model.TransactionType
import com.example.cashcount.features.addtransaction.data.model.appmodel.TransactionAppModel
import javax.inject.Inject

class AppOrUpdateTransactionUseCase @Inject constructor(
    private val transactionRepository: ITransactionRepository,
    private val userDataStore: UserDataStore,
    private val createTransactionIdUseCase: CreateTransactionIdUseCase
) {

    suspend operator fun invoke(
        amount: Long,
        transactionType: TransactionType,
        transactionCategory: TransactionCategory,
        description: String,
        accountId: String,
        timestamp: Long
    ) {
        val transactionId = createTransactionIdUseCase.invoke(timestamp)
        val userId = userDataStore.getCurrentUserId() ?: ""

        val transactionAppModel = TransactionAppModel(
            transactionId = transactionId,
            transactionCategory = transactionCategory,
            transactionType = transactionType,
            timestamp = timestamp,
            amount = amount,
            transactionDescription = description,
            userId = userId,
            accountId = accountId
        )

        transactionRepository.addOrUpdateTransactionWithAccountBalance(transactionAppModel)
    }

}