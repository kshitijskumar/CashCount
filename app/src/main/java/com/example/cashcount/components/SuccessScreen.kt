package com.example.cashcount.components

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cashcount.ui.theme.Black_50

@Composable
fun SuccessScreen(
    @DrawableRes iconResId: Int,
    message: String,
    onScreenClicked: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onScreenClicked.invoke() },
        contentAlignment = Alignment.Center
    ) {

        val context = LocalContext.current

        val backPressCallback = remember {
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onScreenClicked.invoke()
                }
            }
        }

        val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
        val lifecycleOwner = LocalLifecycleOwner.current

        backDispatcher?.let {
            DisposableEffect(it, lifecycleOwner) {
                backDispatcher.addCallback(lifecycleOwner, backPressCallback)

                onDispose {
                    backPressCallback.remove()
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(iconResId)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                color = Black_50,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
        }


    }


}