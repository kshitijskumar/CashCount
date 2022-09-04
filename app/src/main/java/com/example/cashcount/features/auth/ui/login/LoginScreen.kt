package com.example.cashcount.features.auth.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cashcount.R
import com.example.cashcount.components.CashCountButton
import com.example.cashcount.components.CashCountOutlinedTextField
import com.example.cashcount.components.CashCountToolbar
import com.example.cashcount.components.CashCountToolbarConfig
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    vm: LoginViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = vm) {
        vm.effect
            .collect {
                when(it) {
                    LoginSideEffect.NavigateToVerificationScreen -> {

                    }
                    is LoginSideEffect.ShowToast -> {
                        Toast.makeText(context, it.msg, Toast.LENGTH_LONG).show()
                    }
                    is LoginSideEffect.StartPhoneLogin -> {

                    }
                }
            }
    }

    val state = vm.state.collectAsState()
    val phoneNumberEntered = remember {
        mutableStateOf(state.value.phoneNumberEntered)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {
        CashCountToolbar(
            title = stringResource(id = R.string.login),
            leftIconConfig = CashCountToolbarConfig(
                iconResId = R.drawable.ic_back,
                onIconClicked = onBackPressed
            )
        )

        Spacer(modifier = Modifier.height(56.dp))

        CashCountOutlinedTextField(
            value = phoneNumberEntered.value,
            onValueChange = {
                if (it.length <= 10) {
                    phoneNumberEntered.value = it
                    scope.launch {
                        vm.processIntent(LoginIntent.OnPhoneNumberUpdatedIntent(it))
                    }
                }
            },
            placeholderText = stringResource(id = R.string.phone_number),
            modifier = Modifier
                .padding(horizontal = 18.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        CashCountButton(
            btnText = stringResource(id = R.string.continue_text),
            isEnabled = state.value.isContinueButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
        ) {
            scope.launch {
                vm.processIntent(LoginIntent.OnContinueClicked)
            }
        }

    }


}