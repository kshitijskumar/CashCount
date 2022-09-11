package com.example.cashcount.features.addtransaction.data.model

import com.example.cashcount.components.DropDownItem

enum class TransactionType : DropDownItem {
    EXPENSE,
    INCOME;

    override fun toItemNameString(): String {
        return when(this) {
            EXPENSE -> "Expense"
            INCOME -> "Income"
        }
    }
}