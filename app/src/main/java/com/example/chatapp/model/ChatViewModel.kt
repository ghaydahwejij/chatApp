package com.example.chatapp.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ChatViewModel: ViewModel() {
    var database = FirebaseDatabase.getInstance()
    var auth=Firebase.auth
    var dbRef=database.getReference()
    var myEmail by mutableStateOf("")
    var messeges= mutableListOf<Messege>()


    init {
        getMessages()
        auth.currentUser?.email?.let {
             myEmail=it
        }
    }

    fun sendMessage(email: String, text:String)
    {
        if(text.isNotBlank())
        {   val message=Messege(email,text)
            val newmsgRef=dbRef.push()
            dbRef.setValue(message)
        }
    }
    fun getMessages(){
        val postlistener=object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val messagesList= mutableListOf<Messege>()

                snapshot.children.forEach{
                    message->
                    val from=message.child("email").getValue(String::class.java).toString()
                    val text=message.child("text").getValue(String::class.java).toString()
                    val conv=Messege(from,text)
                    messagesList.add(conv)
                }
               messeges=messagesList
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        dbRef.addValueEventListener(postlistener)
    }
}