package com.example.chatapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import com.example.chatapp.destinations.ChatDestination
import com.example.chatapp.destinations.LoginDestination
import com.example.chatapp.destinations.SignInScreenDestination
import com.example.chatapp.destinations.peopleDestination
import com.example.chatapp.model.ChatViewModel
import com.example.chatapp.ui.theme.ChatAppTheme
import com.example.chatapp.ui.theme.Orange
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DestinationsNavHost(navGraph =NavGraphs.root)

                }
            }
        }
    }
}
@Destination()
@Composable
fun SignInScreen(nav:DestinationsNavigator) {
    lateinit var auth:FirebaseAuth
    auth = FirebaseAuth.getInstance()

    var email by remember {
        mutableStateOf("")
    }
    var pass by remember {
        mutableStateOf("")
    }

        Box(

            Modifier
                .fillMaxSize()
                .background(Color.Red)){
        Image(
            painter = painterResource(id = R.drawable.bac),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 400.dp)
                .paint(
                    painter = painterResource(R.drawable.bac),
                    contentScale = ContentScale.FillWidth
                )
        )
        Card(modifier = Modifier
            .fillMaxSize()
            .padding(top = 200.dp),
            RoundedCornerShape(topStart = 70.dp)) {
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                Text(text = "Sign Up", color = Orange, fontSize = 35.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    modifier=Modifier.width(350.dp),

                    value = email,
                    onValueChange = {
                        email = it
                    },
                    leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon") },
                    label = { Text(text = "Email") },
                    placeholder = { Text(text = "Type your email") }
                )
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    modifier= Modifier
                        .width(350.dp)
                        .clip(RectangleShape),
                    value = pass,
                    onValueChange = {
                        pass = it
                    },

                    leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Email Icon") },
                    label = { Text(text = "Password") },
                    placeholder = { Text(text = "Type your password")

                    }


                )
                Spacer(modifier = Modifier.height(150.dp))

                Button(onClick = { val uEmail = email
                    val uPass = pass

                    if (uPass.isNotEmpty() && uEmail.isNotEmpty()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                auth.createUserWithEmailAndPassword(uEmail, uPass).await()
                                withContext(Dispatchers.Main) {
                                    if (auth.currentUser == null) { // not logged in
                                        Log.d ("tag","You are not signed up")
                                    } else {
                                        Log.d ("tag","You are  signed up")
                                    }                    }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
//                                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                                    Log.d ("tag","${e.message}")

                                }
                            }
                        }
                    }
                } ,modifier = Modifier
                    .clip(RectangleShape)
                    .width(340.dp) ) {
                    Text(text="Sign Up", fontSize = 30.sp)

            }
                Row(){
                    Text(text="Do you have a account?",fontSize = 15.sp)
                    Text(text =" Login here", fontSize = 15.sp, modifier = Modifier.clickable {
                        nav.navigate(LoginDestination)
                    }, color = Orange )
                }
            }

        }
    }
}
@Destination(start=true)
@Composable
fun Login(nav:DestinationsNavigator){
    lateinit var auth:FirebaseAuth
    auth = FirebaseAuth.getInstance()
   // auth.signOut()
    var email by remember {
        mutableStateOf("")
    }
    var pass by remember {
        mutableStateOf("")
    }

    Box(

        Modifier
            .fillMaxSize()
            .background(Color.Red)){
        Image(
            painter = painterResource(id = R.drawable.bac),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 400.dp)
                .paint(
                    painter = painterResource(R.drawable.bac),
                    contentScale = ContentScale.FillWidth
                )
        )
        Card(modifier = Modifier
            .fillMaxSize()
            .padding(top = 200.dp),
            RoundedCornerShape(topStart = 70.dp)) {
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                Text(text = "Log In", color = Orange, fontSize = 35.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    modifier=Modifier.width(350.dp),

                    value = email,
                    onValueChange = {
                        email = it
                    },
                    leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon") },
                    label = { Text(text = "Email") },
                    placeholder = { Text(text = "Type your email") }
                )
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    modifier= Modifier
                        .width(350.dp)
                        .clip(RectangleShape),
                    value = pass,
                    onValueChange = {
                        pass = it
                    },

                    leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Email Icon") },
                    label = { Text(text = "Password") },
                    placeholder = { Text(text = "Type your password")

                    }


                )
                Spacer(modifier = Modifier.height(150.dp))

                Button(onClick = {  val uEmail = email
                    val uPass = pass

                    if (uPass.isNotEmpty() && uEmail.isNotEmpty()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                auth.signInWithEmailAndPassword(uEmail, uPass).await()
                                withContext(Dispatchers.Main) {
                                    if (auth.currentUser == null) { // not logged in
                                        Log.d ("tag","You are not logged in")
                                    } else {
                                        Log.d ("tag","You are  logged in")
                                    }                    }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
//                                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                     nav.navigate(peopleDestination)
                                 }, modifier = Modifier
                    .clip(RectangleShape)
                    .width(340.dp) ) {
                    Text(text="Login", fontSize = 30.sp)

                }
                Row(){
                    Text(text="Do you have a account?",fontSize = 15.sp)
                    Text(text =" Sign Up here", fontSize = 15.sp, modifier = Modifier.clickable { nav.navigate(SignInScreenDestination)}, color = Orange )
                }
            }

        }
    }
}
@Destination
@Composable
fun people(nav:DestinationsNavigator) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .height(70.dp)
            .border(width = 0.5.dp, color = Color.LightGray)
            , horizontalArrangement =Arrangement.SpaceBetween
            , verticalAlignment = Alignment.CenterVertically){
            IconButton(onClick = { /*TODO*/ }){
                Icon(imageVector = Icons.Default.Search, contentDescription =null)
            }
            Text(text = "Home", fontSize = 25.sp, color = Color.Black)
            Image(painter =painterResource(id = R.drawable.group_3454), modifier = Modifier
                .size(50.dp)
                .padding(end = 12.dp), contentDescription = null)
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 70.dp)
                .fillMaxWidth()
                .height(80.dp)
                .clickable { nav.navigate(ChatDestination) },
//                .border(width = 1.dp, color = Color.Gray),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
          Image(
                    painter = painterResource(id = R.drawable.caty),
                    contentDescription = null,
              modifier = Modifier
                  .size(70.dp)
                  .padding(start = 15.dp)

                )
            Text(text = "Alberto Moedano", fontSize = 25.sp,

                color = Color.Black,
            modifier = Modifier.padding(end=90.dp)
                , fontFamily = FontFamily.Cursive)

            Image(
                painter = painterResource(id = R.drawable.min),
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 12.dp)

            )
        }

        }
    }

@Destination
@Composable
fun Chat(nav:DestinationsNavigator) {
    var db: FirebaseDatabase;
    var ref: DatabaseReference;
    var viewModel = ChatViewModel()
    lateinit var auth: FirebaseAuth
    auth = FirebaseAuth.getInstance()

    var mess by remember {
        mutableStateOf("")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(80.dp)
                .border(width = 1.dp, color = Color.Gray),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
            Text(text = "Ghaidaa Basha-agha", fontSize = 25.sp, color = Color.Black)
            Image(
                imageVector = Icons.Default.Call, modifier = Modifier
                    .size(50.dp)
                    .padding(end = 12.dp), contentDescription = null
            )
        }



    LazyColumn(modifier = Modifier .padding(vertical =  90.dp)) {
        var vm = viewModel.messeges.value
        items(vm) { item ->

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween

            ) {
                if(viewModel.myEmail==item.email) {
                    Card(
                        modifier = Modifier.padding(15.dp)
                            .align(Alignment.End),
                        RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomStart = 15.dp),
                        backgroundColor = Orange,
                    ) {
                        Text(
                            text = item.text, fontSize = 25.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }else{
                    Card(
                        modifier = Modifier.padding(15.dp),
                        RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomEnd = 15.dp),
                        backgroundColor = Color.LightGray,
                    ) {
                        Text(
                            text = item.text, fontSize = 25.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }
    }

        Row(
            modifier = Modifier.align(Alignment.BottomCenter)

        ) {
            TextField(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                value = mess,
                onValueChange = { mess = it }
            )
        }
        Button(modifier = Modifier.align(Alignment.BottomEnd).width(50.dp).clip(CircleShape).padding(bottom = 2.dp),
            onClick = {
            viewModel.sendMessage(viewModel.myEmail, mess)
        }) {
            Icon(imageVector = Icons.Default.Send, contentDescription =null )
        }
}}