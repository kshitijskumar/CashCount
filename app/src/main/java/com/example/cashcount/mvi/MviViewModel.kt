package com.example.cashcount.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

@Suppress("LeakingThis")
abstract class MviViewModel<STATE, INTENT, EFFECT, CHANGE: PartialChange<STATE>> : ViewModel() {

    private val intentFlow = MutableSharedFlow<INTENT>()

    private val _effectChannel = Channel<EFFECT>(Channel.BUFFERED)
    val effect: Flow<EFFECT> = _effectChannel.receiveAsFlow()

    val state: StateFlow<STATE>

    abstract fun initialState(): STATE
    abstract fun Flow<INTENT>.toPartialChange(): Flow<CHANGE>
    abstract fun sideEffect(change: CHANGE): List<EFFECT>

     suspend fun processIntent(intent: INTENT) {
        Log.d("MviViewModelStuff", "process: $intent")
        intentFlow.emit(intent)
    }

    init {
        Log.d("MviViewModelStuff", "init")
        val initialState = initialState()
        state = intentFlow
            .onEach { Log.d("MviViewModelStuff", "intent: $it") }
            .toPartialChange()
            .onEach { Log.d("MviViewModelStuff", "change: $it") }
            .toSideEffect()
            .scan(initialState) { oldState, change -> change.reduce(oldState) }
            .onEach { Log.d("MviViewModelStuff", "state: $it") }
            .catch { Log.d("MviViewModelStuff", "catch: $it") }
            .stateIn(viewModelScope, SharingStarted.Eagerly, initialState)
    }

    private fun Flow<CHANGE>.toSideEffect(): Flow<CHANGE> {
        return this.onEach {
            val sideEffects = sideEffect(it)
            sideEffects.forEach { _effectChannel.trySend(it) }
        }
    }

}