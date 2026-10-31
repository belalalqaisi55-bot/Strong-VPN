package com.example.securevpn.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.securevpn.ui.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val selectedProtocol by viewModel.protocol.collectAsState()
    val protocols = listOf("WireGuard", "OpenVPN")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(text = "Connection Protocol", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            
            protocols.forEach { protocol ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (protocol == selectedProtocol),
                            onClick = { viewModel.updateProtocol(protocol) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (protocol == selectedProtocol),
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = protocol, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
