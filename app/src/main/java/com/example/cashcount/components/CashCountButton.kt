package com.example.cashcount.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashcount.ui.theme.Violet
import com.example.cashcount.ui.theme.Violet_20
import com.example.cashcount.ui.theme.White_80

@Composable
fun CashCountButton(
    modifier: Modifier = Modifier,
    mode: CashCountButtonMode = CashCountButtonMode.LightOnDark,
    btnText: String,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    val buttonColors = mode.getButtonColors()

    Button(
        onClick = onClick,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp)),
        colors = ButtonDefaults
            .buttonColors(
                backgroundColor = buttonColors.backgroundColor,
                contentColor = Color.Transparent
            ),
        enabled = isEnabled
    ) {
        Text(
            text = btnText,
            color = buttonColors.textColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(vertical = 10.dp)
        )
    }

}

@Preview(showSystemUi = true)
@Composable
fun CashCountButtonPreview() {
    Column {
        CashCountButton(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp),
            btnText = "Login",
            mode = CashCountButtonMode.LightOnDark
        ) {

        }

        CashCountButton(
            modifier = Modifier.fillMaxWidth(),
            btnText = "Login",
            mode = CashCountButtonMode.DarkOnLight
        ) {

        }
    }
}


enum class CashCountButtonMode {
    LightOnDark,
    DarkOnLight
}

fun CashCountButtonMode.getButtonColors(): CashCountButtonColors {
    return when (this) {
        CashCountButtonMode.LightOnDark -> {
            CashCountButtonColors(
                textColor = White_80,
                backgroundColor = Violet
            )
        }
        CashCountButtonMode.DarkOnLight -> {
            CashCountButtonColors(
                textColor = Violet,
                backgroundColor = Violet_20
            )
        }
    }
}

data class CashCountButtonColors(
    val textColor: Color,
    val backgroundColor: Color
)