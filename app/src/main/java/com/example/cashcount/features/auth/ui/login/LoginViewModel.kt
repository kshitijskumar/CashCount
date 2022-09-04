package com.example.cashcount.features.auth.ui.login

import com.example.cashcount.features.auth.domain.ValidatePhoneNumberUseCase
import com.example.cashcount.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase
) : MviViewModel<LoginViewState, LoginIntent, LoginSideEffect, LoginPartialChange>() {

    override fun initialState(): LoginViewState {
        return LoginViewState()
    }

    override fun Flow<LoginIntent>.toPartialChange(): Flow<LoginPartialChange> {
        return merge(
            handleOnPhoneNumberUpdatedIntent(filterIsInstance()),
            handleOnContinueClicked(filterIsInstance())
        )
    }

    override fun sideEffect(change: LoginPartialChange): List<LoginSideEffect> {
        val effect: LoginSideEffect? = when(change) {
            is LoginPartialChange.PhoneNumberUpdateChange.InvalidNumberEntered -> null
            is LoginPartialChange.PhoneNumberUpdateChange.ValidNumberEntered -> null
            is LoginPartialChange.ContinueClickedChange -> LoginSideEffect.StartPhoneLogin(change.numberEntered)
        }

        return mutableListOf<LoginSideEffect>().apply {
            effect?.let { add(it) }
        }
    }

    private fun handleOnPhoneNumberUpdatedIntent(
        flow: Flow<LoginIntent.OnPhoneNumberUpdatedIntent>
    ): Flow<LoginPartialChange.PhoneNumberUpdateChange> {
        return flow.map {
            val isValid = validatePhoneNumberUseCase.invoke(it.numberEntered)
            if (isValid) {
                LoginPartialChange.PhoneNumberUpdateChange.ValidNumberEntered(it.numberEntered)
            } else {
                LoginPartialChange.PhoneNumberUpdateChange.InvalidNumberEntered(it.numberEntered)
            }
        }
    }

    private fun handleOnContinueClicked(
        flow: Flow<LoginIntent.OnContinueClicked>
    ): Flow<LoginPartialChange.ContinueClickedChange> {
        return flow.map {
            LoginPartialChange.ContinueClickedChange("$+91${state.value.phoneNumberEntered.replace(" ", "")}")
        }
    }
}