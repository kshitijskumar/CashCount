package com.example.cashcount.features.main

import com.example.cashcount.mvi.PartialChange

data class MainNavState(
    val shouldInflateNavigation: Boolean = false,
    val startDestinationsType: MainScreenStartDestinationsType = MainScreenStartDestinationsType.SETUP_ACCOUNT_SCREEN
)

enum class MainScreenStartDestinationsType {
    SETUP_ACCOUNT_SCREEN,
    DASHBOARD_SCREEN
}

sealed class MainNavIntent {

    object InitializationIntent : MainNavIntent()
    object CreateAccountIntent : MainNavIntent()
    object SuccessScreenIntent : MainNavIntent()
    object DashboardScreenIntent : MainNavIntent()

}

sealed class MainNavSideEffect {

    object NavigateToMainScreen : MainNavSideEffect()
    object NavigateToCreateAccountScreen : MainNavSideEffect()
    object NavigateToSuccessScreen : MainNavSideEffect()
    object NavigateToDashboardScreen : MainNavSideEffect()

}

sealed class MainNavPartialChange : PartialChange<MainNavState> {

    sealed class InitializationChange : MainNavPartialChange() {

        override fun reduce(oldState: MainNavState): MainNavState {
            return when(this) {
                AtleastOneAccountExist -> {
                    oldState.copy(
                        shouldInflateNavigation = true,
                        startDestinationsType = MainScreenStartDestinationsType.DASHBOARD_SCREEN
                    )
                }
                NoAccountsExist -> {
                    oldState.copy(
                        shouldInflateNavigation = true,
                        startDestinationsType = MainScreenStartDestinationsType.SETUP_ACCOUNT_SCREEN
                    )
                }
            }
        }

        object NoAccountsExist : InitializationChange()
        object AtleastOneAccountExist : InitializationChange()

    }

    object CreateAccountChange : MainNavPartialChange()
    object SuccessScreenChange : MainNavPartialChange()
    object DashboardScreenChange : MainNavPartialChange()


}