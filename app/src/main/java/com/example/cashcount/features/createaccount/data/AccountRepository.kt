package com.example.cashcount.features.createaccount.data

import com.example.cashcount.features.createaccount.data.local.AccountLocalDataSource
import com.example.cashcount.features.createaccount.data.local.IAccountLocalDataSource
import com.example.cashcount.features.createaccount.data.mappers.toAccountAppModel
import com.example.cashcount.features.createaccount.data.mappers.toAccountEntity
import com.example.cashcount.features.createaccount.data.model.appmodel.AccountAppModel
import com.example.cashcount.features.createaccount.data.model.entity.AccountEntity
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class AccountRepository @Inject constructor(
    private val accountLocalDataSource: IAccountLocalDataSource
) : IAccountRepository {

    override suspend fun insertOrUpdateAccount(accountAppModel: AccountAppModel) {
        return accountLocalDataSource.insertOrUpdateAccount(accountAppModel.toAccountEntity())
    }

    override fun getAllAccountsOfUser(userId: String): Flow<List<AccountAppModel>> {
        return accountLocalDataSource.getAllAccountsOfUser(userId).map { it.map { it.toAccountAppModel() } }
    }

}