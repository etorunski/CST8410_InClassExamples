package com.example.cst8410_inclassexamples


import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.net.wifi.WifiManager
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
import android.hardware.SensorManager

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
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

class MainActivity : ComponentActivity() {

    fun isWifiSettingOn(context: Context): Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
        // as means casting

        return wifiManager?.isWifiEnabled ?: false

        // ? is a null check. If it's not null, do what comes after ?
        // ?: is what to do in case it is null:   (obj)?.[ obj not null ] ?: [ if obj is null ]
        // ?: is called the "Elvis" operator

    }

    fun openWifiPanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val intent = Intent(Settings.Panel.ACTION_WIFI)
            startActivity(intent)
        } else {
            // Fallback for older Android versions
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }
    }

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        Log.d(TAG, "onCreate() called")

        enableEdgeToEdge()
        setContent {

            val currentLight = remember { mutableFloatStateOf(0.0f) }

            val lightSensorListener = object: SensorEventListener {
                override fun onAccuracyChanged(
                    sensor: Sensor?,
                    accuracy: Int
                ) {

                }
                override fun onSensorChanged(event: SensorEvent) { //array of the readings, 1 or 3 dimension
                    //this is state, so cause a recomposition:
                    currentLight.floatValue = event.values[0]
                }
            }

            sensorManager.registerListener(lightSensorListener, lightSensor, SensorManager.SENSOR_DELAY_UI)


        CST8410_InClassExamplesTheme(content=
        {
            Scaffold(modifier = Modifier.fillMaxSize(),
                contentWindowInsets =WindowInsets.safeDrawing)

                { innerPadding ->
                    AppNavigation( currentLight, modifier = Modifier.padding(innerPadding))
                    {

                    }
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
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    //initially showing, but changing it will redraw:
    var isShowingDialog = remember {mutableStateOf(true)}
    var agreeCollectData = remember{mutableStateOf(false) }
    Column {
        Button(onClick = {
           onClicked()

        }, modifier = modifier){
            Text("CLick for wifi check")
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun FeatureThatRequiresCameraPermission() {

    // Multiple permissions state
    val multiplePermissionsState = rememberMultiplePermissionsState(

        //  List the permissions you need here:
        permissions = listOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
        )
    )

    //If they have have all been granted:
    if (multiplePermissionsState.allPermissionsGranted) {
        Text("Camera and Audio permissions Granted")
    } else {
        Column {

            // Should Show Rationale is the case when the user has rejected the permissions twice.
            //Android will quietly not launch the permissions window even though you call launchMultiplePermissionRequest()
            val textToShow = if (multiplePermissionsState.shouldShowRationale) {
                "The camera and audio permissions are important for this app. Please grant the permissions."
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Camera and audio permissions required for this feature to be available. " +
                        "Please grant the permissions"
            }
            Text(textToShow)
            Button(onClick = { multiplePermissionsState.launchMultiplePermissionRequest() }) {
                Text("Request permissions")
            }
        }
    }
}

@Preview
@Composable
fun previewDisplayLighting()
{
    val lightLevel =remember{ mutableFloatStateOf(1.05f)}
    DisplayLighting(lightLevel, Modifier)
}
@Composable
fun DisplayLighting(lightingLevel: MutableFloatState, modifier: Modifier = Modifier)
{
    val contxt = LocalContext.current
    val isWifiConnected by contxt.getWifiStateFlow().collectAsStateWithLifecycle(initialValue = false)
    val wifiOn = if (isWifiConnected == WifiState.Enabled) "On" else "Off"
    Column {
        Text("WIfi is: $wifiOn")
        Text("The light is ${lightingLevel.floatValue}", lineHeight = 50.sp, fontSize = 50.sp)
    }
}


@Composable
fun AppNavigation(
    lightValue : MutableFloatState,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
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
                    DisplayLighting(lightValue, modifier)
                }

                is SecondRoute -> NavEntry(key) {
                    DisplayText(
                        onClicked
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
