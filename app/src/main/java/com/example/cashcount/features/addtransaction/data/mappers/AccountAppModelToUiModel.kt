package com.example.cashcount.features.addtransaction.data.mappers

import com.example.cashcount.features.addtransaction.data.model.appmodel.AccountUiModel
import com.example.cashcount.features.createaccount.data.model.appmodel.AccountAppModel

fun AccountAppModel.toAccountUiModel(): AccountUiModel {
    return AccountUiModel(
        accountId = accountId,
        accountName = accountName,
        accountType = accountType
    )
}