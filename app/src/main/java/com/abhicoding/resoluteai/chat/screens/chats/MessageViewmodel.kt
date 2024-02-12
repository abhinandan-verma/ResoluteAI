package com.abhicoding.resoluteai.chat.screens.chats

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.abhicoding.resoluteai.DataState
import com.abhicoding.resoluteai.chat.Message
import com.abhicoding.resoluteai.util.formatTime
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageViewmodel(currentUser: String, nextUser: String) : ViewModel(){
    val response: MutableState<DataState> = mutableStateOf(DataState.Empty)

    init {
        fetchAllMessage(currentUser, nextUser)
    }

    private fun fetchAllMessage(currentUser: String, nextUser: String) {
        val messageList = mutableListOf<Message>()
        response.value = DataState.Loading
        FirebaseDatabase.getInstance().reference.child("Messages").child(createMessageId(currentUser, nextUser))
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (childSnapshot in snapshot.children) {
                        val message = childSnapshot.getValue(Message::class.java)
                        message?.let { messageList.add(it) }
                    }
                    response.value = DataState.MessageSuccess(messageList)
                    //   Toast.makeText(context,"Retrieved Message Successfully", Toast.LENGTH_SHORT).show()
                    Log.d("msg", "Retrieved Message Successfully")
                }

                override fun onCancelled(error: DatabaseError) {
                    response.value = DataState.Failure(error.message)
                    Log.e(
                        ContentValues.TAG,
                        "Failed to read messages $error",
                        error.toException()
                    )
                    //  Toast.makeText(context,"Failed to read data due to $error", Toast.LENGTH_LONG).show()
                }
            })
    }

}

fun sendMessage(messageText: String, currentUser: String, nextUser: String){
    val reference = FirebaseDatabase.getInstance().getReference("Messages").child(createMessageId(currentUser, nextUser))

    if (messageText.trim() != "" ){
        val msg = Message(
                message = messageText,
                senderId = currentUser,
                time = formatTime()
        )

        val randomKey : String? = reference.push().key

        if (randomKey != null) {
            reference.child(randomKey).setValue(msg)
            Log.d("msg","$messageText Sent Successfully")
        }
    }
}

fun createMessageId(currentUser: String, nextUser: String): String {
    val firstLetter1 = currentUser.lowercase()[0]
    val firstLetter2 = nextUser.lowercase()[0]

    return if (firstLetter1 < firstLetter2) {
        "$currentUser$nextUser"
    } else {
        "$nextUser$currentUser"
    }
}

