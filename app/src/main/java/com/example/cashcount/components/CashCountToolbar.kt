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

@Composable
fun CashCountToolbar(
    title: String,
    leftIconConfig: CashCountToolbarConfig? = null,
    rightIconConfig: CashCountToolbarConfig? = null
) {

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
                    contentDescription = null
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
                color = Black_50,
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
                    contentDescription = null
                )
            }
        }

    }

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