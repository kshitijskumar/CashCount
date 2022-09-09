package com.example.cashcount.features.createaccount.data.model

enum class AccountType {
    BANK,
    UPI,
    CREDIT_CARD,
    WALLET,
    CASH;

    companion object {
        fun getAccountTypesList(): List<AccountType> {
            return AccountType.values().toList()
        }

        fun stringToAccountType(accountString: String): AccountType {
            return when(accountString) {
                "Bank" -> AccountType.BANK
                "UPI" -> AccountType.UPI
                "Credit card" -> AccountType.CREDIT_CARD
                "Wallet" -> AccountType.WALLET
                "Cash" -> AccountType.CASH
                else -> throw IllegalArgumentException("Unknown account type $accountString passed")
            }
        }
    }
}

fun AccountType.toAccountTypeString(): String {
    return when(this) {
        AccountType.BANK -> "Bank"
        AccountType.UPI -> "UPI"
        AccountType.CREDIT_CARD -> "Credit card"
        AccountType.WALLET -> "Wallet"
        AccountType.CASH -> "Cash"
    }
}