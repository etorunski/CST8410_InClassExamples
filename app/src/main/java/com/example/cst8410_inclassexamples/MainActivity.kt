package com.example.cst8410_inclassexamples

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.ParcelUuid
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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

    private var bluetoothManager : BluetoothManager? = null
    private var bluetoothAdapter: BluetoothAdapter? = null
    fun printName(s1:String = "Hello", s2:String="World", stringModifier : (String)->String = { str:String -> str }):String
    {
        return stringModifier("String1 is $s1 and String2 is $s2")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        if(bluetoothManager != null){
            bluetoothAdapter = bluetoothManager?.getAdapter()
        }

        if(bluetoothAdapter != null){
            val scanner = bluetoothAdapter!!.bluetoothLeScanner
            val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // Fast scanning mode
                .setReportDelay(0) // Notify devices immediately
                .build()
//use this to find all devices:
            val noFilters = listOf<ScanFilter>()

var isConnecting = false

            val filters = listOf(
                ScanFilter.Builder()
                    .setDeviceName("Pixel 9 Pro XL") // Filter by device name
                    .setServiceUuid(ParcelUuid.fromString("0000180D-0000-1000-8000-00805F9B34FB")) // Filter by specific service, this should match the advertising UUID from the Server
                    .build()
            )
            //This function gets called every time a new device is detected:
            val scanCallback = object : ScanCallback() {

                //we heard a bluetooth Advertisement:
                override fun onScanResult(callbackType: Int, result: ScanResult?) {
                    super.onScanResult(callbackType, result)
                    result?.let {
                        val device = it.device
                        val deviceName = device.name
                        val deviceAddress = device.address
                        Log.d("BLE", "Device found: $deviceName ($deviceAddress)")

                        if(isConnecting == false)
                        {
                            isConnecting = true

                            val connectCallback = object: BluetoothGattCallback(){
                                override fun onConnectionStateChange(
                                    gatt: BluetoothGatt?,
                                    status: Int,
                                    newState: Int
                                ) {
                                    super.onConnectionStateChange(gatt, status, newState)

                                    if(newState == BluetoothGatt.STATE_CONNECTING)
                                    {
                                        Log.d("Bluetooth", "CONNECTING")
                                    }
                                    else if(newState == BluetoothGatt.STATE_CONNECTED)
                                    {
                                        Log.d("Bluetooth", "CONNECTED")
                                    }
                                    else if(newState == BluetoothGatt.STATE_DISCONNECTING)
                                    {
                                        Log.d("Bluetooth", "DISCONNECTING")
                                    }else if(newState == BluetoothGatt.STATE_DISCONNECTED)
                                    {
                                        Log.d("Bluetooth", "DISCONNECTED")
                                    }
                                }
                            }
                            //Step 6
                            val server = device.connectGatt(this@MainActivity, false, connectCallback) //the second parameter is autoConnect. false means connect now, true means wait until it's available
                            //on the server, this should call the server's gattCallback onDeviceConnected() callback
                        }
                    }
                }
                //bluetooth didn't initialize:
                override fun onScanFailed(errorCode: Int) {
                    super.onScanFailed(errorCode)
                    //TODO: Something went wrong
                }
            }
            val requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
                {   //This is a lambda function that gets called after the user clicks "allow" or "do not allow"
                        isGranted    ->
                    if (isGranted.values.all { it == true}) {
                        ///Step 4 The user accepted all permissions

                        scanner?.startScan(noFilters, settings, scanCallback)

                        ///////////////end of Step 4
                    } else {
                        // The user rejected the permissions. Show an alert dialog to explain
                        // to the user that the feature is unavailable because the
                        // feature requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }
                }




            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) //SDK 31 or higher
            {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN))
                // the next line of code is the lambda function above
            }
            else //SDK 30 or less
            {
                requestPermissionLauncher.launch(
                    arrayOf(Manifest.permission.BLUETOOTH))
                // the next line of code is the lambda function above
            }

            //start scannig for bluetooth devices:
           // scanner?.startScan(noFilters, settings, scanCallback)
        }

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

     }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CST8410_InClassExamplesTheme {
        Greeting("Android")
    }
}