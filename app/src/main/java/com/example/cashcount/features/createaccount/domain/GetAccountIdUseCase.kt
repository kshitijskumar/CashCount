package com.example.cashcount.features.createaccount.domain

import com.example.cashcount.features.createaccount.data.model.AccountType
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetAccountIdUseCase @Inject constructor() {

    operator fun invoke(
        userId: String,
        accountName: String,
        accountType: AccountType
    ): String {
        return "${userId}_${accountName}_${accountType.name}"
    }

}