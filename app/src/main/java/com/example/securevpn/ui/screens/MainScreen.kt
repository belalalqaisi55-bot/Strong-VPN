package com.example.securevpn.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.securevpn.R
import com.example.securevpn.ui.MainViewModel
import com.example.securevpn.ui.DiagnosticsComponent

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.VpnService
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings

// ... existing code ...

// ... (imports remain)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel, onNavigateToSettings: () -> Unit) {
    val isConnected by viewModel.isConnected.collectAsState()
    val killSwitchEnabled by viewModel.killSwitchEnabled.collectAsState()
    val uploadSpeed by viewModel.uploadSpeed.collectAsState()
    val downloadSpeed by viewModel.downloadSpeed.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val servers = listOf("Server 1 (US)", "Server 2 (UK)", "Server 3 (JP)")
    val selectedServer by viewModel.selectedServer.collectAsState()
    val context = LocalContext.current

    val vpnPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            viewModel.toggleConnection()
            val startIntent = Intent(context, com.example.securevpn.vpn.SecureVpnService::class.java)
            startIntent.putExtra("SERVER", selectedServer)
            context.startService(startIntent)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Secure VPN") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ... (rest of the code)
            Text(text = if (isConnected) stringResource(R.string.connected) else stringResource(R.string.disconnected), style = MaterialTheme.typography.headlineMedium)
            
            if (isConnected) {
                NetworkSpeedIndicator(uploadSpeed = uploadSpeed, downloadSpeed = downloadSpeed)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = selectedServer,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.server_selection)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    servers.forEach { server ->
                        DropdownMenuItem(
                            text = { Text(server) },
                            onClick = {
                                viewModel.setSelectedServer(server)
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Spacer(modifier = Modifier.height(16.dp))
            
            VpnToggleButton(
                isConnected = isConnected,
                onToggle = {
                    if (!isConnected) {
                        val intent = VpnService.prepare(context)
                        if (intent != null) {
                            vpnPermissionLauncher.launch(intent)
                        } else {
                            viewModel.toggleConnection()
                            val startIntent = Intent(context, com.example.securevpn.vpn.SecureVpnService::class.java)
                            startIntent.putExtra("SERVER", selectedServer)
                            context.startService(startIntent)
                        }
                    } else {
                        viewModel.toggleConnection()
                        val stopIntent = Intent(context, com.example.securevpn.vpn.SecureVpnService::class.java)
                        context.stopService(stopIntent)
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.kill_switch))
                Switch(
                    checked = killSwitchEnabled,
                    onCheckedChange = { viewModel.toggleKillSwitch() }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            DiagnosticsComponent()
        }
    }
}

@Composable
fun NetworkSpeedIndicator(uploadSpeed: Long, downloadSpeed: Long) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SpeedItem(icon = Icons.Filled.KeyboardArrowDown, speed = downloadSpeed, label = "Download")
        SpeedItem(icon = Icons.Filled.KeyboardArrowUp, speed = uploadSpeed, label = "Upload")
    }
}

@Composable
fun SpeedItem(icon: androidx.compose.ui.graphics.vector.ImageVector, speed: Long, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null)
        Text(text = "$speed KB/s", style = MaterialTheme.typography.bodyMedium)
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun VpnToggleButton(isConnected: Boolean, onToggle: () -> Unit) {
    FilledIconToggleButton(
        checked = isConnected,
        onCheckedChange = { onToggle() },
        modifier = Modifier.size(80.dp)
    ) {
        Icon(
            imageVector = if (isConnected) Icons.Filled.Lock else Icons.Filled.Lock,
            contentDescription = if (isConnected) "Connected" else "Disconnected",
            modifier = Modifier.size(40.dp)
        )
    }
}

