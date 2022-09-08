package com.example.cashcount.features.createaccount.data.model.appmodel

import com.example.cashcount.features.createaccount.data.model.AccountType

data class AccountAppModel(
    val accountId: String,
    val userId: String,
    val accountType: AccountType,
    val accountName: String,
    val balance: Long,
)