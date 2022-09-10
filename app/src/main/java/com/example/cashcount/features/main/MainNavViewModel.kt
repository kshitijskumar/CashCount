package com.example.cashcount.features.main

import android.util.Log
import com.example.cashcount.features.createaccount.domain.GetAllAccountsOfUserUseCase
import com.example.cashcount.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainNavViewModel @Inject constructor(
    private val getAllAccountsOfUserUseCase: GetAllAccountsOfUserUseCase
) : MviViewModel<MainNavState, MainNavIntent, MainNavSideEffect, MainNavPartialChange>() {

    override fun initialState(): MainNavState {
        return MainNavState()
    }

    override fun Flow<MainNavIntent>.toPartialChange(): Flow<MainNavPartialChange> {
        return merge(
            handleInitializationIntent(filterIsInstance()),
            handleCreateAccountIntent(filterIsInstance()),
            handleSuccessScreenIntent(filterIsInstance()),
            handleDashboardScreenIntent(filterIsInstance()),
        )
    }

    override fun sideEffect(change: MainNavPartialChange): List<MainNavSideEffect> {
        val effect: MainNavSideEffect? = when(change) {
            MainNavPartialChange.InitializationChange.AtleastOneAccountExist -> null
            MainNavPartialChange.InitializationChange.NoAccountsExist -> null
            MainNavPartialChange.CreateAccountChange -> MainNavSideEffect.NavigateToCreateAccountScreen
            MainNavPartialChange.DashboardScreenChange -> MainNavSideEffect.NavigateToDashboardScreen
            MainNavPartialChange.SuccessScreenChange -> MainNavSideEffect.NavigateToSuccessScreen
        }

        return mutableListOf<MainNavSideEffect>().apply {
            effect?.let { add(it) }
        }
    }

    private fun handleInitializationIntent(
        flow: Flow<MainNavIntent.InitializationIntent>
    ): Flow<MainNavPartialChange.InitializationChange> {
        return flow.map {
            if (getAllAccountsOfUserUseCase.invoke().firstOrNull()?.isNullOrEmpty() == true) {
                MainNavPartialChange.InitializationChange.NoAccountsExist
            } else {
                MainNavPartialChange.InitializationChange.AtleastOneAccountExist
            }
        }
    }

    private fun handleCreateAccountIntent(
        flow: Flow<MainNavIntent.CreateAccountIntent>
    ): Flow<MainNavPartialChange.CreateAccountChange> {
        return flow.map {
            MainNavPartialChange.CreateAccountChange
        }
    }

    private fun handleSuccessScreenIntent(
        flow: Flow<MainNavIntent.SuccessScreenIntent>
    ): Flow<MainNavPartialChange.SuccessScreenChange> {
        return flow.map {
            MainNavPartialChange.SuccessScreenChange
        }
    }

    private fun handleDashboardScreenIntent(
        flow: Flow<MainNavIntent.DashboardScreenIntent>
    ): Flow<MainNavPartialChange.DashboardScreenChange> {
        return flow.map {
            MainNavPartialChange.DashboardScreenChange
        }
    }

}