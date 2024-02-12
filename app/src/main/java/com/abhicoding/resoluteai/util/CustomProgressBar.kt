package com.abhicoding.resoluteai.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmojiProgressBar(progress: Float) {
    var switch by remember { mutableStateOf(false) }
    val bgColor by animateColorAsState(if (switch) Color.Red else Color.Green, label = "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Circular progress indicator
        CircularProgressIndicator(
            modifier = Modifier.size(100.dp),
            color = bgColor,
            strokeWidth = 10.dp
        )

        // Emoji or text indicating progress
        Text(
            text = "Loading... ${((progress * 100).toInt())}%",
            color = Color.Gray,
            fontSize = 20.sp
        )

        // Toggle the color animation
        Button(
            onClick = { switch = !switch },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Toggle Color")
        }
    }
}

@Composable
fun SmilingEmojiProgressBar(
    progress: Float,
    durationMillis: Int = 1600
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "Progressing"
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(8.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw the base circle
            drawCircle(
                color = Color.Gray,
                radius = size.minDimension / 2,
                center = center
            )

            // Draw the smiling face
            rotate(rotation) {
                drawArc(
                    color = Color.Yellow,
                    startAngle = 45f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(size.width / 4, size.height / 4),
                    size = Size(size.width / 2, size.height / 2),
                )
            }
        }

        // Draw the progress bar
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = Color.Blue,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                topLeft = Offset(size.width / 4, size.height / 4),
                size = Size(size.width / 2, size.height / 2),

            )
        }
    }
}


