package com.example.cashcount.features.auth.ui.verification

import com.example.cashcount.features.auth.domain.CreateUserUseCase
import com.example.cashcount.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase
) : MviViewModel<VerificationState, VerificationIntent, VerificationSideEffect, VerificationPartialChange>()  {

    override fun initialState(): VerificationState {
        return VerificationState()
    }

    override fun Flow<VerificationIntent>.toPartialChange(): Flow<VerificationPartialChange> {
        return merge(
            handleOnCodeUpdatedIntent(filterIsInstance()),
            handleOnVerifyClickedIntent(filterIsInstance()),
            handleStopLoadingIntent(filterIsInstance()),
            handleCreateUserIntent(filterIsInstance())
        )
    }

    override fun sideEffect(change: VerificationPartialChange): List<VerificationSideEffect> {
        val effect: VerificationSideEffect? = when(change) {
            is VerificationPartialChange.OnCodeUpdatedChange -> null
            VerificationPartialChange.OnVerifyClickedChange -> {
                VerificationSideEffect.VerifyCodeEntered(state.value.codeEntered)
            }
            VerificationPartialChange.StopLoadingChange -> null
            is VerificationPartialChange.CreateUserChange -> {
                VerificationSideEffect.UserCreated
            }
        }

        return mutableListOf<VerificationSideEffect>().apply {
            effect?.let { add(it) }
        }
    }

    private fun handleOnCodeUpdatedIntent(
        flow: Flow<VerificationIntent.OnCodeUpdatedIntent>
    ): Flow<VerificationPartialChange.OnCodeUpdatedChange> {
        return flow.map {
            VerificationPartialChange.OnCodeUpdatedChange(it.newCode, it.newCode.length == 6)
        }
    }

    private fun handleOnVerifyClickedIntent(
        flow: Flow<VerificationIntent.OnVerifyClickedIntent>
    ): Flow<VerificationPartialChange.OnVerifyClickedChange> {
        return flow.map {
            VerificationPartialChange.OnVerifyClickedChange
        }
    }

    private fun handleStopLoadingIntent(
        flow: Flow<VerificationIntent.StopLoading>
    ): Flow<VerificationPartialChange.StopLoadingChange> {
        return flow.map {
            VerificationPartialChange.StopLoadingChange
        }
    }

    private fun handleCreateUserIntent(
        flow: Flow<VerificationIntent.CreateUserIntent>
    ): Flow<VerificationPartialChange.CreateUserChange> {
        return flow.map {
            createUserUseCase.invoke(it.user)
            VerificationPartialChange.CreateUserChange(it.user)
        }
    }
}