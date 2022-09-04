package com.example.cashcount.features.auth.ui.login

import com.example.cashcount.mvi.PartialChange

data class LoginViewState(
    val phoneNumberEntered: String = "",
    val isLoading: Boolean = false,
    val isContinueButtonEnabled: Boolean = false
)

sealed class LoginIntent {
    data class OnPhoneNumberUpdatedIntent(val numberEntered: String): LoginIntent()
    object OnContinueClicked : LoginIntent()
}

sealed class LoginSideEffect {
    object NavigateToVerificationScreen : LoginSideEffect()
    data class ShowToast(val msg: String): LoginSideEffect()
    data class StartPhoneLogin(val numberEntered: String): LoginSideEffect()
}

sealed class LoginPartialChange : PartialChange<LoginViewState> {

    sealed class PhoneNumberUpdateChange : LoginPartialChange() {
        override fun reduce(oldState: LoginViewState): LoginViewState {
            return when(this) {
                is InvalidNumberEntered -> oldState.copy(phoneNumberEntered = numberEntered, isContinueButtonEnabled = false)
                is ValidNumberEntered -> oldState.copy(phoneNumberEntered = numberEntered, isContinueButtonEnabled = true)
            }
        }

        data class ValidNumberEntered(val numberEntered: String): PhoneNumberUpdateChange()
        data class InvalidNumberEntered(val numberEntered: String): PhoneNumberUpdateChange()
    }

    data class ContinueClickedChange(val numberEntered: String) : LoginPartialChange()
}