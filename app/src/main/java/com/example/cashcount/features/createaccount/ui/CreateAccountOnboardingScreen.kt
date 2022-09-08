package com.example.cashcount.features.createaccount.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashcount.R
import com.example.cashcount.components.CashCountButton
import com.example.cashcount.ui.theme.Black_25
import com.example.cashcount.ui.theme.Black_50

@Composable
fun CreateAccountOnboardingScreen(
    onNextClicked: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(id = R.string.lets_setup_your_account),
                color = Black_50,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = stringResource(id = R.string.account_onboarding_account_type_info),
                color = Black_25,
                fontSize = 18.sp
            )
        }

        Column(
            modifier = Modifier
                .padding(bottom = 12.dp)
        ) {
            CashCountButton(
                btnText = stringResource(id = R.string.lets_go),
                onClick = onNextClicked,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }

}