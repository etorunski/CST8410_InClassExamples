package com.example.cst8410_inclassexamples


import android.os.Bundle
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.cst8410_inclassexamples.ui.theme.CST8410_InClassExamplesTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {

        CST8410_InClassExamplesTheme(content=
        {
            Scaffold(modifier = Modifier.fillMaxSize(),
                contentWindowInsets =WindowInsets.safeDrawing)
                { innerPadding ->
                    Greeting( name= "Eric", Modifier.padding(innerPadding))
                }
            })
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier)
{
  Column(verticalArrangement = Arrangement.SpaceAround,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = modifier.fillMaxSize()
      )
  {
      Button(onClick = {  }){
          Text("Click me!!")
      }
  }
}

