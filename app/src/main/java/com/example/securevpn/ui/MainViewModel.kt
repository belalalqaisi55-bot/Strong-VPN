package com.example.securevpn.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.net.TrafficStats
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel() {
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private val _killSwitchEnabled = MutableStateFlow(false)
    val killSwitchEnabled: StateFlow<Boolean> = _killSwitchEnabled

    private val _selectedServer = MutableStateFlow("Server 1 (US)")
    val selectedServer: StateFlow<String> = _selectedServer

    fun setSelectedServer(server: String) {
        _selectedServer.value = server
    }

    private val _uploadSpeed = MutableStateFlow(0L)
    val uploadSpeed: StateFlow<Long> = _uploadSpeed

    private val _downloadSpeed = MutableStateFlow(0L)
    val downloadSpeed: StateFlow<Long> = _downloadSpeed

    init {
        startTrafficMonitoring()
    }

    private fun startTrafficMonitoring() {
        viewModelScope.launch(Dispatchers.IO) {
            var lastRx = TrafficStats.getTotalRxBytes()
            var lastTx = TrafficStats.getTotalTxBytes()
            while (true) {
                delay(1000)
                val currentRx = TrafficStats.getTotalRxBytes()
                val currentTx = TrafficStats.getTotalTxBytes()
                _downloadSpeed.value = (currentRx - lastRx) / 1024 // KB/s
                _uploadSpeed.value = (currentTx - lastTx) / 1024 // KB/s
                lastRx = currentRx
                lastTx = currentTx
            }
        }
    }

    fun toggleConnection() {
        _isConnected.value = !_isConnected.value
    }

    fun toggleKillSwitch() {
        _killSwitchEnabled.value = !_killSwitchEnabled.value
    }
}
