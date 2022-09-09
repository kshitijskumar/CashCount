package com.example.cashcount.features.createaccount.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashcount.R
import com.example.cashcount.components.*
import com.example.cashcount.ui.theme.Violet
import com.example.cashcount.ui.theme.White_80_64

@Composable
fun CreateAccountScreen(
    onBackPressed: () -> Unit
) {

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Violet)
            .padding(top = ToolbarTopPadding),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CashCountToolbar(
            title = stringResource(id = R.string.add_new_account),
            leftIconConfig = CashCountToolbarConfig(
                iconResId = R.drawable.ic_back,
                onIconClicked = onBackPressed
            ),
            toolbarMode = CashCountToolbarMode.LIGHT_ON_DARK
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Box(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),

            ) {
                Text(
                    text = stringResource(id = R.string.balance),
                    color = White_80_64,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(horizontal = 18.dp)
                )
                CashCountBalanceTextField(onBalanceUpdated = {

                })
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
                
                val accountNameState = remember {
                    mutableStateOf("")
                }
                
                CashCountOutlinedTextField(
                    value = accountNameState.value,
                    onValueChange = {
                        accountNameState.value = it
                    },
                    placeholderText = stringResource(id = R.string.account_name)
                )
                Spacer(modifier = Modifier.height(24.dp))

                CashCountDropdown(
                    selectedAccountType = null
                )

                Spacer(modifier = Modifier.height(24.dp))
                CashCountButton(
                    btnText = stringResource(id = R.string.continue_text),
                    modifier = Modifier.fillMaxWidth()
                ) {

                }
            }
        }
    }

}