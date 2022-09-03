package com.example.cashcount.features.auth.ui.navigation

import android.util.Log
import com.example.cashcount.features.auth.domain.CheckIsUserLoggedInUseCase
import com.example.cashcount.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

@HiltViewModel
class AuthNavigationViewModel @Inject constructor(
    private val checkIsUserLoggedInUseCase: CheckIsUserLoggedInUseCase
) : MviViewModel<AuthNavState, AuthNavIntent, AuthNavSideEffect, AuthNavPartialChange>() {

    override fun initialState(): AuthNavState {
        return AuthNavState()
    }

    override fun Flow<AuthNavIntent>.toPartialChange(): Flow<AuthNavPartialChange> {
        Log.d("MviViewModelStuff", "in merge")
        return merge(
            handleCheckIsUserLoggedInIntent(filterIsInstance()),
            handleNavigateToLoginScreen(filterIsInstance()),
            handleNavigateToSetupAccountScreen(filterIsInstance()),
            handleNavigateToMainScreen(filterIsInstance()),
            handleNavigateToVerificationScreen(filterIsInstance()),
        )
    }

    override fun sideEffect(change: AuthNavPartialChange): List<AuthNavSideEffect> {
        val effect: AuthNavSideEffect? = when(change) {
            is AuthNavPartialChange.CheckIsUserLoggedInChange -> {
                if (change.isUserLoggedIn) {
                    AuthNavSideEffect.NavigateToMainScreen
                } else {
                    null
                }
            }
            AuthNavPartialChange.NavigateToLoginScreen -> {
                AuthNavSideEffect.NavigateToLoginScreen
            }
            AuthNavPartialChange.NavigateToSetupAccountScreen -> {
                AuthNavSideEffect.NavigateToSetupAccountScreen
            }
            AuthNavPartialChange.NavigateToVerificationScreen -> {
                AuthNavSideEffect.NavigateToVerificationScreen
            }
            AuthNavPartialChange.NavigateToMainScreen -> {
                AuthNavSideEffect.NavigateToMainScreen
            }
        }

        return mutableListOf<AuthNavSideEffect>().apply {
            effect?.let { add(it) }
        }
    }

    private fun handleCheckIsUserLoggedInIntent(
        flow: Flow<AuthNavIntent.CheckIsUserLoggedIn>
    ): Flow<AuthNavPartialChange.CheckIsUserLoggedInChange> {
        return flow.map {
            val isUserLoggedIn = checkIsUserLoggedInUseCase.invoke()
            AuthNavPartialChange.CheckIsUserLoggedInChange(isUserLoggedIn)
        }
    }

    private fun handleNavigateToLoginScreen(
        flow: Flow<AuthNavIntent.NavigateToLoginScreen>
    ): Flow<AuthNavPartialChange.NavigateToLoginScreen> {
        return flow.map {
            AuthNavPartialChange.NavigateToLoginScreen
        }
    }

    private fun handleNavigateToSetupAccountScreen(
        flow: Flow<AuthNavIntent.NavigateToSetupAccountScreen>
    ): Flow<AuthNavPartialChange.NavigateToSetupAccountScreen> {
        return flow.map {
            AuthNavPartialChange.NavigateToSetupAccountScreen
        }
    }

    private fun handleNavigateToMainScreen(
        flow: Flow<AuthNavIntent.NavigateToMainScreen>
    ): Flow<AuthNavPartialChange.NavigateToMainScreen> {
        return flow.map {
            AuthNavPartialChange.NavigateToMainScreen
        }
    }

    private fun handleNavigateToVerificationScreen(
        flow: Flow<AuthNavIntent.NavigateToVerificationScreen>
    ): Flow<AuthNavPartialChange.NavigateToVerificationScreen> {
        return flow.map {
            AuthNavPartialChange.NavigateToVerificationScreen
        }
    }

}