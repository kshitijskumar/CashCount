package com.example.cashcount.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.cashcount.R
import com.example.cashcount.ui.theme.Violet

@Composable
fun LoadingDialog(
    shouldShow: Boolean
) {
    if (shouldShow) {
        AlertDialog(
            onDismissRequest = { },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            text = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp)
                ) {
                    CircularProgressIndicator(
                        color = Violet
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(40.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = stringResource(id = R.string.loading),
                            fontWeight = FontWeight.Light,
                            color = Color.Black,
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.Center)
                        )
                    }
                }
            },
            buttons = {}
        )
    }
}