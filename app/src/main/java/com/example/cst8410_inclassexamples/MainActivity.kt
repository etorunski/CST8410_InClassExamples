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
    //Step 1
    private var bluetoothManager : BluetoothManager? = null
    private var bluetoothAdapter: BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        if(bluetoothManager != null){
            bluetoothAdapter = bluetoothManager?.getAdapter()
        }

        //Step 2
        val scanner = bluetoothAdapter.bluetoothLeScanner

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // Fast scanning mode
            .setReportDelay(0) // Notify devices immediately
            .build()

        val filters = listOf(
            ScanFilter.Builder()
                //.setDeviceName("MyBLEDevice") // Filter by device name
                //.setServiceUuid(ParcelUuid(UUID.fromString("0000180D-0000-1000-8000-00805F9B34FB"))) // Filter by specific service, this should match the advertising UUID from the Server
                .build()
        )

        //This function gets called every time a new device is detected:
        val scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                super.onScanResult(callbackType, result)
                result?.let {
                    val device = it.device
                    val deviceName = device.name
                    val deviceAddress = device.address
                    Log.d("BLE", "Device found: $deviceName ($deviceAddress)")
                }
            }
            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
                //TODO: Something went wrong
            }
        }
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                    isGranted    ->
                if (isGranted.values.all { it == true}) {

                    ///Step 4
                    isScanning = true
                    scanner?.startScan(filters, settings, scanCallback)

                    ///////////////end of Step 4
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }

        //This code launches the permission dialog. If the user clicks "OK", then line 84 runs
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) //SDK 31 or more
        {
            requestPermissionLauncher.launch(
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN))
        }
        else //SDK 30 or less
        {
            requestPermissionLauncher.launch(
                arrayOf(Manifest.permission.BLUETOOTH))
        }

        enableEdgeToEdge()
        setContent {

            CST8410_InClassExamplesTheme(content=
            {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Eric",
                        modifier = Modifier.padding(innerPadding) ,
                    ){ str:String -> str.lowercase() }
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
          fontSize = 40.sp, modifier = modifier )

      Button(onClick={ }, ){ Text("CLick me") }

      Button(onClick={ }, ){
          Image( painter=painterResource(R.drawable.beach ), contentDescription = "Beach",
              modifier=Modifier.fillMaxSize(0.25f)
          )
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