package com.abhicoding.resoluteai.chat.screens.chats

import android.content.Intent
import android.widget.ProgressBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhicoding.resoluteai.DataState
import com.abhicoding.resoluteai.signup.SignUpActivity
import com.abhicoding.resoluteai.signup.User
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ChatsScreen() {

    val viewModel = ChatViewModel(LocalContext.current)
    val context = LocalContext.current


    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    context.startActivity(Intent(context, SignUpActivity::class.java))
                },
                containerColor = Color.Magenta,
                contentColor = Color.Black) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Group",
                    Modifier.size(30.dp)
                )
            }
        }
    ){innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding)
        ){
            Text(text = "Explore Other Users",
                fontWeight = FontWeight.Bold,
                color = Color.Yellow,
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            )

            SetUserData(viewModel = viewModel)
        }
    }

}

@Composable
fun SetUserData(viewModel: ChatViewModel) {
    when (val result = viewModel.response.value) {
        is DataState.UserSuccess -> {
            ShowLazyList(result.data)
        }

        is DataState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ProgressBar(LocalContext.current)
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
fun ShowLazyList(usersList: MutableList<User>) {
    val context = LocalContext.current

    var currentUserEmail by remember {
        mutableStateOf("")
    }

    currentUserEmail = FirebaseAuth.getInstance().currentUser?.email.toString()

    LazyColumn {
        items(usersList) {user ->
            ElevatedCard(
                modifier = Modifier
                    .padding(8.dp),
                shape = RoundedCornerShape(15.dp),
                onClick = {
                    val intent = Intent(context, UserChatActivity::class.java)
                    intent.putExtra("NEXT_USER",user.email.removeSuffix(".com"))
                    intent.putExtra("CURRENT_USER",currentUserEmail.removeSuffix(".com") )
                    context.startActivity(intent)

                }
            ) {
                Row(
                    Modifier
                        .background(
                            Brush.linearGradient(
                                listOf(Color.Yellow, Color.Green, Color.Cyan)
                            )
                        )
                        .padding(16.dp)
                ) {
                    Image(
                        imageVector = Icons.TwoTone.Person,
                        colorFilter = ColorFilter.tint(Color.Black),
                        alignment = Alignment.TopStart,
                        contentDescription = "Group",
                        modifier = Modifier.size(40.dp)

                    )
                    Spacer(modifier = Modifier.padding(3.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = user.name,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 22.sp
                        )
                        Text(
                            text = user.email,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}