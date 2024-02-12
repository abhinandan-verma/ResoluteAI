package com.abhicoding.resoluteai.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.abhicoding.resoluteai.chat.ChatsActivity
import com.abhicoding.resoluteai.login.MainActivity
import com.abhicoding.resoluteai.login.auth
import com.abhicoding.resoluteai.signup.User
import com.abhicoding.resoluteai.ui.theme.ResoluteAITheme
import com.abhicoding.resoluteai.util.ChatsAppProgress
import com.abhicoding.resoluteai.util.Progress
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.util.Locale


val reference = Firebase.database.getReference("Users")


class UserActivity : ComponentActivity() {


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {

        var path = ""
        if (intent.hasExtra("PATH1")) {
            path = intent.getStringExtra("PATH1").toString()
        } else if (intent.hasExtra("PATH2")) {
            path = intent.getStringExtra("PATH2").toString()
        } else if (intent.hasExtra("PATH3")) {
            path = intent.getStringExtra("PATH3").toString()
        } else if (intent.hasExtra("PATH4")) {
            path = intent.getStringExtra("PATH4").toString()
        } else if (intent.hasExtra("PATHM")) {
            path = intent.getStringExtra("PATHM").toString()
        }

        Log.d("msg", path)

        super.onCreate(savedInstanceState)
        setContent {
            ResoluteAITheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(20.dp)
                ) {
                    LocalContext.current
                    var fromEmail by remember {
                        mutableStateOf(true)
                    }
                    if (intent.hasExtra("PATHM")){
                        fromEmail = false
                    }
                    var userData by remember {
                        mutableStateOf(User("", "", ""))
                    }
                    var name by remember {
                        mutableStateOf("")
                    }
                    var email1 by remember {
                        mutableStateOf("")
                    }
                    var password by remember {
                        mutableStateOf("")
                    }
                    var location by remember {
                        mutableStateOf("User Location")
                    }
                    val geocoder = Geocoder(this@UserActivity, Locale.getDefault())

                    var addresses: MutableList<Address>?
                    fusedLocationProviderClient =
                        LocationServices.getFusedLocationProviderClient(this@UserActivity)
                    var address by remember {
                        mutableStateOf<Address?>(null)
                    }

                    if (ActivityCompat.checkSelfPermission(
                            this@UserActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this@UserActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermission()
                        return@Box
                    }
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener { locate ->
                        if (locate != null) {
                            addresses =
                                geocoder.getFromLocation(locate.latitude, locate.longitude, 1)!!
                            if (addresses != null) {
                                address = addresses!![0]
                                location = address!!.getAddressLine(0).toString()
                            } else {
                                location = "Addresses is Null"
                            }

                        } else {
                            location = "Location Access Not Granted"
                        }
                    }

                    Log.d("location", "User Location : $location")
                    reference.child(path).addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            // Get the user data from the snapshot
                            name = snapshot.child("name").value.toString()
                            email1 = snapshot.child("email").value.toString()
                            password = snapshot.child("password").value.toString()


                            // Update the state variable with the user data

                            Log.d("msg", "User name $name")
                            Log.d("msg", "User email $email1")
                            Log.d("msg", "User password $password")
                            userData = User(name, email1, password)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle the error
                            Log.d("error", error.details)
                        }
                    })

                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        ChatsAppProgress(modifier = Modifier.size(80.dp))
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "<--- Profile Page --->",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(30.dp))

                        Row {
                            Text(
                                text = "Hello $name",
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 35.sp,
                                modifier = Modifier
                                    .padding(5.dp)
                            )

                            Progress(
                                modifier = Modifier.size(65.dp)
                                    .padding(bottom = 10.dp)
                            )
                        }


                        Box(
                            modifier = Modifier
                                .border(5.dp, Color.Green, shape = RoundedCornerShape(10.dp))
                                .padding(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "User Name : $name",
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = if (fromEmail) {
                                        "User Email : $email1"
                                    } else {
                                        "User Mobile No.: $email1"
                                    },
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "User Password : $password",
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(20.dp))

                                Text(
                                    text = "User Location : $location",
                                    fontWeight = FontWeight.Bold
                                )
                            }

                        }
                        Spacer(modifier = Modifier.height(30.dp))


                            Button(
                                onClick = {
                                    val intent =
                                        Intent(this@UserActivity, ChatsActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                },
                                colors = ButtonDefaults.textButtonColors(Color.Green),
                                modifier = Modifier
                                    .fillMaxWidth()

                            ) {
                                Text(
                                    text = "Go to ChatsActivity ",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )

                            }

                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.End
                        ) {

                            Button(onClick = {
                                auth.signOut()
                                Toast.makeText(
                                    this@UserActivity,
                                    "$name signed out successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d("msg", "$name signed out successfully")
                                val newIntent = Intent(this@UserActivity, MainActivity::class.java)
                                startActivity(newIntent)
                                finish()
                            },
                                colors = ButtonDefaults.textButtonColors(Color.Magenta),
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(text = "Sign Out",
                                    color = Color.White)
                            }
                        }
                    }
                }
            }
        }

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            100
        )

    }
}








