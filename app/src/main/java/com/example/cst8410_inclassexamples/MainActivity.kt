package com.example.cst8410_inclassexamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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

data class ShoppingItem(var name:String, var sel:Boolean)
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
                    )
                }
            })
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    val contxt = LocalActivity.current
    val widthSizeClass = calculateWindowSizeClass(contxt!!).widthSizeClass
    var selectedItem = remember { mutableStateOf<ShoppingItem?>(null)}

    //this holds the string that the user is typing in
    var newItem = rememberSaveable { mutableStateOf("")}

    //This holds the ArrayList to be shown in the LazyColumn. It should be mutableStateListOf to redraw when the items in the ArrayList change
    val items = remember { mutableStateListOf<ShoppingItem>() }

    val isTablet = widthSizeClass == WindowWidthSizeClass.Expanded

    var rowWidth =   if(!isTablet) 1.0f else 0.3f;

    //always show the LazyColumn if
    // you're a tablet. If you're a phone, just show when nothing is selected
    if(isTablet or (selectedItem.value == null))
    {
        Row(modifier=modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth(rowWidth)) {
                //Have a Row at the top for the button and TextField
                Row {
                    TextField(
                        value = newItem.value,
                        onValueChange = { newStr -> newItem.value = newStr })
                    Button(onClick = {
                        items.add(
                            ShoppingItem(
                                newItem.value,
                                false
                            )
                        ) //What are you adding when you click the button?
                        newItem.value = ""
                    }) {
                        Text("Add item")
                    }
                }
                //Now have a dynamic-size column that grows as items are added in the ArrayList
                LazyColumn {

                    //This generates a for-loop from 0 to items.size, and passes in index as the counter variable
                    items(items.size) { index ->
                        //return a row that is full-width, centered vertically, and space between the two items on each row:
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .clickable(onClick = { selectedItem.value = items[index] }),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            //The shopping item's name
                            Text(text = "Item: ${items[index].name}")
                            //A checkbox showing if an item is selected or now
                            Checkbox(checked = items[index].sel,
                                //when the user clicks on the checkbox, change the object at that row to trigger a recomposition
                                onCheckedChange = { newVal ->
                                    items[index] = items[index].copy(sel = newVal)
                                })
                        }
                    }
                }
            }
            //for when it's a tablet and selectedItem != null
            selectedItem.value?.let{
                Column(modifier = Modifier.fillMaxWidth(1.0f-rowWidth)){
                    ItemDetails(selectedItem, modifier) //This shows the details page on the right side
                }
            }
        }
    }
    else //there's an item selected
    {
        ItemDetails(selectedItem, modifier) //This shows the details page on the whole page
    }
}

@Composable
fun ItemDetails(selectedItem: MutableState<ShoppingItem?>, modifier: Modifier = Modifier){
    Box(modifier = modifier.fillMaxSize()) {
        Column {
            Text(selectedItem.value!!.name)
            Text(selectedItem.value!!.sel.toString())
        }
        Button(modifier=Modifier.align(Alignment.BottomStart),  onClick = { selectedItem.value = null }) {
            Text("Hide")
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