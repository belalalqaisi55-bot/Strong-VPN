package com.example.securevpn.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {
    companion object {
        val PROTOCOL_KEY = stringPreferencesKey("protocol")
    }

    val protocolFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PROTOCOL_KEY] ?: "WireGuard"
    }

    suspend fun saveProtocol(protocol: String) {
        context.dataStore.edit { preferences ->
            preferences[PROTOCOL_KEY] = protocol
        }
    }
}
