package com.example.cashcount.features.auth.domain

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ValidatePhoneNumberUseCase @Inject constructor(){

    operator fun invoke(numberEntered: String): Boolean {
        val numberEnteredTrimmed = numberEntered.replace(" ", "")
        if (numberEnteredTrimmed.length < 10) {
            return false
        }
        numberEnteredTrimmed.forEach {
            if (!it.isDigit()) {
                return false
            }
        }

        return true
    }
}