package com.abhicoding.resoluteai.chat.screens.notifications

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

@Composable
fun NotificationsScreen() {

    val context = LocalContext.current

    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w("FCM", "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }

        // Get new FCM registration token
        val token = task.result

        // Log and toast
        Log.d("FCM", token.toString())
        Toast.makeText(context, token.toString(), Toast.LENGTH_SHORT).show()
    })

    FirebaseMessagingScreen()
}