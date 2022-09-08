package com.example.cashcount.features.createaccount.domain

import com.example.cashcount.datastore.user.UserDataStore
import com.example.cashcount.features.createaccount.data.IAccountRepository
import com.example.cashcount.features.createaccount.data.model.AccountType
import com.example.cashcount.features.createaccount.data.model.appmodel.AccountAppModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@ViewModelScoped
class CreateAccountUseCase @Inject constructor(
    private val userDataStore: UserDataStore,
    private val accountRepository: IAccountRepository,
    private val getAccountIdUseCase: GetAccountIdUseCase
){

    suspend operator fun invoke(
        accountName: String,
        accountType: AccountType,
        initialBalance: Long
    ) {
        val userId = userDataStore.getCurrentUserId() ?: return
        val accountId = getAccountIdUseCase.invoke(userId, accountName, accountType)

        val accountAppModel = AccountAppModel(
            accountId = accountId,
            userId = userId,
            accountType = accountType,
            accountName = accountName,
            balance = initialBalance
        )
        accountRepository.insertOrUpdateAccount(
            accountAppModel = accountAppModel
        )
    }

}