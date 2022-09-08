package com.example.cashcount.features.createaccount.data

import com.example.cashcount.features.createaccount.data.model.appmodel.AccountAppModel
import kotlinx.coroutines.flow.Flow

interface IAccountRepository {

    suspend fun insertOrUpdateAccount(accountAppModel: AccountAppModel)

    fun getAllAccountsOfUser(userId: String): Flow<List<AccountAppModel>>

}