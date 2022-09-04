package com.example.cashcount.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cashcount.ui.theme.Black_20
import com.example.cashcount.ui.theme.Violet

@Composable
fun CashCountOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        placeholder = {
            placeholderText?.let {
                Text(
                    text = it,
                    color = Black_20
                )
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            unfocusedBorderColor = Black_20,
            unfocusedLabelColor = Black_20,
            focusedBorderColor = Violet,
            focusedLabelColor = Violet
        )
    )
}

@Preview(showSystemUi = true)
@Composable
fun CashCountOutlinedTextFieldPreview() {
    Column(
        modifier = Modifier.padding(vertical = 10.dp)
    ) {

        val text = remember {
            mutableStateOf("")
        }

        CashCountOutlinedTextField(
            value = text.value,
            onValueChange = {
                if (it.length <= 10) {
                    text.value = it
                }
            },
            placeholderText = "Phone number"
        )
    }
}