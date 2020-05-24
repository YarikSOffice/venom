package com.github.venom.service

import android.app.ActivityManager
import android.content.Context
import android.content.Intent

internal class ServiceDelegate(private val context: Context) {

    fun startService() {
        if (!isServiceRunning()) {
            context.startService(Intent(context, VenomService::class.java))
        }
    }

    fun stopService() {
        context.stopService(Intent(context, VenomService::class.java))
    }

    @Suppress("DEPRECATION")
    fun isServiceRunning(): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return manager.getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == VenomService::class.java.name }
    }
}
