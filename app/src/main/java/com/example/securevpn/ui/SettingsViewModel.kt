package com.example.securevpn.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.securevpn.data.SettingsManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsManager = SettingsManager(application)
    val protocol = settingsManager.protocolFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "WireGuard"
    )

    fun updateProtocol(newProtocol: String) {
        viewModelScope.launch {
            settingsManager.saveProtocol(newProtocol)
        }
    }
}
