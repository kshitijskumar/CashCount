package com.example.cashcount.features.auth.ui.navigation

import com.example.cashcount.mvi.PartialChange

data class AuthNavState(
    val isUserAuthenticated: Boolean = false,
    val setupNavigation: Boolean = false
)

sealed class AuthNavIntent {
    object CheckIsUserLoggedIn : AuthNavIntent()
    object NavigateToLoginScreen : AuthNavIntent()
    object NavigateToSetupAccountScreen : AuthNavIntent()
    object NavigateToMainScreen : AuthNavIntent()
    object NavigateToVerificationScreen : AuthNavIntent()
}

sealed class AuthNavSideEffect {
    object NavigateToMainScreen : AuthNavSideEffect()
    object NavigateToLoginScreen : AuthNavSideEffect()
    object NavigateToSetupAccountScreen : AuthNavSideEffect()
    object NavigateToVerificationScreen : AuthNavSideEffect()
}


sealed class AuthNavPartialChange : PartialChange<AuthNavState> {
    data class CheckIsUserLoggedInChange(val isUserLoggedIn: Boolean) : AuthNavPartialChange() {
        override fun reduce(oldState: AuthNavState): AuthNavState {
            return oldState.copy(
                isUserAuthenticated = isUserLoggedIn,
                setupNavigation = !isUserLoggedIn
            )
        }
    }
    object NavigateToLoginScreen : AuthNavPartialChange()
    object NavigateToSetupAccountScreen : AuthNavPartialChange()
    object NavigateToVerificationScreen : AuthNavPartialChange()
    object NavigateToMainScreen : AuthNavPartialChange()

}