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
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.cst8410_inclassexamples.ui.theme.CST8410_InClassExamplesTheme
import kotlinx.serialization.Serializable


class MainActivity : ComponentActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate() called")

        enableEdgeToEdge()
        setContent {

        CST8410_InClassExamplesTheme(content=
        {
            Scaffold(modifier = Modifier.fillMaxSize(),
                contentWindowInsets =WindowInsets.safeDrawing)
                { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding))
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
    textStateFlow: StateFlow<String>,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val textState by textStateFlow.collectAsStateWithLifecycle()

    //initially showing, but changing it will redraw:
    var isShowingDialog = remember {mutableStateOf(true)}
    var agreeCollectData = remember{mutableStateOf(false) }
    Column {
        Text(text = "The text is now: $textState", modifier = modifier)
        TextField(
            label = { Text("Type Something here") },
            value = textState,
            onValueChange = onTextChanged
        )
    }

    if(isShowingDialog.value)
    {
        AlertDialog(
            onDismissRequest = {isShowingDialog.value = false},
            title = { Text(text = "Dialog Title") },
            text = { Text("Here is a text ") },       //This below causes a recomposition
            confirmButton = {  Button( onClick = {
                agreeCollectData.value = true
                isShowingDialog.value = false }) { Text("This is the Confirm Button")   }  },
            dismissButton = {  Button( onClick = {
                agreeCollectData.value = true
                isShowingDialog.value = false }) {Text("This is the dismiss Button")    }  }
        )
    }

}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
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
                        textStateFlow = viewModel.textState,
                        onTextChanged = viewModel::onTextChanged
                    )
                }

                is SecondRoute -> NavEntry(key) {
                    DisplayText(
                        textStateFlow = viewModel.textState,
                        onTextChanged = viewModel::onTextChanged
                    )
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
