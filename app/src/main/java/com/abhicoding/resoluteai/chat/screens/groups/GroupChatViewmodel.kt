package com.abhicoding.resoluteai.chat.screens.groups

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.abhicoding.resoluteai.DataState
import com.abhicoding.resoluteai.chat.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GroupChatViewmodel(groupName: String) {

    val response: MutableState<DataState> = mutableStateOf(DataState.Empty)

    init {
        fetchGroupMessage(groupName)
    }

    private fun fetchGroupMessage(groupName: String) {
        val messageList = mutableListOf<Message>()
        response.value = DataState.Loading
        FirebaseDatabase.getInstance().reference.child("Groups").child(groupName).child("Chats")
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
fun sendGroupMessage(messageText: String, groupName: String, sender: String){
    val reference = FirebaseDatabase.getInstance().getReference("Groups").child(groupName).child("Chats")

    if (messageText.trim() != "" ){
        val msg = Message(
            message = messageText,
            senderId = sender,
            time = System.currentTimeMillis().toString()
        )

        val randomKey : String? = reference.push().key

        if (randomKey != null) {
            reference.child(randomKey).setValue(msg)
            Log.d("msg","$messageText Sent Successfully")
        }
    }
}
