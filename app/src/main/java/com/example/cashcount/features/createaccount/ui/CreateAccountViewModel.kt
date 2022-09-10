package com.example.cashcount.features.createaccount.ui

import com.example.cashcount.components.RupeeSymbol
import com.example.cashcount.features.createaccount.CreateAccountIntent
import com.example.cashcount.features.createaccount.CreateAccountPartialChange
import com.example.cashcount.features.createaccount.CreateAccountSideEffect
import com.example.cashcount.features.createaccount.CreateAccountState
import com.example.cashcount.features.createaccount.data.model.AccountType
import com.example.cashcount.features.createaccount.domain.CreateAccountUseCase
import com.example.cashcount.features.createaccount.domain.ValidateAmountStringUseCase
import com.example.cashcount.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val validateAmountStringUseCase: ValidateAmountStringUseCase,
    private val createAccountUseCase: CreateAccountUseCase
): MviViewModel<CreateAccountState, CreateAccountIntent, CreateAccountSideEffect, CreateAccountPartialChange>() {

    override fun initialState(): CreateAccountState {
        return CreateAccountState()
    }

    override fun Flow<CreateAccountIntent>.toPartialChange(): Flow<CreateAccountPartialChange> {
        return merge(
            handleOnBalanceUpdateIntent(filterIsInstance()),
            handleOnAccountNameUpdateIntent(filterIsInstance()),
            handleOnAccountTypeUpdateIntent(filterIsInstance()),
            handleOnContinueClickedIntent(filterIsInstance()),
        )
    }

    override fun sideEffect(change: CreateAccountPartialChange): List<CreateAccountSideEffect> {
        val effect: CreateAccountSideEffect? = when(change) {
            is CreateAccountPartialChange.OnAccountNameUpdateChange -> null
            is CreateAccountPartialChange.OnAccountTypeUpdateChange -> null
            is CreateAccountPartialChange.OnBalanceUpdateChange.InvalidBalanceAmount -> null
            is CreateAccountPartialChange.OnBalanceUpdateChange.ValidBalanceAmount -> null
            CreateAccountPartialChange.OnContinueClickedChange -> CreateAccountSideEffect.AccountCreatedEffect
        }

        return mutableListOf<CreateAccountSideEffect>().apply {
            effect?.let { add(it) }
        }
    }

    private fun handleOnBalanceUpdateIntent(
        flow: Flow<CreateAccountIntent.OnBalanceUpdate>
    ): Flow<CreateAccountPartialChange.OnBalanceUpdateChange> {
        return flow.map {
            val isValidAmount = validateAmountStringUseCase.invoke(it.newBalance)
            val areAllFieldsValid = areAllFieldsValid(it.newBalance)
            if (isValidAmount) {
                CreateAccountPartialChange.OnBalanceUpdateChange.ValidBalanceAmount(it.newBalance, areAllFieldsValid)
            } else {
                CreateAccountPartialChange.OnBalanceUpdateChange.InvalidBalanceAmount(it.newBalance)
            }
        }
    }

    private fun handleOnAccountNameUpdateIntent(
        flow: Flow<CreateAccountIntent.OnAccountNameUpdate>
    ): Flow<CreateAccountPartialChange.OnAccountNameUpdateChange> {
        return flow.map {
            CreateAccountPartialChange.OnAccountNameUpdateChange(
                newAccountName = it.newAccountName,
                isContinueEnabled = areAllFieldsValid(accountName = it.newAccountName)
            )
        }
    }

    private fun handleOnAccountTypeUpdateIntent(
        flow: Flow<CreateAccountIntent.OnAccountTypeUpdate>
    ): Flow<CreateAccountPartialChange.OnAccountTypeUpdateChange> {
        return flow.map {
            CreateAccountPartialChange.OnAccountTypeUpdateChange(
                accountType = it.accountType,
                isContinueEnabled = areAllFieldsValid(accountType = it.accountType)
            )
        }
    }

    private fun handleOnContinueClickedIntent(
        flow: Flow<CreateAccountIntent.OnContinueClicked>
    ): Flow<CreateAccountPartialChange.OnContinueClickedChange> {
        return flow.map {
            with(state.value) {
                createAccountUseCase.invoke(
                    accountName = accountName,
                    accountType = accountType!!,
                    initialBalance = initialBalance.replace(RupeeSymbol, "").toLongOrNull() ?: 0
                )
            }
            CreateAccountPartialChange.OnContinueClickedChange
        }
    }

    private fun areAllFieldsValid(
        balance: String = state.value.initialBalance,
        accountName: String = state.value.accountName,
        accountType: AccountType? = state.value.accountType
    ): Boolean {
        return validateAmountStringUseCase.invoke(balance) &&
                accountName.isNotEmpty() &&
                accountType != null
    }
}