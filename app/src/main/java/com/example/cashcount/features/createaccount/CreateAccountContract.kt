package com.example.cashcount.features.createaccount

import com.example.cashcount.features.createaccount.data.model.AccountType
import com.example.cashcount.mvi.PartialChange

data class CreateAccountState(
    val initialBalance: String = "",
    val accountName: String = "",
    val accountType: AccountType? = null,
    val isContinueEnabled: Boolean = false
)

sealed class CreateAccountIntent {
    data class OnBalanceUpdate(val newBalance: String) : CreateAccountIntent()
    data class OnAccountNameUpdate(val newAccountName: String) : CreateAccountIntent()
    data class OnAccountTypeUpdate(val accountType: AccountType) : CreateAccountIntent()
    object OnContinueClicked : CreateAccountIntent()
}

sealed class CreateAccountSideEffect {
    object AccountCreatedEffect : CreateAccountSideEffect()
}

sealed class CreateAccountPartialChange : PartialChange<CreateAccountState> {
    sealed class OnBalanceUpdateChange(private val isContinueEnabled: Boolean) : CreateAccountPartialChange() {
        override fun reduce(oldState: CreateAccountState): CreateAccountState {
            return when(this) {
                is InvalidBalanceAmount -> {
                    oldState.copy(
                        initialBalance = balance,
                        isContinueEnabled = isContinueEnabled
                    )
                }
                is ValidBalanceAmount -> {
                    oldState.copy(
                        initialBalance = balance,
                        isContinueEnabled = isContinueEnabled
                    )
                }
            }
        }

        data class ValidBalanceAmount(val balance: String, val isContinueEnabled: Boolean): OnBalanceUpdateChange(isContinueEnabled)
        data class InvalidBalanceAmount(val balance: String): OnBalanceUpdateChange(false)
    }

    data class OnAccountNameUpdateChange(val newAccountName: String, val isContinueEnabled: Boolean): CreateAccountPartialChange() {
        override fun reduce(oldState: CreateAccountState): CreateAccountState {
            return oldState.copy(
                accountName = newAccountName,
                isContinueEnabled = isContinueEnabled
            )
        }
    }

    data class OnAccountTypeUpdateChange(val accountType: AccountType, val isContinueEnabled: Boolean): CreateAccountPartialChange() {
        override fun reduce(oldState: CreateAccountState): CreateAccountState {
            return oldState.copy(
                accountType = accountType,
                isContinueEnabled = isContinueEnabled
            )
        }
    }

    object OnContinueClickedChange : CreateAccountPartialChange()
}