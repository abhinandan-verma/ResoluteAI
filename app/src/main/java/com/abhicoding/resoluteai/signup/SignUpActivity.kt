package com.abhicoding.resoluteai.signup

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.abhicoding.resoluteai.ui.theme.ResoluteAITheme
import com.abhicoding.resoluteai.user.UserActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val database = Firebase.database
val reference = database.getReference("Users")
class SignUpActivity : ComponentActivity() {

    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ResoluteAITheme {
                SignUp()
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload(currentUser)
        }
    }

    private fun reload(currentUser: FirebaseUser) {
        Toast.makeText(this, "Authenticating ${currentUser.email}", Toast.LENGTH_LONG).show()
        Toast.makeText(this, "Redirecting...", Toast.LENGTH_LONG).show()
        val intent = Intent(this, UserActivity::class.java).apply {
            putExtra("PATH4",currentUser.email?.removeSuffix(".com"))
        }
        startActivity(intent)
        finish()
    }


}
@OptIn(DelicateCoroutinesApi::class)
fun signUpUser(name: String, email: String, password: String, context: Context) {
    val auth = Firebase.auth
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(ContentValues.TAG, "createUserWithEmail:success")
                val user = auth.currentUser
                updateUI(user, context)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    context,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
                updateUI(null, context)
            }
        }.addOnSuccessListener {
            Log.d("msg", "User signed up Successfully")
            Toast.makeText(context, "You Signed Up Successfully", Toast.LENGTH_SHORT).show()

                val user = User(name, email, password)

            reference.child(email.removeSuffix(".com")).setValue(user).addOnSuccessListener {
                Log.d("msg", "User $name document created successfully in realtime db")
                GlobalScope.launch {
                    delay(2000)
                    val intent = Intent(context, UserActivity::class.java).apply {
                        putExtra("PATH2", email.removeSuffix(".com"))
                    }
                    context.startActivity(intent)
                }

            }.addOnFailureListener {
                Log.d("msg", "User $name document creation failedq")
            }



        }
}

    fun updateUI(user: FirebaseUser?, context: Context) {
        Log.d("tag","Updating... for ${user.toString()}")
        Toast.makeText(context,"Updating for ${user.toString()}",Toast.LENGTH_SHORT).show()
    }


