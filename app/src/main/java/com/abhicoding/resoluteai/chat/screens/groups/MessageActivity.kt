package com.abhicoding.resoluteai.chat.screens.groups

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.abhicoding.resoluteai.chat.screens.groups.ui.theme.ResoluteAITheme
import com.abhicoding.resoluteai.util.LottieButton2
import com.abhicoding.resoluteai.util.Progress
import com.google.firebase.auth.FirebaseAuth

class MessageActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResoluteAITheme {

                var message by remember{
                        mutableStateOf("")
                }

                val groupName = intent.getStringExtra("GROUP_NAME")!!

                val viewmodel = GroupChatViewmodel(groupName)

                val currentUser = FirebaseAuth.getInstance().currentUser!!.email?.removeSuffix(".com").toString()

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Column {
                                    Text(
                                        text = groupName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Group Description",
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomAppBar(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black,

                            ){
                            OutlinedTextField(
                                value = message,
                                onValueChange = {
                                    message = it
                                },
                                label = {
                                    Text(text = "Start Chatting...")
                                },
                                modifier = Modifier.fillMaxWidth(.75F),
                                shape = RoundedCornerShape(35.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            LottieButton2 (
                                modifier = Modifier.fillMaxSize()
                            ){
                                sendGroupMessage(message, groupName, currentUser )
                                message = ""
                            }
                        }
                    }
                ){
                    Column (
                        modifier = Modifier.padding(it)
                    ){
                        ShowGroupMessage(viewmodel)
                    }

                }
            }
        }
    }
}

@Composable
fun ShowGroupMessage(viewModel: GroupChatViewmodel) {
    when (val result = viewModel.response.value) {
        is DataState.MessageSuccess -> {
           ShowGroupMessagesList(result.data)
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
fun ShowGroupMessagesList(messageList: MutableList<Message>) {
    val  currentUserEmail = FirebaseAuth.getInstance().currentUser?.email?.removeSuffix(".com").toString()

    LazyColumn {
        items(messageList){message ->
            Row (
                horizontalArrangement =
                if (message.senderId == currentUserEmail) Arrangement.Start
                else
                    Arrangement.Absolute.Left
            ){

                Card(
                    modifier = Modifier
                        .padding(2.dp)
                        .wrapContentSize(),
                    shape = RoundedCornerShape(15.dp),
                    colors =
                    if(message.senderId == currentUserEmail)
                        CardDefaults.cardColors(Color.Magenta)
                    else
                        CardDefaults.cardColors(Color.Gray)
                ) {
                    Row(
                        Modifier
                            .background(
                                if (message.senderId == currentUserEmail)
                                    Color.Magenta
                                else
                                    Color.White
                            )

                            .padding(4.dp)
                    ) {

                        Column{
                            Text(
                                text = message.message!!,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 20.sp,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Text(
                                text = message.time.toString(),
                                fontSize = 10.sp,
                                color = Color.Black,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }
    }
}


