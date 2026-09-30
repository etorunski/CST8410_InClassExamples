package com.example.cst8410_inclassexamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.cst8410_inclassexamples.ui.theme.CST8410_InClassExamplesTheme
import java.lang.Character.toUpperCase
import java.util.Locale
import java.util.Locale.getDefault

class MainActivity : ComponentActivity() {

    fun printName(s1:String = "Hello", s2:String="World", stringModifier : (String)->String = { str:String -> str }):String
    {
        return stringModifier("String1 is $s1 and String2 is $s2")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {

            CST8410_InClassExamplesTheme(content=
            {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = printName{ str:String -> str.lowercase(getDefault()) },
                        modifier = Modifier.padding(innerPadding) ,

                    ){ str:String -> str.lowercase()   }
                }
            })
        }
    }
}

@Composable
fun Greeting(name: String,
             modifier: Modifier = Modifier,
             stringChanger:(String) -> String = { str:String -> str })
{
  Column(verticalArrangement = Arrangement.SpaceAround) {

      Text(text = stringChanger(stringResource(R.string.hello_message)  ),
          fontSize = 40.sp,    modifier = modifier )

      Image( painter=painterResource(R.drawable.beach ), contentDescription = "Beach",
          modifier=Modifier.fillMaxSize(0.25f)
      )//contentDescription is for screen reader software

        Button(onClick={ }, ){
            Text("CLick me")
        }


      Button(onClick={ }, ){
          Image( painter=painterResource(R.drawable.beach ), contentDescription = "Beach",
              modifier=Modifier.fillMaxSize(0.25f)
          )//
      }
     }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CST8410_InClassExamplesTheme {
        Greeting("Android")
    }
}