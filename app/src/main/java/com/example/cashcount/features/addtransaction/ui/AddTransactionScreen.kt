package com.example.cashcount.features.addtransaction.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.cashcount.R
import com.example.cashcount.components.BalanceAndBottomSheetScreenTemplate
import com.example.cashcount.components.CashCountButton
import com.example.cashcount.components.CashCountOutlinedTextField
import com.example.cashcount.components.GenericDropDown
import com.example.cashcount.features.addtransaction.data.model.TransactionType
import com.example.cashcount.ui.theme.Green
import com.example.cashcount.ui.theme.Red
import kotlinx.coroutines.launch

@Composable
fun AddTransactionScreen(
    vm: AddTransactionViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onNavigateBack: () -> Unit
) {

    val scope = rememberCoroutineScope()
    val state = vm.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    val transactionType = remember {
        mutableStateOf(state.value.transactionType)
    }

    val backgroundColor = remember(transactionType.value) {
        when(transactionType.value) {
            TransactionType.EXPENSE -> Red
            TransactionType.INCOME -> Green
        }
    }

    val titleText = when(transactionType.value) {
        TransactionType.EXPENSE -> stringResource(id = R.string.expense)
        TransactionType.INCOME -> stringResource(id = R.string.income)
    }

    LaunchedEffect(key1 = vm) {

        vm.processIntent(AddTransactionIntent.SetupUserAccountsList)

        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            vm.effect
                .collect {
                    when(it) {
                        AddTransactionSideEffect.NavigateBack -> onNavigateBack.invoke()
                    }
                }
        }
    }

    BalanceAndBottomSheetScreenTemplate(
        screenColor = backgroundColor,
        toolbarTitle = titleText,
        amountLabel = stringResource(id = R.string.how_much),
        onBackPressed = onBackPressed,
        onBalanceUpdate = {
            scope.launch {
                vm.processIntent(AddTransactionIntent.OnAmountUpdate(it))
            }
        }
    ) {

        val description = remember {
            mutableStateOf("")
        }

        val selectedCategory = remember {
            mutableStateOf(state.value.transactionCategory)
        }

        val selectedAccount = remember {
            mutableStateOf(state.value.accountUsed)
        }

        GenericDropDown(
            startingItem = transactionType.value,
            itemsList = TransactionType.values().toList(),
            onItemSelected = {
                transactionType.value = it
                scope.launch {
                    vm.processIntent(AddTransactionIntent.OnTransactionTypeUpdate(it))
                }
            },
            placeholder = stringResource(id = R.string.category)
        )
        Spacer(modifier = Modifier.height(24.dp))

        GenericDropDown(
            itemsList = state.value.transactionCategoryList,
            onItemSelected = {
                selectedCategory.value = it
                scope.launch {
                    vm.processIntent(AddTransactionIntent.OnTransactionCategoryUpdate(it))
                }
            },
            placeholder = stringResource(id = R.string.spent_on)
        )

        Spacer(modifier = Modifier.height(24.dp))
        CashCountOutlinedTextField(
            value = description.value,
            onValueChange = {
                description.value = it
                scope.launch {
                    vm.processIntent(AddTransactionIntent.OnDescriptionUpdate(it))
                }
            },
            placeholderText = stringResource(id = R.string.description),
        )
        Spacer(modifier = Modifier.height(24.dp))

        GenericDropDown(
            itemsList = state.value.userAccountsList,
            onItemSelected = {
                selectedAccount.value = it
                scope.launch {
                    vm.processIntent(AddTransactionIntent.OnAccountUsedUpdate(it))
                }
            },
            placeholder = stringResource(id = R.string.account_used)
        )

        Spacer(modifier = Modifier.height(24.dp))

        CashCountButton(
            btnText = stringResource(id = R.string.add),
            modifier = Modifier.fillMaxWidth(),
            isEnabled = state.value.isAddBtnEnabled
        ) {
            scope.launch {
                vm.processIntent(AddTransactionIntent.OnAddClicked)
            }
        }

    }

}