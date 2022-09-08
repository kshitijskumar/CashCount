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

}

sealed class MainNavSideEffect {

    object NavigateToMainScreen : MainNavSideEffect()

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

}