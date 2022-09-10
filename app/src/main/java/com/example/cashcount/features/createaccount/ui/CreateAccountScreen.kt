package com.example.cashcount.features.createaccount.ui

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
import com.example.cashcount.components.CashCountDropdown
import com.example.cashcount.components.CashCountOutlinedTextField
import com.example.cashcount.features.createaccount.CreateAccountIntent
import com.example.cashcount.features.createaccount.CreateAccountSideEffect
import com.example.cashcount.ui.theme.Violet
import kotlinx.coroutines.launch

@Composable
fun CreateAccountScreen(
    vm: CreateAccountViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onAccountCreated: () -> Unit
) {

    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    val state = vm.state.collectAsState()

    LaunchedEffect(key1 = vm) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            vm.effect
                .collect {
                    when(it) {
                        CreateAccountSideEffect.AccountCreatedEffect -> onAccountCreated.invoke()
                    }
                }
        }
    }

    BalanceAndBottomSheetScreenTemplate(
        screenColor = Violet,
        toolbarTitle = stringResource(id = R.string.add_new_account),
        amountLabel = stringResource(id = R.string.balance),
        onBackPressed = onBackPressed,
        onBalanceUpdate = {
            scope.launch {
                vm.processIntent(CreateAccountIntent.OnBalanceUpdate(it))
            }
        },
        bottomSheetContent = {
            val accountNameState = remember {
                mutableStateOf("")
            }

            CashCountOutlinedTextField(
                value = accountNameState.value,
                onValueChange = {
                    accountNameState.value = it
                    scope.launch {
                        vm.processIntent(CreateAccountIntent.OnAccountNameUpdate(it))
                    }
                },
                placeholderText = stringResource(id = R.string.account_name)
            )
            Spacer(modifier = Modifier.height(24.dp))

            CashCountDropdown(
                selectedAccountType = null
            ) {
                scope.launch {
                    vm.processIntent(CreateAccountIntent.OnAccountTypeUpdate(it))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            CashCountButton(
                btnText = stringResource(id = R.string.continue_text),
                modifier = Modifier.fillMaxWidth(),
                isEnabled = state.value.isContinueEnabled
            ) {
                scope.launch {
                    vm.processIntent(CreateAccountIntent.OnContinueClicked)
                }
            }
        }
    )

}