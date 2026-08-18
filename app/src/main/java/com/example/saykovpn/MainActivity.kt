package com.example.saykovpn

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var statusText: TextView
    private lateinit var connectButton: Button
    private lateinit var disconnectButton: Button

    companion object {
        private const val VPN_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        connectButton = findViewById(R.id.connectButton)
        disconnectButton = findViewById(R.id.disconnectButton)

        connectButton.setOnClickListener {
            prepareVpn()
        }

        disconnectButton.setOnClickListener {
            stopVpn()
        }
    }

    private fun prepareVpn() {
        val permissionIntent = VpnService.prepare(this)

        if (permissionIntent != null) {
            startActivityForResult(
                permissionIntent,
                VPN_REQUEST_CODE
            )
        } else {
            startVpn()
        }
    }

    private fun startVpn() {
        val intent = Intent(this, PugVpnService::class.java)
        startService(intent)

        statusText.text = "Статус: VPN подключен"
        connectButton.isEnabled = false
        disconnectButton.isEnabled = true
    }

    private fun stopVpn() {
        val intent = Intent(this, PugVpnService::class.java)
        stopService(intent)

        statusText.text = "Статус: отключено"
        connectButton.isEnabled = true
        disconnectButton.isEnabled = false
    }

    @Deprecated("Deprecated in Android API 30")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == VPN_REQUEST_CODE &&
            resultCode == RESULT_OK
        ) {
            startVpn()
        }
    }
}