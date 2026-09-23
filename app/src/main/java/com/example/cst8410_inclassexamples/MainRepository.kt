@file:Suppress("DEPRECATION")

package com.example.cst8410_inclassexamples

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class MainRepository(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secret_shared_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var firstName: String = sharedPreferences.getString("first_name", "Eric") ?: "Eric"
        private set

    fun setFirstName(name: String) {
        this.firstName = name
        sharedPreferences.edit {
            putString("first_name", name)
        }
    }

    fun getData(): String {
        return firstName
    }
}
