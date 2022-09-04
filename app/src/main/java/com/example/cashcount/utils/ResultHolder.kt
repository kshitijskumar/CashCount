package com.example.cashcount.utils

sealed class ResultHolder<out SUCCESS, out ERROR> {
    data class Success<SUCCESS>(val data: SUCCESS): ResultHolder<SUCCESS, Nothing>()
    data class Error<ERROR>(val error: ERROR): ResultHolder<Nothing, ERROR>()
}
