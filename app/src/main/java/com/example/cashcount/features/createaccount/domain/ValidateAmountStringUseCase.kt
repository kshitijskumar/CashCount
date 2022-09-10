package com.example.cashcount.features.createaccount.domain

import com.example.cashcount.components.RupeeSymbol
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ValidateAmountStringUseCase @Inject constructor()  {

    operator fun invoke(balanceString: String): Boolean {
        balanceString.forEach {
            if (!it.isDigit() && it.toString() != RupeeSymbol) {
                return false
            }
        }
        return true
    }

}