package com.example.cashcount.features.auth.ui.verification

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.cashcount.R
import com.example.cashcount.components.*
import com.example.cashcount.features.auth.data.mappers.toUserDataStoreModels
import com.example.cashcount.features.auth.handler.PhoneAuthFailureReason
import com.example.cashcount.features.auth.handler.PhoneAuthStatus
import com.example.cashcount.ui.theme.Black_100
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

@Composable
fun VerificationScreen(
    vm: VerificationViewModel = hiltViewModel(),
    phoneAuthStatus: PhoneAuthStatus?,
    verifyCodeEntered: (String) -> Unit,
    onBackPressed: () -> Unit,
    navigateToMainScreen: () -> Unit
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val state = vm.state.collectAsState()

    LaunchedEffect(key1 = phoneAuthStatus) {
        if (phoneAuthStatus == null) return@LaunchedEffect

        vm.processIntent(VerificationIntent.StopLoading)
        when(phoneAuthStatus) {
            PhoneAuthStatus.CodeSent -> {
                // not to handle here
            }
            is PhoneAuthStatus.VerificationCompleted -> {
                vm.processIntent(VerificationIntent.CreateUserIntent(phoneAuthStatus.user.toUserDataStoreModels()))
            }
            is PhoneAuthStatus.VerificationFailed -> {
                val toastMsg = when(phoneAuthStatus.reason) {
                    PhoneAuthFailureReason.INVALID_REQUEST -> "Invalid phone number"
                    PhoneAuthFailureReason.QUOTA_EXCEEDED -> "Something went wrong"
                    PhoneAuthFailureReason.UNKNOWN_ERROR -> "Something went wrong"
                    PhoneAuthFailureReason.INVALID_OTP -> "Invalid code entered"
                }
                Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show()
            }
        }
    }

    LaunchedEffect(key1 = vm) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            vm.effect
                .collect {
                    when(it) {
                        is VerificationSideEffect.VerifyCodeEntered -> {
                            verifyCodeEntered.invoke(it.codeEntered)
                        }
                        VerificationSideEffect.UserCreated -> {
                            navigateToMainScreen.invoke()
                        }
                    }
                }
        }
    }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        CashCountToolbar(
            title = stringResource(id = R.string.verification),
            leftIconConfig = CashCountToolbarConfig(
                iconResId = R.drawable.ic_back,
                onIconClicked = onBackPressed
            )
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.enter_your_verification_code),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Black_100,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
            )
            Spacer(modifier = Modifier.height(52.dp))
            OtpField(
                onValueChange = {
                    scope.launch {
                        vm.processIntent(VerificationIntent.OnCodeUpdatedIntent(it))
                    }
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
        ) {
            CashCountButton(
                btnText = stringResource(id = R.string.verify),
                isEnabled = state.value.isVerifyBtnEnabled,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                scope.launch {
                    vm.processIntent(VerificationIntent.OnVerifyClickedIntent)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    LoadingDialog(shouldShow = state.value.isLoading)

}