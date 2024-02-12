package com.abhicoding.resoluteai.chat.screens.groups

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhicoding.resoluteai.DataState
import com.abhicoding.resoluteai.R
import com.google.firebase.Firebase
import com.google.firebase.database.database

@Composable
fun GroupsScreen(
    modifier: Modifier = Modifier) {
    val viewModel = GroupViewModel(LocalContext.current)

    val context = LocalContext.current

    val createNewGroupDialog = remember {
        mutableStateOf(false)
    }
    val newGroupName = remember {
        mutableStateOf("")
    }
    val newGroupDescription = remember {
        mutableStateOf("")
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    createNewGroupDialog.value = true
                },
                containerColor = Color.Green,
                contentColor = Color.Black
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Group",
                    Modifier.size(30.dp)
                )
            }
        }
    ) {innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            SetData(viewModel = viewModel)
        }

        if(createNewGroupDialog.value){
            AlertDialog(
                onDismissRequest = { createNewGroupDialog.value = false },
                title = { Text(text = "Group Name") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newGroupName.value,
                            onValueChange = { newGroupName.value = it },
                            label = { Text(text = "Enter Group Name") }
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        OutlinedTextField(
                            value = newGroupDescription.value,
                            onValueChange = { newGroupDescription.value = it },
                            label = { Text(text = "Enter Group Desscrition") }
                        )
                    }
                },
                textContentColor = Color.Black,
                confirmButton = {
                    Button(
                        onClick = {
                            createNewGroup(newGroupName, newGroupDescription, context)
                            createNewGroupDialog.value = false
                        },
                        content = { Text(text = "Save")
                        },

                    )
                },
                dismissButton = {
                    Button(
                        onClick = {
                            Toast.makeText(context, "Cancelling...", Toast.LENGTH_SHORT).show()
                            createNewGroupDialog.value = false
                        },
                        content = { Text(text = "Cancel") },
                        colors = ButtonDefaults.buttonColors(Color.Red)
                    )
                }
            )
        }
    }
}

@Composable
fun SetData(viewModel:GroupViewModel) {
    when (val result = viewModel.response.value) {
        is DataState.Success -> {
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
fun ShowLazyList(groups: MutableList<FireGroup>) {
    val context = LocalContext.current

    LazyColumn {
        items(groups) {group ->
            ElevatedCard(
                modifier = Modifier
                    .padding(8.dp),
                shape = RoundedCornerShape(15.dp),
                onClick = {
                    val intent = Intent(context, MessageActivity::class.java)
                    intent.putExtra("GROUP_NAME",group.groupName)
                    context.startActivity(intent)

                }
            ) {
                Row(
                    Modifier
                        .background(
                            Brush.linearGradient(
                                listOf(Color.White,Color.Cyan)
                            )
                        )
                        .padding(16.dp)
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.group),
                        colorFilter = ColorFilter.tint(Color.Black),
                        alignment = Alignment.TopStart,
                        contentDescription = "Group",
                        modifier = Modifier.size(40.dp)

                    )
                    Spacer(modifier = Modifier.padding(3.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = group.groupName!!,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 22.sp
                        )
                        Text(
                            text = group.groupDescription!!,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}


fun createNewGroup(newGroupName: MutableState<String>,
                   newGroupDescription:MutableState<String>,
                   context: Context
) {
        val database = Firebase.database
        val reference = database.getReference("Groups")

    try {
        val group = FireGroup(newGroupName.value,"Default Icon", newGroupDescription.value)
        reference.child(newGroupName.value).setValue(group).addOnSuccessListener {
            Log.d("msg","Group $newGroupName created successfully")
            Toast.makeText(context,"Group $newGroupName created successfully",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context,"Error: ${it.message.toString()}\nGroup could not be created",Toast.LENGTH_SHORT).show()
        }
    }catch (e: Exception){
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
    }

}

