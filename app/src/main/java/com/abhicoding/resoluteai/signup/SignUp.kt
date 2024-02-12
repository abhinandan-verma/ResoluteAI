package com.abhicoding.resoluteai.signup

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhicoding.resoluteai.util.Progress

@Composable
fun SignUp() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

            Card (
                modifier = Modifier.background(Color.White)
            ){
                val context = LocalContext.current
                var email by remember {
                    mutableStateOf("")
                }
                var password by remember {
                    mutableStateOf("")
                }
                var name by remember {
                    mutableStateOf("")
                }
                var progress by remember {
                    mutableStateOf(false)
                }
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(1f)
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sign Up Here",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                    OutlinedTextField(value = name,
                        onValueChange = { name = it },
                        label = { Text(text = "Username") }
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(text = "Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(text = "Password") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    Button(
                        onClick = {
                            Log.d("msg", "Signing Up User")
                            progress = true
                            signUpUser(name, email, password, context)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            Color.Green
                        ),
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            text = "Create Account",
                            color = Color.Black
                        )
                    }
                    if (progress){
                        Progress()
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

            }

        }
    }
