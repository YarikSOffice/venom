package com.github.yariksoffice.venom

import android.app.*
import android.content.Intent
import android.os.IBinder

internal class VenomService : Service() {

    private lateinit var prefs: VenomPreferenceManager
    private lateinit var notificationManager: VenomNotificationManager

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        prefs = VenomPreferenceManager(this)
        notificationManager = VenomNotificationManager(this)
        notificationManager.verifyNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationManager.createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_CANCEL -> cancelAndTerminate()
            ACTION_KILL -> launchDeath()
        }
        return START_NOT_STICKY
    }

    private fun launchDeath() {
        DeathActivity.launch(this)
        stopSelf()
    }

    private fun cancelAndTerminate() {
        prefs.setActive(false)
        stopSelf()
    }

    companion object {
        private const val NOTIFICATION_ID = 200
        const val ACTION_KILL = "action_kill"
        const val ACTION_CANCEL = "action_cancel"
    }
}