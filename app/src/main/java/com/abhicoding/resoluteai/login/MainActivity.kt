package com.abhicoding.resoluteai.login

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.abhicoding.resoluteai.getActivity
import com.abhicoding.resoluteai.signup.User
import com.abhicoding.resoluteai.signup.reference
import com.abhicoding.resoluteai.signup.updateUI
import com.abhicoding.resoluteai.ui.theme.ResoluteAITheme
import com.abhicoding.resoluteai.user.UserActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

val auth = FirebaseAuth.getInstance()
var storedVerificationId: String = ""
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ResoluteAITheme {

                Login()
            }
        }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload(currentUser)
        }
    }

    private fun reload(currentUser: FirebaseUser) {
        Log.d("msg","$currentUser name")
        Toast.makeText(this, "Authenticating ${currentUser.email}", Toast.LENGTH_LONG).show()
        val intent = Intent(this,UserActivity::class.java).apply {
            putExtra("PATH3",currentUser.email?.removeSuffix(".com"))
        }
        startActivity(intent)
        finish()
    }
}

fun signInWithEmail(email: String, password: String, context: Context){

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithEmail:success")
                val user = auth.currentUser
                updateUI(user,context)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(
                    context,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
                updateUI(null,context)
            }
        }.addOnSuccessListener {
            Log.d("msg","Sign in successful")
            Toast.makeText(context,"Sign in successful\nRedirecting to Chats",Toast.LENGTH_SHORT).show()

            val intent = Intent(context,UserActivity::class.java).apply {
                   putExtra("PATH1", email.removeSuffix(".com"))
               }
               context.startActivity(intent)
        }
}

fun verifyPhoneNumberWithCode(context: Context, verificationId: String, otp: String, username: String, phoneNumber: String) {
    val credential = PhoneAuthProvider.getCredential(verificationId, otp)
    signInWithPhoneAuthCredential(context, credential, username = username, phoneNumber = phoneNumber)
}

fun onLoginClicked(context: Context, phoneNumber: String,username: String, onCodeSent: () -> Unit) {
    auth.setLanguageCode("en")
    val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("msg", "Otp verified Successfully")
            Toast.makeText(context, "Otp verified Successfully", Toast.LENGTH_SHORT).show()
            signInWithPhoneAuthCredential(context, credential, username = username, phoneNumber)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Log.d("msg", "Otp verification Failed")
            Toast.makeText(context, "Otp Verification Failed", Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {

            Log.d("msg", "code sent $verificationId")
            storedVerificationId = verificationId
            onCodeSent()
            super.onCodeSent(verificationId, token)
        }
    }
    val options = context.getActivity()?.let {
        PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(it) // Activity (for callback binding)
            .setCallbacks(callback) // OnVerificationStateChangedCallbacks
            .build()
    }
    Log.d("msg", options.toString())
    if (options != null) {
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}

private fun signInWithPhoneAuthCredential( context: Context, credential: PhoneAuthCredential, username: String, phoneNumber: String) {
    context.getActivity()?.let {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in is success update UI with the signed-in user's information
                    val user = task.result?.user
                    Log.d("msg", "$user Logged In")
                    Toast.makeText(context,"$user logged in", Toast.LENGTH_SHORT).show()

                    val user2 = User(username, phoneNumber, "Password Not Set")

                    reference.child(phoneNumber).setValue(user2).addOnSuccessListener {
                        val intent = Intent(context, UserActivity::class.java).apply {
                           putExtra("PATHM",phoneNumber)
                        }
                        context.startActivity(intent)
                    }

                } else {
                    // Signed in failed , display the message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // the verification code was invalid
                        Log.d("msg", "Wrong Otp")
                        Toast.makeText(context, "Wrong otp", Toast.LENGTH_SHORT).show()
                    }
                    // Update ui
                }
            }
    }
}
