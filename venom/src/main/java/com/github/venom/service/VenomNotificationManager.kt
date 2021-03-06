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
import com.github.venom.service.VenomService.Companion.ACTION_CANCEL
import com.github.venom.service.VenomService.Companion.ACTION_KILL

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
            .addAction(createAction(ACTION_CANCEL, config.buttonCancel))
            .addAction(createAction(ACTION_KILL, config.buttonKill))
            .build()
    }

    private fun createAction(action: String, text: String): Action {
        val intent = Intent(context, VenomService::class.java).setAction(action)
        val pendingIntent = PendingIntent.getService(
            context,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        return Action(0, text, pendingIntent)
    }

    companion object {
        private const val VENOM_NOTIFICATION_CHANNEL_ID = "venom_notification_channel"
        private const val VENOM_NOTIFICATION_CHANNEL_NAME = "Venom"
    }
}
