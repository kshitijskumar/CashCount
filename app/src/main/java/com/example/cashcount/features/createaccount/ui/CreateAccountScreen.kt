package com.example.cashcount.features.createaccount.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cashcount.R
import com.example.cashcount.components.BalanceAndBottomSheetScreenTemplate
import com.example.cashcount.components.CashCountButton
import com.example.cashcount.components.CashCountDropdown
import com.example.cashcount.components.CashCountOutlinedTextField
import com.example.cashcount.ui.theme.Violet

@Composable
fun CreateAccountScreen(
    onBackPressed: () -> Unit
) {

    val scope = rememberCoroutineScope()

    BalanceAndBottomSheetScreenTemplate(
        screenColor = Violet,
        toolbarTitle = stringResource(id = R.string.add_new_account),
        amountLabel = stringResource(id = R.string.balance),
        onBackPressed = onBackPressed,
        onBalanceUpdate = {

        },
        bottomSheetContent = {
            val accountNameState = remember {
                mutableStateOf("")
            }

            CashCountOutlinedTextField(
                value = accountNameState.value,
                onValueChange = {
                    accountNameState.value = it
                },
                placeholderText = stringResource(id = R.string.account_name)
            )
            Spacer(modifier = Modifier.height(24.dp))

            CashCountDropdown(
                selectedAccountType = null
            )

            Spacer(modifier = Modifier.height(24.dp))
            CashCountButton(
                btnText = stringResource(id = R.string.continue_text),
                modifier = Modifier.fillMaxWidth()
            ) {

            }
        }
    )

}