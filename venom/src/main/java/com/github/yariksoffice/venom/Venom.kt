package com.github.yariksoffice.venom

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import java.lang.IllegalStateException

class Venom private constructor(
    private val context: Context,
    private val prefs: VenomPreferenceManager
) {

    fun initialize() {
        if (prefs.isActive()) {
            start()
        }
    }

    fun start() {
        prefs.setActive(true)
        if (!isVenomServiceRunning()) {
            context.startService(Intent(context, VenomService::class.java))
        }
    }

    fun stop() {
        prefs.setActive(false)
        context.stopService(Intent(context, VenomService::class.java))
    }

    fun isRunning(): Boolean {
        return prefs.isActive() && isVenomServiceRunning()
    }

    @Suppress("DEPRECATION")
    private fun isVenomServiceRunning(): Boolean {
        val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        return manager.getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == VenomService::class.java.name }
    }

    companion object {

        private var instance: Venom? = null

        fun createInstance(context: Context): Venom {
            return Venom(context, VenomPreferenceManager(context))
        }

        fun setGlobalInstance(venom: Venom) {
            if (instance != null) {
                throw IllegalStateException("Global instance is already initialized")
            }
            instance = venom
        }

        fun getGlobalInstance(): Venom {
            return instance ?: throw IllegalStateException("Global instance is not initialized")
        }
    }
}