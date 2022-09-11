package com.example.cashcount.features.addtransaction.ui

import com.example.cashcount.components.RupeeSymbol
import com.example.cashcount.features.addtransaction.data.mappers.toAccountUiModel
import com.example.cashcount.features.addtransaction.data.model.TransactionCategory
import com.example.cashcount.features.addtransaction.data.model.TransactionType
import com.example.cashcount.features.addtransaction.data.model.appmodel.AccountUiModel
import com.example.cashcount.features.addtransaction.domain.AppOrUpdateTransactionUseCase
import com.example.cashcount.features.createaccount.domain.GetAllAccountsOfUserUseCase
import com.example.cashcount.features.createaccount.domain.ValidateAmountStringUseCase
import com.example.cashcount.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val validateAmountStringUseCase: ValidateAmountStringUseCase,
    private val addOrUpdateTransactionUseCase: AppOrUpdateTransactionUseCase,
    private val getAllAccountsOfUserUseCase: GetAllAccountsOfUserUseCase
) : MviViewModel<TransactionState, AddTransactionIntent, AddTransactionSideEffect, AddTransactionPartialChange>() {

    override fun initialState(): TransactionState {
        return TransactionState()
    }

    override fun Flow<AddTransactionIntent>.toPartialChange(): Flow<AddTransactionPartialChange> {
        return merge(
            handleSetupUserAccountsListIntent(filterIsInstance()),
            handleAmountUpdateIntent(filterIsInstance()),
            handleTransactionTypeUpdateIntent(filterIsInstance()),
            handleTransactionCategoryUpdateIntent(filterIsInstance()),
            handleDescriptionUpdateIntent(filterIsInstance()),
            handleAccountUsedUpdateIntent(filterIsInstance()),
            handleOnAddClickedIntent(filterIsInstance()),
        )
    }

    override fun sideEffect(change: AddTransactionPartialChange): List<AddTransactionSideEffect> {
        val effect: AddTransactionSideEffect? = when(change) {
            is AddTransactionPartialChange.OnAccountUsedUpdateChange -> null
            AddTransactionPartialChange.OnAddClickedChange -> AddTransactionSideEffect.NavigateBack
            is AddTransactionPartialChange.OnAmounUpdateChange -> null
            is AddTransactionPartialChange.OnDescriptionUpdateChange -> null
            is AddTransactionPartialChange.OnTransactionCategoryUpdateChange -> null
            is AddTransactionPartialChange.OnTransactionTypeUpdateChange -> null
            is AddTransactionPartialChange.UserAccountsListChange -> null
        }

        return mutableListOf<AddTransactionSideEffect>().apply {
            effect?.let { add(it) }
        }
    }

    private fun handleSetupUserAccountsListIntent(
        flow: Flow<AddTransactionIntent.SetupUserAccountsList>
    ): Flow<AddTransactionPartialChange.UserAccountsListChange> {
        return flow.flatMapLatest {
            getAllAccountsOfUserUseCase.invoke()
                .map {
                    val accountsUiModels = it.map { it.toAccountUiModel() }
                    AddTransactionPartialChange.UserAccountsListChange(accountsUiModels)
                }
        }
    }

    private fun handleAmountUpdateIntent(
        flow: Flow<AddTransactionIntent.OnAmountUpdate>
    ): Flow<AddTransactionPartialChange.OnAmounUpdateChange> {
        return flow.map {
            AddTransactionPartialChange.OnAmounUpdateChange(
                newAmount = it.newAmount,
                isAddBtnEnabled = areAllFieldsValid(amountString = it.newAmount)
            )
        }
    }

    private fun handleTransactionTypeUpdateIntent(
        flow: Flow<AddTransactionIntent.OnTransactionTypeUpdate>
    ): Flow<AddTransactionPartialChange.OnTransactionTypeUpdateChange> {
        return flow.map {
            AddTransactionPartialChange.OnTransactionTypeUpdateChange(
                transactionType = it.transactionType,
                isAddBtnEnabled = areAllFieldsValid(transactionType = it.transactionType)
            )
        }
    }

    private fun handleTransactionCategoryUpdateIntent(
        flow: Flow<AddTransactionIntent.OnTransactionCategoryUpdate>
    ): Flow<AddTransactionPartialChange.OnTransactionCategoryUpdateChange> {
        return flow.map {
            AddTransactionPartialChange.OnTransactionCategoryUpdateChange(
                category = it.transactionCategory,
                isAddBtnEnabled = areAllFieldsValid(transactionCategory = it.transactionCategory)
            )
        }
    }

    private fun handleDescriptionUpdateIntent(
        flow: Flow<AddTransactionIntent.OnDescriptionUpdate>
    ): Flow<AddTransactionPartialChange.OnDescriptionUpdateChange> {
        return flow.map {
            AddTransactionPartialChange.OnDescriptionUpdateChange(
                desc = it.description,
                isAddBtnEnabled = areAllFieldsValid()
            )
        }
    }

    private fun handleAccountUsedUpdateIntent(
        flow: Flow<AddTransactionIntent.OnAccountUsedUpdate>
    ): Flow<AddTransactionPartialChange.OnAccountUsedUpdateChange> {
        return flow.map {
            AddTransactionPartialChange.OnAccountUsedUpdateChange(
                accountUsed = it.accountUsed,
                isAddBtnEnabled = areAllFieldsValid(accountUsed = it.accountUsed)
            )
        }
    }

    private fun handleOnAddClickedIntent(
        flow: Flow<AddTransactionIntent.OnAddClicked>
    ): Flow<AddTransactionPartialChange.OnAddClickedChange> {
        return flow.map {
            with(state.value) {
                addOrUpdateTransactionUseCase.invoke(
                    amount = amountString.replace(RupeeSymbol, "").toLongOrNull() ?: 0,
                    transactionType = transactionType,
                    transactionCategory = transactionCategory!!,
                    description = description,
                    accountId = accountUsed?.accountId!!,
                    timestamp = timestamp
                )
            }
            AddTransactionPartialChange.OnAddClickedChange
        }
    }

    private fun areAllFieldsValid(
        amountString: String = state.value.amountString,
        transactionType: TransactionType? = state.value.transactionType,
        transactionCategory: TransactionCategory? = state.value.transactionCategory,
        accountUsed: AccountUiModel? = state.value.accountUsed
    ): Boolean {
        return validateAmountStringUseCase.invoke(amountString) &&
                transactionType != null &&
                transactionCategory != null &&
                accountUsed != null
    }
}