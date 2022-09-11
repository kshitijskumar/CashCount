package com.example.cashcount.features.addtransaction.data.model

import com.example.cashcount.components.DropDownItem

enum class TransactionCategory : DropDownItem {
    SHOPPING,
    FOOD,
    ENTERTAINMENT,
    SALARY,
    GIFT,
    UTILITIES;

    override fun toItemNameString(): String {
        return when(this) {
            SHOPPING -> "Shopping"
            FOOD -> "Food"
            ENTERTAINMENT -> "Entertainment"
            SALARY -> "Salary"
            GIFT -> "Gift"
            UTILITIES -> "Other"
        }
    }
}