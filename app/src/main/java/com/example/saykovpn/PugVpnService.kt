package com.example.saykovpn

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor

class PugVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        startVpn()

        return START_STICKY
    }

    private fun startVpn() {

        if (vpnInterface != null) {
            return
        }

        val builder = Builder()

        builder.setSession("SAYKOVPN")

        builder.addAddress("10.8.0.2", 32)

        builder.addDnsServer("1.1.1.1")
        builder.addDnsServer("8.8.8.8")

        builder.addRoute("0.0.0.0", 0)

        vpnInterface = builder.establish()
    }

    override fun onDestroy() {
        vpnInterface?.close()
        vpnInterface = null

        super.onDestroy()
    }
}