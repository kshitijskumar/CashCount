package com.example.cashcount.features.addtransaction.domain

import com.example.cashcount.datastore.user.UserDataStore
import com.example.cashcount.features.addtransaction.data.model.TransactionCategory
import com.example.cashcount.features.addtransaction.data.model.TransactionType
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class CreateTransactionIdUseCase @Inject constructor(
    private val userDataStore: UserDataStore
) {
    
    suspend operator fun invoke(
        timestamp: Long
    ): String {
        return "${userDataStore.getCurrentUserId()}_$timestamp"
    }
    
}