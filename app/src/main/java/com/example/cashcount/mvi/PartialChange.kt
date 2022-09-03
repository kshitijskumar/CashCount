package com.example.cashcount.mvi

interface PartialChange<STATE> {

    fun reduce(oldState: STATE): STATE {
        return oldState
    }

}