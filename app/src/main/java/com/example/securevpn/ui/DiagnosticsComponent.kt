package com.example.securevpn.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DiagnosticsComponent(viewModel: DiagnosticsViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Network Diagnostics", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Public IP: ${state.ip}")
            Spacer(modifier = Modifier.height(8.dp))
            if (state.isLoading) {
                CircularProgressIndicator()
            }
            if (state.error != null) {
                Text(text = "Error: ${state.error}", color = MaterialTheme.colorScheme.error)
            }
            Button(onClick = { viewModel.fetchPublicIp() }) {
                Text("Refresh")
            }
        }
    }
}
