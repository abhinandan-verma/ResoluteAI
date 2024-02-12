package com.abhicoding.resoluteai.login

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhicoding.resoluteai.signup.SignUpActivity
import com.abhicoding.resoluteai.util.Progress
import kotlinx.coroutines.delay

@Composable
fun Login() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        var progress by remember {
        mutableStateOf(false)
        }

        var progressVal by remember {
            mutableDoubleStateOf(0.0)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            val context = LocalContext.current
            var username by remember {
                mutableStateOf("")
            }
            var phoneNumber by remember {
                mutableStateOf("")
            }
            var otp by remember {
                mutableStateOf(TextFieldValue(""))
            }
            var isOtpVisible by remember {
                mutableStateOf(false)
            }
            var email by remember {
                mutableStateOf("")
            }
            var password by remember {
                mutableStateOf("")
            }

            LaunchedEffect(key1 = true){
                while (true){
                    delay(1400)
                    progressVal = (progressVal + 0.1f) % 1.0f
                }
            }

            if(progress){
                Row {
//                    EmojiProgressBar(progress = progressVal.toFloat())
//                    SmilingEmojiProgressBar(progress = progressVal.toFloat())

                    Progress()
                }
            }


            Spacer(modifier = Modifier.height(20.dp))

            Card {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(1f)
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Login With Phone Number",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            if (it.length <= 14) {
                                username = it
                            }
                        },
                        label = { Text("Username") }
                    )
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            if (it.length <= 14) {
                                phoneNumber = it
                            }
                        },
                        label = { Text("Phone Number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                    if (isOtpVisible) {
                        OutlinedTextField(
                            value = otp,
                            onValueChange = { otp = it },
                           label = { Text(text = "Enter OTP") },
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(top = 8.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )
                    }
                    if (!isOtpVisible) {
                        Button(
                            onClick = {
                                onLoginClicked(context, phoneNumber, username) {
                                    Toast.makeText(context, "Sent Otp Visible", Toast.LENGTH_SHORT)
                                        .show()
                                    Log.d("Msg", "Sent OTP Visible")
                                    isOtpVisible = true
                                }
                            }, colors = ButtonDefaults.textButtonColors(
                                Color.Green
                            ),
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(top = 8.dp)
                        ) {
                            Text(text = "Sent OTP", color = Color.Blue)

                        }
                    } else {
                        Button(
                            onClick = {
                                progress = true
                                verifyPhoneNumberWithCode(
                                    context,
                                    storedVerificationId,
                                    otp.text,
                                    username,
                                    phoneNumber
                                )
                            },
                            colors = ButtonDefaults.textButtonColors(Color.Cyan),
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(8.dp)
                        ) {
                            Text(text = "Verify", color = Color.Black)
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(30.dp))
            Card {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(1f)
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Login with Email and Password",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(text = "Enter Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(text = "Enter Password") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    Button(
                        onClick = {
                            progress = true
                            Log.d("msg", "Sign in Initiated")
                            signInWithEmail(email, password, context)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            Color.Cyan
                        ),
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            text = "Login ",
                            color = Color.Black
                        )
                    }

                    Button(
                        onClick = {
                            val intent = Intent(context, SignUpActivity::class.java)
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            Color.Magenta
                        ),
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            text = "Create Account",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }


                }
            }

        }
    }
}
