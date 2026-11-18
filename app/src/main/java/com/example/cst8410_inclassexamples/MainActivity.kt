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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.cst8410_inclassexamples.ui.theme.CST8410_InClassExamplesTheme

import java.util.Locale.getDefault



data class ShoppingItem(var name:String, var sel:Boolean)


class MainActivity : ComponentActivity() {

    val name = "eric"
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

                    val items = remember{ mutableStateListOf<ShoppingItem>()}

                    var newItem = rememberSaveable { mutableStateOf("")}
                    Column(modifier = Modifier.padding(innerPadding)){
                        //This row is for adding new items
                        Row{
                            TextField(value = newItem.value, onValueChange = { newStr -> newItem.value = newStr })
                            Button(onClick = {
                                items.add( ShoppingItem( newItem.value, false));
                                newItem.value="" }) {
                                Text("Add item")
                            }
                        }

                        //then we draw the items in the arraylist
                        LazyColumn {

                            items(items.size) { index ->
                                Row(horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier=Modifier.fillMaxWidth()){
                                    Text(text = "Item: ${items[index].name}")
                                    Checkbox(checked = items[index].sel, onCheckedChange = {
                                        sel -> items[index] = items[index].copy(sel = sel)
                                    })
                                }
                            }
                        }
                    }


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
    var text = remember{ mutableStateOf("Hello")}
  Column(verticalArrangement = Arrangement.SpaceAround) {

      Text(text = text.value  ,
          fontSize = 40.sp,    modifier = modifier )

      Image( painter=painterResource(R.drawable.beach ), contentDescription = "Beach",
          modifier=Modifier.fillMaxSize(0.25f)
      )//contentDescription is for screen reader software

        Button(onClick={
            text.value = "You clicked on me"
        }, ){
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