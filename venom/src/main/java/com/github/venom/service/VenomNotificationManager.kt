/*
 * The MIT License (MIT)
 *
 * Copyright 2020 Yaroslav Berezanskyi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.venom.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service.NOTIFICATION_SERVICE
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Action
import androidx.core.content.ContextCompat
import com.github.venom.DeathActivity
import com.github.venom.DeathActivity.LaunchMode
import com.github.venom.DeathActivity.LaunchMode.KILL
import com.github.venom.DeathActivity.LaunchMode.RESTART
import com.github.venom.service.VenomService.Companion.ACTION_CANCEL

private const val ACTIVITY_KILL_CODE = 100
private const val ACTIVITY_RESTART_CODE = 200
private const val SERVICE_CANCEL_CODE = 300
private const val VENOM_NOTIFICATION_CHANNEL_ID = "venom_notification_channel"
private const val VENOM_NOTIFICATION_CHANNEL_NAME = "Venom"

internal class VenomNotificationManager(private val context: Context) {

    var config: NotificationConfig = defaultNotification(context)

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
        return NotificationCompat.Builder(context, VENOM_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(config.title)
            .setContentText(config.text)
            .setSmallIcon(config.iconRes)
            .setColor(ContextCompat.getColor(context, config.colorRes))
            .addAction(createActivityAction(config.buttonKill, KILL, ACTIVITY_KILL_CODE))
            .addAction(createActivityAction(config.buttonRestart, RESTART, ACTIVITY_RESTART_CODE))
            .addAction(createCancelAction())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun createActivityAction(
        text: String,
        launchMode: LaunchMode,
        requestCode: Int
    ): Action {
        val intent = DeathActivity.createIntent(context, launchMode)
        val intentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_ONE_SHOT
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            intentFlags
        )
        return Action(0, text, pendingIntent)
    }

    private fun createCancelAction(): Action {
        val intent = Intent(context, VenomService::class.java).setAction(ACTION_CANCEL)
        val intentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_ONE_SHOT
        }
        val pendingIntent = PendingIntent.getService(
            context,
            SERVICE_CANCEL_CODE,
            intent,
            intentFlags
        )
        return Action(0, config.buttonCancel, pendingIntent)
    }
}
