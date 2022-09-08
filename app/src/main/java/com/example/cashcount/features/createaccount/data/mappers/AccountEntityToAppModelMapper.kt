package com.example.cashcount.features.createaccount.data.mappers

import com.example.cashcount.features.createaccount.data.model.appmodel.AccountAppModel
import com.example.cashcount.features.createaccount.data.model.entity.AccountEntity

fun AccountEntity.toAccountAppModel(): AccountAppModel {
    return AccountAppModel(
        accountId = accountId,
        userId = userId,
        accountType = accountType,
        accountName = accountName,
        balance = balance
    )
}

fun AccountAppModel.toAccountEntity(): AccountEntity {
    return AccountEntity(
        accountId = accountId,
        userId = userId,
        accountType = accountType,
        accountName = accountName,
        balance = balance
    )
}