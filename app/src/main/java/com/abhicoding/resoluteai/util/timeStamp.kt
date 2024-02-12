package com.abhicoding.resoluteai.util

import java.text.SimpleDateFormat
import java.util.Date

fun formatTime(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    val currentTime = Date()
    return dateFormat.format(currentTime)
}