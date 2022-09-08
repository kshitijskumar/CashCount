package com.example.cashcount.features.createaccount.domain

import com.example.cashcount.datastore.user.UserDataStore
import com.example.cashcount.features.createaccount.data.IAccountRepository
import com.example.cashcount.features.createaccount.data.model.appmodel.AccountAppModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@ViewModelScoped
class GetAllAccountsOfUserUseCase @Inject constructor(
    private val userDataStore: UserDataStore,
    private val accountRepository: IAccountRepository
) {

    suspend operator fun invoke(): Flow<List<AccountAppModel>> {
        val userId = userDataStore.getCurrentUserId() ?: return flowOf(listOf())
        return accountRepository.getAllAccountsOfUser(userId)
    }

}