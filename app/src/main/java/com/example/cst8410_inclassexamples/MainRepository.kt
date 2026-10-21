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
        "app_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private var textValue: String = sharedPreferences.getString("saved_text", "Hello world") ?: "Hello world"

    fun getText(): String {
        return textValue
    }

    fun updateText(newText: String) {
        textValue = newText
        sharedPreferences.edit {
            putString("saved_text", newText)
        }
    }
}
