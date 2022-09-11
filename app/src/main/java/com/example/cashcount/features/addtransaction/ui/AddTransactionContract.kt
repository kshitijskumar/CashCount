package com.example.cashcount.features.addtransaction.ui

import com.example.cashcount.components.RupeeSymbol
import com.example.cashcount.features.addtransaction.data.model.TransactionCategory
import com.example.cashcount.features.addtransaction.data.model.TransactionType
import com.example.cashcount.features.addtransaction.data.model.appmodel.AccountUiModel
import com.example.cashcount.mvi.PartialChange

data class TransactionState(
    val amountString: String = "${RupeeSymbol}0",
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val transactionCategory: TransactionCategory? = null,
    val description: String = "",
    val accountUsed: AccountUiModel? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isAddBtnEnabled: Boolean = false,
    val transactionCategoryList: List<TransactionCategory> = TransactionCategory.values().toList(),
    val userAccountsList: List<AccountUiModel> = listOf()
)

sealed class AddTransactionIntent {
    object SetupUserAccountsList : AddTransactionIntent()
    data class OnAmountUpdate(val newAmount: String): AddTransactionIntent()
    data class OnTransactionTypeUpdate(val transactionType: TransactionType): AddTransactionIntent()
    data class OnTransactionCategoryUpdate(val transactionCategory: TransactionCategory): AddTransactionIntent()
    data class OnDescriptionUpdate(val description: String): AddTransactionIntent()
    data class OnAccountUsedUpdate(val accountUsed: AccountUiModel): AddTransactionIntent()
    object OnAddClicked : AddTransactionIntent()

}


sealed class AddTransactionSideEffect {
    object NavigateBack: AddTransactionSideEffect()
}

sealed class AddTransactionPartialChange : PartialChange<TransactionState> {

    data class UserAccountsListChange(val accounts: List<AccountUiModel>) : AddTransactionPartialChange() {
        override fun reduce(oldState: TransactionState): TransactionState {
            return oldState.copy(
                userAccountsList = accounts
            )
        }
    }

    data class OnAmounUpdateChange(val newAmount: String, val isAddBtnEnabled: Boolean) : AddTransactionPartialChange() {
        override fun reduce(oldState: TransactionState): TransactionState {
            return oldState.copy(
                amountString = newAmount,
                isAddBtnEnabled = isAddBtnEnabled
            )
        }
    }

    data class OnTransactionTypeUpdateChange(val transactionType: TransactionType, val isAddBtnEnabled: Boolean) : AddTransactionPartialChange() {
        override fun reduce(oldState: TransactionState): TransactionState {
            return oldState.copy(
                transactionType = transactionType,
                isAddBtnEnabled = isAddBtnEnabled
            )
        }
    }

    data class OnTransactionCategoryUpdateChange(val category: TransactionCategory, val isAddBtnEnabled: Boolean) : AddTransactionPartialChange() {
        override fun reduce(oldState: TransactionState): TransactionState {
            return oldState.copy(
                transactionCategory = category,
                isAddBtnEnabled = isAddBtnEnabled
            )
        }
    }

    data class OnDescriptionUpdateChange(val desc: String, val isAddBtnEnabled: Boolean) : AddTransactionPartialChange() {
        override fun reduce(oldState: TransactionState): TransactionState {
            return oldState.copy(
                description = desc,
                isAddBtnEnabled = isAddBtnEnabled
            )
        }
    }

    data class OnAccountUsedUpdateChange(val accountUsed: AccountUiModel, val isAddBtnEnabled: Boolean) : AddTransactionPartialChange() {
        override fun reduce(oldState: TransactionState): TransactionState {
            return oldState.copy(
                accountUsed = accountUsed,
                isAddBtnEnabled = isAddBtnEnabled
            )
        }
    }

    object OnAddClickedChange : AddTransactionPartialChange()
}