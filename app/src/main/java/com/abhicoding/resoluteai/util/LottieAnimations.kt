package com.abhicoding.resoluteai.util

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.abhicoding.resoluteai.R
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
fun Progress(modifier: Modifier = Modifier) {
    val progress = remember {
        Animatable(0f)
    }

    LaunchedEffect(Unit) {
        progress.animateTo(1f, animationSpec = tween(durationMillis = 1000))
    }

    Box(modifier = modifier) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie))

        val progressState = animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            isPlaying = true,
            speed = 3f,
            reverseOnRepeat = true,
            restartOnPlay = true,
        )
        LottieAnimation(
            composition = composition,
            progress = progressState.progress,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
        )

    }
}

@Composable
fun LottieButton2(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val progress = remember { Animatable(0f) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottiebtn))

    LaunchedEffect(Unit) {
        progress.animateTo(1f, animationSpec = tween(durationMillis = 1000))
    }

    Button(
        onClick = {
            onClick()
//            progress.animateTo(0f, animationSpec = tween(durationMillis = 1000))
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues()
    ) {

            val progressState = animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                isPlaying = true,
                speed = 2f,
                reverseOnRepeat = true,
                restartOnPlay = true
            )
            LottieAnimation(
                composition = composition,
                progress = progressState.progress,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterVertically)

            )
        }
    }


@Composable
fun ChatsAppProgress(modifier: Modifier = Modifier) {

    val progress = remember {
        Animatable(0f)
    }

    LaunchedEffect(Unit) {
        progress.animateTo(1f, animationSpec = tween(durationMillis = 1000))
    }

    Box(
        modifier = modifier
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.chatsapp))

        val progressState = animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            isPlaying = true,
            speed = 2f,
            reverseOnRepeat = true,
            restartOnPlay = true,
            )
        LottieAnimation(
            composition = composition,
            progress = progressState.progress,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
        )

    }
}

@Composable
fun Otp(modifier: Modifier = Modifier) {
    val progress = remember {
        Animatable(0f)
    }

    LaunchedEffect(Unit) {
        progress.animateTo(1f, animationSpec = tween(durationMillis = 1000))
    }

    Box(
        modifier = modifier
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.otp))

        val progressState = animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            isPlaying = true,
            speed = 2f,
            reverseOnRepeat = true,
            restartOnPlay = true,
        )
        LottieAnimation(
            composition = composition,
            progress = progressState.progress,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
        )

    }
}



