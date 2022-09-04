package com.example.cashcount.components

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.example.cashcount.R
import com.example.cashcount.ui.theme.TransparentTheme

@Composable
fun OtpField(
    count: Int = 6,
    onValueChange: (String) -> Unit
) {

    val context = LocalContext.current

    val otpFieldSize = 48.dp

    val otpFieldValue = remember {
        mutableStateOf("")
    }

    TransparentTheme {
        Box(
            modifier = Modifier
        ) {

            TextField(
                value = otpFieldValue.value,
                onValueChange = {
                    if (it.length <= count) {
                        otpFieldValue.value = it
                        onValueChange.invoke(it)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Transparent,
                    cursorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    focusedLabelColor = Color.Transparent
                ),
                modifier = Modifier.matchParentSize(),
            )

            Row(
                modifier = Modifier
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                (0 until count).forEach { index ->
                    Box(contentAlignment = Alignment.Center) {
                        if (index >= otpFieldValue.value.length) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(R.drawable.ic_otp_blank)
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(otpFieldSize)
                                    .align(Alignment.Center)
                                    .padding(12.dp)
                            )
                        } else {
                            Text(
                                text = otpFieldValue.value[index].toString(),
                                fontSize = 38.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black,
                                modifier = Modifier
                                    .size(otpFieldSize)
                                    .align(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun OtpFieldPreview() {

    Column(modifier = Modifier.padding(18.dp)) {
        OtpField(
            onValueChange = {

            }
        )
    }

}