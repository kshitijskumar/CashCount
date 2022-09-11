package com.example.cashcount.features.addtransaction.data.model.appmodel

import com.example.cashcount.components.DropDownItem
import com.example.cashcount.features.createaccount.data.model.AccountType

data class AccountUiModel(
    val accountId: String,
    val accountName: String,
    val accountType: AccountType
) : DropDownItem {
    override fun toItemNameString(): String {
        return accountName
    }
}
