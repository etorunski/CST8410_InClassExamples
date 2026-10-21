package com.example.cst8410_inclassexamples


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.cst8410_inclassexamples.ui.theme.CST8410_InClassExamplesTheme
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.android.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Serializable
data class LoginRequest(
    @SerialName("loginName")
    var loginName: String?,
    @SerialName("password")
    var password: String?
)

class MainActivity : ComponentActivity() {




    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val client = HttpClient(Android){
            install(ContentNegotiation) {
                json()
            }
        }

        Log.d(TAG, "onCreate() called")

        enableEdgeToEdge()
        setContent {

        CST8410_InClassExamplesTheme(content=
        {
            Scaffold(modifier = Modifier.fillMaxSize(),
                contentWindowInsets =WindowInsets.safeDrawing)
                { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding), client)
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
}


@Composable
fun DisplayText(
    onLogin:(lname:String, password:String) -> Unit,
    modifier: Modifier = Modifier
) {
    var loginName = remember{ mutableStateOf("")}
    var password = remember{ mutableStateOf("")}
    Column {

        TextField(
            value = loginName.value,
            onValueChange = { newValue -> loginName.value = newValue })
        TextField(
            value = password.value,
            onValueChange = { newValue -> password.value = newValue })
        Button(onClick = {
            onLogin(loginName.value, password.value)

        }){
            Text("Login")
        }
    }


}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    client: HttpClient
) {
    //This variable is the Stack of NavKeys:
    val backStack = rememberNavBackStack(HomeRoute)

    //This Widget takes care of rendering the current page at the top of the stack:
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        modifier = modifier,
        entryProvider = { key -> //key is a navkey in your app

            //When is the same as switch / case statements:
            when (key) { // switch
                is HomeRoute -> NavEntry(key) { //case, is ___ ->  XXXX
                    DisplayText(
                        onLogin = { lname, password ->
                            CoroutineScope(Dispatchers.IO).launch {
                                //Do you stuff here in a background thread.
                                try {
                                    val response: HttpResponse = client.post("http://10.70.98.74:8080/firstTest")
                                    {
                                        contentType(ContentType.Application.Json)
                                        setBody(LoginRequest("Jet", "Brains"))
                                    }
                                    val body =  response.body<String>()
                                    println(response.status)
                                }
                                catch(e:Exception)
                                {
                                    println(e.message)
                                }

                            }

                        }
                    )
                }

                is SecondRoute -> NavEntry(key) {
                    DisplayText(
                        onLogin = { lname, password ->  } )
                }
                else -> error("Unknown route: $key")
            }
        }
    )
}

@Serializable
data object HomeRoute : NavKey
@Serializable
data object SecondRoute : NavKey
