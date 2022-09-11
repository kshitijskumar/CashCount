package com.example.cashcount.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashcount.R
import com.example.cashcount.ui.theme.White_80_64

@Composable
fun BalanceAndBottomSheetScreenTemplate(
    screenColor: Color,
    toolbarTitle: String,
    amountLabel: String,
    onBackPressed: () -> Unit,
    onBalanceUpdate: (String) -> Unit,
    bottomSheetContent: @Composable ColumnScope.() -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(screenColor)
            .padding(top = ToolbarTopPadding),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CashCountToolbar(
            title = toolbarTitle,
            leftIconConfig = CashCountToolbarConfig(
                iconResId = R.drawable.ic_back,
                onIconClicked = onBackPressed
            ),
            toolbarMode = CashCountToolbarMode.LIGHT_ON_DARK
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .scrollable(rememberScrollState(), orientation = Orientation.Vertical)
        ) {

            Box(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),

                ) {
                Text(
                    text = amountLabel,
                    color = White_80_64,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(horizontal = 18.dp)
                )
                CashCountBalanceTextField(onBalanceUpdated = onBalanceUpdate)
            }

            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .clip(
                        RoundedCornerShape(
                            topStart = BottomSheetCornerRadius,
                            topEnd = BottomSheetCornerRadius
                        )
                    )
                    .background(Color.White)
                    .padding(top = 28.dp, bottom = ButtonBottomPadding)
                    .padding(horizontal = 18.dp)
            ) {
                this.bottomSheetContent()

            }
        }
    }

}