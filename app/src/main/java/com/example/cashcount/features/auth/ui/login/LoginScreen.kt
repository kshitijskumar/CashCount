package com.example.cashcount.features.auth.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.cashcount.R
import com.example.cashcount.components.*
import com.example.cashcount.features.auth.handler.PhoneAuthFailureReason
import com.example.cashcount.features.auth.handler.PhoneAuthStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    vm: LoginViewModel = hiltViewModel(),
    phoneAuthStatus: Flow<PhoneAuthStatus?>,
    onBackPressed: () -> Unit,
    startPhoneLogin: (String) -> Unit,
    navigateToVerificationScreen: () -> Unit,
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = phoneAuthStatus) {
        phoneAuthStatus.collect {
            scope.launch {
                vm.processIntent(LoginIntent.StopLoading)
            }
            when(it) {
                is PhoneAuthStatus.CodeSent -> {
                    scope.launch {
                        vm.processIntent(LoginIntent.NavigateToVerificationScreen)
                    }
                }
                is PhoneAuthStatus.VerificationCompleted -> {
                    // not to handle here
                }
                is PhoneAuthStatus.VerificationFailed -> {
                    val toastMsg = when(it.reason) {
                        PhoneAuthFailureReason.INVALID_REQUEST -> "Invalid phone number"
                        PhoneAuthFailureReason.QUOTA_EXCEEDED -> "Something went wrong"
                        PhoneAuthFailureReason.UNKNOWN_ERROR -> "Something went wrong"
                        PhoneAuthFailureReason.INVALID_OTP -> "Invalid code entered"
                    }
                    Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    LaunchedEffect(key1 = vm) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            vm.effect
                .collect {
                    when(it) {
                        LoginSideEffect.NavigateToVerificationScreen -> {
                            navigateToVerificationScreen.invoke()
                        }
                        is LoginSideEffect.ShowToast -> {
                            Toast.makeText(context, it.msg, Toast.LENGTH_LONG).show()
                        }
                        is LoginSideEffect.StartPhoneLogin -> {
                            startPhoneLogin.invoke(it.numberEntered)
                        }
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
                .padding(horizontal = 18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
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

    LoadingDialog(shouldShow = state.value.isLoading)

}