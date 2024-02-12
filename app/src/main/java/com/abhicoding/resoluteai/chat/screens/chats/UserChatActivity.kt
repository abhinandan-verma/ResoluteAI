package com.abhicoding.resoluteai.chat.screens.chats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhicoding.resoluteai.DataState
import com.abhicoding.resoluteai.chat.Message
import com.abhicoding.resoluteai.ui.theme.ResoluteAITheme
import com.abhicoding.resoluteai.util.LottieButton2
import com.abhicoding.resoluteai.util.Progress
import com.google.firebase.auth.FirebaseAuth

class UserChatActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        val currentUserEmail = intent.getStringExtra("CURRENT_USER")!!
        val nextUserEmail = intent.getStringExtra("NEXT_USER")!!

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResoluteAITheme {


                var message by remember {
                    mutableStateOf("")
                }

                val viewmodel  = MessageViewmodel(currentUserEmail, nextUserEmail)

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(
                                    modifier = Modifier

                                ) {
                                    Image(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Group Icon",
                                        modifier = Modifier
                                            .size(60.dp)
                                            .wrapContentSize()
                                            .border(shape = CircleShape, width = 2.dp, color = Color.Magenta)
                                            .padding(7.dp)
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = nextUserEmail,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = currentUserEmail,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors( Color.Magenta )
                        )
                    },
                    bottomBar = {
                        BottomAppBar(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black,
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy((-25).dp)
                            ) {
                                OutlinedTextField(
                                    value = message,
                                    onValueChange = {
                                        message = it
                                    },
                                    label = {
                                        Text(text = "Start Chatting...")
                                    },
                                    modifier = Modifier.fillMaxWidth(.76f),
                                    shape = RoundedCornerShape(35.dp)
                                )
                                LottieButton2(
                                    modifier = Modifier.size(150.dp)
                                ) {
                                    sendMessage(message, currentUserEmail, nextUserEmail)
                                    message = ""
                                }
                            }
                        }
                    }
                ) {

                    Column(
                        modifier = Modifier.padding(it)
                    ){
                        ShowMessages(viewmodel)

                    }
                }
            }
        }
    }
}

@Composable
fun ShowMessages(viewModel: MessageViewmodel) {
    when (val result = viewModel.response.value) {
        is DataState.MessageSuccess -> {
            ShowMessagesList(result.data)
        }

        is DataState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Progress()
            }
        }

        is DataState.Failure -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error!! Try Again\n")
                Text(
                    text = result.message,
                    fontSize = 30.sp,
                )
            }
        }

        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error!! Try Again\n")
                Text(
                    text = "Error Fetching Data\nPerhaps Empty List 🫥",
                    fontSize = 30.sp,
                )
            }

        }
    }
}

@Composable
fun ShowMessagesList(messagesList: MutableList<Message>) {


   val  currentUserEmail = FirebaseAuth.getInstance().currentUser?.email?.removeSuffix(".com").toString()
    LazyColumn {
        items(messagesList) {message ->

            Card(
                modifier = Modifier
                    .padding(5.dp)
                    .wrapContentSize()
                ,
                shape = RoundedCornerShape(15.dp),
                colors =
                if (message.senderId == currentUserEmail)
                    CardDefaults.cardColors(Color.Magenta)
                else
                    CardDefaults.cardColors(Color.White)
            ) {
                Column (
                    verticalArrangement = Arrangement.spacedBy((-12).dp),
                ){
                    Text(
                        text = message.message!!,
                        color = if (currentUserEmail == message.senderId) Color.White else Color.Black,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(end = 2.dp, start = 2.dp)
                    )
                    Text(
                        text = message.time.toString(),
                        fontSize = 9.sp,
                        color =  if (currentUserEmail == message.senderId) Color.White else Color.Black,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}


