package com.abhicoding.resoluteai.chat.screens.chats

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.abhicoding.resoluteai.DataState
import com.abhicoding.resoluteai.signup.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChatViewModel(context: Context) {

    val response: MutableState<DataState> = mutableStateOf(DataState.Empty)
    init {
        fetchAllUsers(context = context)
    }

    private fun fetchAllUsers(context: Context) {

            val userList = mutableListOf<User>()
            response.value = DataState.Loading
            FirebaseDatabase.getInstance().getReference("Users")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (childSnapshot in snapshot.children) {
                            val group = childSnapshot.getValue(User::class.java)
                            group?.let {userList.add(it) }
                        }
                        response.value = DataState.UserSuccess(userList)
                        Toast.makeText(context,"Retrieved Users Successfully", Toast.LENGTH_SHORT).show()
                        Log.d("msg","Retrieved Users Successfully")
                    }

                    override fun onCancelled(error: DatabaseError) {
                        response.value = DataState.Failure(error.message)
                        Log.e(ContentValues.TAG, "Failed to read value $error", error.toException())
                        Toast.makeText(context,"Failed to read data due to $error", Toast.LENGTH_LONG).show()
                    }
                })
    }
}