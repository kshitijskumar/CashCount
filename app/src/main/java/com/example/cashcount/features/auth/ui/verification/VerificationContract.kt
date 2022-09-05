package com.example.cashcount.features.auth.ui.verification

import com.example.cashcount.datastore.user.models.UserDataStoreModel
import com.example.cashcount.mvi.PartialChange

data class VerificationState(
    val codeEntered: String = "",
    val isLoading: Boolean = false,
    val isVerifyBtnEnabled: Boolean = false
)

sealed class VerificationIntent {
    data class OnCodeUpdatedIntent(val newCode: String): VerificationIntent()
    object OnVerifyClickedIntent : VerificationIntent()
    object StopLoading : VerificationIntent()
    data class CreateUserIntent(val user: UserDataStoreModel): VerificationIntent()
}

sealed class VerificationSideEffect {
    data class VerifyCodeEntered(val codeEntered: String): VerificationSideEffect()
    object UserCreated : VerificationSideEffect()
}

sealed class VerificationPartialChange : PartialChange<VerificationState> {

    data class OnCodeUpdatedChange(val updatedCode: String, val isVerifyBtnEnabled: Boolean): VerificationPartialChange() {
        override fun reduce(oldState: VerificationState): VerificationState {
            return oldState.copy(codeEntered = updatedCode, isVerifyBtnEnabled = isVerifyBtnEnabled)
        }
    }

    object OnVerifyClickedChange : VerificationPartialChange() {
        override fun reduce(oldState: VerificationState): VerificationState {
            return oldState.copy(isLoading = true)
        }
    }

    object StopLoadingChange : VerificationPartialChange() {
        override fun reduce(oldState: VerificationState): VerificationState {
            return oldState.copy(isLoading = false)
        }
    }

    data class CreateUserChange(val user: UserDataStoreModel): VerificationPartialChange() {
        override fun reduce(oldState: VerificationState): VerificationState {
            return oldState.copy(isLoading = false)
        }
    }

}