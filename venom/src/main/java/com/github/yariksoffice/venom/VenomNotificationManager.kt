package com.github.yariksoffice.venom

import android.app.*
import android.app.Service.NOTIFICATION_SERVICE
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Action
import androidx.core.content.ContextCompat
import com.github.yariksoffice.venom.VenomService.Companion.ACTION_CANCEL
import com.github.yariksoffice.venom.VenomService.Companion.ACTION_KILL

internal class VenomNotificationManager(private val context: Context) {

    fun verifyNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(
            VENOM_NOTIFICATION_CHANNEL_ID,
            VENOM_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        val nm = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(channel)
    }

    fun createNotification(): Notification {
        val cancelAction = createAction(ACTION_CANCEL, R.string.venom_notification_button_cancel)
        val killAction = createAction(ACTION_KILL, R.string.venom_notification_button_kill)
        return NotificationCompat.Builder(context, VENOM_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.android_adb)
            .setContentTitle(context.getString(R.string.venom_foreground_service_title))
            .setColor(ContextCompat.getColor(context, R.color.venom_primary))
            .setContentText(context.getString(R.string.venom_foreground_service_text))
            .addAction(cancelAction)
            .addAction(killAction)
            .build()
    }

    private fun createAction(action: String, text: Int): Action {
        val intent = Intent(context, VenomService::class.java).setAction(action)
        val pendingIntent = PendingIntent.getService(
            context, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        return Action(
            R.drawable.android_adb,
            context.getString(text),
            pendingIntent
        )
    }

    companion object {
        private const val VENOM_NOTIFICATION_CHANNEL_ID = "venom_notification_channel"
        private const val VENOM_NOTIFICATION_CHANNEL_NAME = "Venom"
    }
}