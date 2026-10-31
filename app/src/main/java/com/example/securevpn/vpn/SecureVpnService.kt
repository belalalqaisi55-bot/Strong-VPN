package com.example.securevpn.vpn

import android.net.VpnService

import android.content.Intent
import android.os.ParcelFileDescriptor

import android.util.Log

class SecureVpnService : VpnService() {
    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            val server = intent?.getStringExtra("SERVER") ?: "Unknown"
            val builder = Builder()
            builder.setSession("SecureVPN - $server")
                .addAddress("10.0.0.2", 24)
                .addDnsServer("8.8.8.8")
                .addRoute("0.0.0.0", 0)
                .setMtu(1500)
            
            vpnInterface = builder.establish()
        } catch (e: Exception) {
            Log.e("SecureVpnService", "Failed to establish VPN", e)
        }
        
        return START_STICKY
    }

    override fun onDestroy() {
        vpnInterface?.close()
        super.onDestroy()
    }
}
