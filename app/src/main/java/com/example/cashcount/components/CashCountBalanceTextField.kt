package com.example.cashcount.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.example.cashcount.ui.theme.White_80

@Composable
fun CashCountBalanceTextField(
    initialBalanceToShow: String = "${RupeeSymbol}0",
    onBalanceUpdated: (String) -> Unit
) {

    val balanceState = remember {
        mutableStateOf(initialBalanceToShow)
    }

    TextField(
        value = TextFieldValue(text = balanceState.value, selection = TextRange(balanceState.value.length)),
        onValueChange = {
            if (it.text.length == 0) {
                balanceState.value = "${RupeeSymbol}0"
                onBalanceUpdated.invoke("${RupeeSymbol}0")
            } else {
                balanceState.value = it.text
                onBalanceUpdated.invoke(it.text)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.textFieldColors(
            textColor = White_80,
            backgroundColor = Color.Transparent,
            cursorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle.Default.copy(fontSize = BalanceFontSize, fontWeight = FontWeight.Bold)
    )
}