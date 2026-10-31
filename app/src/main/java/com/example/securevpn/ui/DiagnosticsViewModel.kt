package com.example.securevpn.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.securevpn.network.NetworkClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DiagnosticsState(
    val ip: String = "Loading...",
    val isLoading: Boolean = false,
    val error: String? = null
)

class DiagnosticsViewModel : ViewModel() {
    private val _state = MutableStateFlow(DiagnosticsState())
    val state: StateFlow<DiagnosticsState> = _state

    init {
        fetchPublicIp()
    }

    fun fetchPublicIp() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val response = NetworkClient.api.getPublicIp()
                _state.value = _state.value.copy(ip = response.ip, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false, ip = "Failed to load")
            }
        }
    }
}
