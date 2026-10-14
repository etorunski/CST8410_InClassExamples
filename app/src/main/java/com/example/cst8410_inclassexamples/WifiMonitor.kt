package com.example.cst8410_inclassexamples

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

sealed class WifiState {
    data object Enabled : WifiState()
    data object Disabled : WifiState()
}

/**
 * Returns a Flow indicating whether Wi-Fi is enabled on the device.
 */
fun Context.getWifiStateFlow(): Flow<WifiState> = callbackFlow {
    val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager

    fun currentState(): WifiState {
        return if (wifiManager?.isWifiEnabled == true) WifiState.Enabled else WifiState.Disabled
    }

    // Emit initial state
    trySend(currentState())

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == WifiManager.WIFI_STATE_CHANGED_ACTION) {
                trySend(currentState())
            }
        }
    }

    val filter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
    registerReceiver(receiver, filter)

    awaitClose {
        unregisterReceiver(receiver)
    }
}

/**
 * Returns a Flow indicating whether a Wi-Fi network connection is active.
 */
fun Context.getWifiNetworkFlow(): Flow<Boolean> = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            trySend(true)
        }

        override fun onLost(network: Network) {
            trySend(false)
        }

        override fun onUnavailable() {
            trySend(false)
        }
    }

    val request = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    connectivityManager.registerNetworkCallback(request, networkCallback)

    awaitClose {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
