package com.example.cashcount.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashcount.R
import com.example.cashcount.ui.theme.Black_50
import com.example.cashcount.ui.theme.White_80

@Composable
fun CashCountToolbar(
    title: String,
    leftIconConfig: CashCountToolbarConfig? = null,
    rightIconConfig: CashCountToolbarConfig? = null,
    toolbarMode: CashCountToolbarMode = CashCountToolbarMode.DARK_ON_LIGHT
) {

    val tint = when(toolbarMode) {
        CashCountToolbarMode.LIGHT_ON_DARK -> White_80
        CashCountToolbarMode.DARK_ON_LIGHT -> Black_50
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(horizontal = 12.dp),
    ) {
        IconButton(onClick = { leftIconConfig?.onIconClicked?.invoke() }, enabled = leftIconConfig != null) {
            leftIconConfig?.let {
                Icon(
                    painter = painterResource(id = it.iconResId),
                    contentDescription = null,
                    tint = tint
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = tint,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center),
            )
        }
        IconButton(onClick = { rightIconConfig?.onIconClicked?.invoke() }, enabled = rightIconConfig != null) {
            rightIconConfig?.let {
                Icon(
                    painter = painterResource(id = it.iconResId),
                    contentDescription = null,
                    tint = tint
                )
            }
        }

    }

}

enum class CashCountToolbarMode {
    LIGHT_ON_DARK,
    DARK_ON_LIGHT
}

data class CashCountToolbarConfig(
    @DrawableRes val iconResId: Int,
    val onIconClicked: () -> Unit
)

@Preview(showSystemUi = true)
@Composable
fun CashCountToolbarPreview() {
    Column {
        CashCountToolbar(
            title = "Login",
            leftIconConfig = CashCountToolbarConfig(
                iconResId = R.drawable.ic_back,
                onIconClicked = {}
            )
        )
    }
}