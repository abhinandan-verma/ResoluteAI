package com.abhicoding.resoluteai.util

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.abhicoding.resoluteai.R
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
fun Progress() {
    val progress = remember {
        Animatable(0f)
    }

    LaunchedEffect(Unit) {
        progress.animateTo(1f, animationSpec = tween(durationMillis = 1000))
    }

    Box() {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie))

        val progressState = animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            isPlaying = true,
            speed = 4.5f,
            reverseOnRepeat = true,
            restartOnPlay = true,
            clipSpec = LottieClipSpec.Progress(0f, 1f),

        )
        LottieAnimation(
            composition = composition,
            progress = progressState.progress,
            modifier = Modifier.fillMaxSize()
                .align(Alignment.Center)
        )

    }
}

@Composable
fun LottieButton(onClick : () -> Unit){
    val progress = remember {
        Animatable(0f)
    }

    LaunchedEffect(Unit) {
        progress.animateTo(1f, animationSpec = tween(durationMillis = 1000))
    }
    Box(
        modifier = Modifier.fillMaxSize(1f)
            .size(70.dp),
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottiebtn))

        val progressState = animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            isPlaying = true,
            speed = 2.0f,
            reverseOnRepeat = true,
            restartOnPlay = true,
            clipSpec = LottieClipSpec.Progress(0f, 1f),

            )
        LottieAnimation(
            composition = composition,
            progress = progressState.progress,
            modifier = Modifier.fillMaxSize(1f)

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
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val progressState = animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                isPlaying = true,
                speed = 2f,
                reverseOnRepeat = true,
                restartOnPlay = true,
                clipSpec = LottieClipSpec.Progress(0f, 1f),
            )
            LottieAnimation(
                composition = composition,
                progress = progressState.progress,
                modifier = Modifier.align(Alignment.Center).fillMaxSize()
            )
        }
    }
}