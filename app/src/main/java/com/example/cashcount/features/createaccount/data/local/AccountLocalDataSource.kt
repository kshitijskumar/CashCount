package com.example.cashcount.features.createaccount.data.local

import com.example.cashcount.features.createaccount.data.mappers.toAccountAppModel
import com.example.cashcount.features.createaccount.data.mappers.toAccountEntity
import com.example.cashcount.features.createaccount.data.model.appmodel.AccountAppModel
import com.example.cashcount.features.createaccount.data.model.entity.AccountEntity
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class AccountLocalDataSource @Inject constructor(
    private val accountDao: AccountDao
) : IAccountLocalDataSource {

    override suspend fun insertOrUpdateAccount(accountEntity: AccountEntity) {
        accountDao.insertOrUpdateAccount(accountEntity)
    }

    override fun getAllAccountsOfUser(userId: String): Flow<List<AccountEntity>> {
        return accountDao.getAllAccountsForUserId(userId = userId)
    }
}