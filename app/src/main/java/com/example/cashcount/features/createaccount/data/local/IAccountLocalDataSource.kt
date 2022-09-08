package com.example.cashcount.features.createaccount.data.local

import com.example.cashcount.features.createaccount.data.model.appmodel.AccountAppModel
import com.example.cashcount.features.createaccount.data.model.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

interface IAccountLocalDataSource {

    suspend fun insertOrUpdateAccount(accountEntity: AccountEntity)

    fun getAllAccountsOfUser(userId: String): Flow<List<AccountEntity>>

}