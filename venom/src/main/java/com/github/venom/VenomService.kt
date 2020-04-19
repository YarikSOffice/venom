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

package com.github.venom

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder

internal class VenomService : Service() {

    private lateinit var prefs: VenomPreferenceManager
    private lateinit var notificationManager: VenomNotificationManager

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        val compositionRoot = CompositionRoot.getCompositionRoot(this)
        prefs = compositionRoot.preferenceManager
        notificationManager = compositionRoot.notificationManager
        notificationManager.verifyNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_CANCEL -> cancelAndTerminate()
            ACTION_KILL -> launchDeath()
            else -> intent?.extras?.let(::showNotification)
        }
        return START_NOT_STICKY
    }

    private fun showNotification(extras: Bundle) {
        val notificationResources = requireNotNull(extras.getParcelable<VenomNotification>(NOTIFICATION_RESOURCES_EXTRA))
        val notification = notificationManager.createNotification(notificationResources)
        startForeground(NOTIFICATION_ID, notification)
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
        private const val NOTIFICATION_RESOURCES_EXTRA = "NOTIFICATION_RESOURCES_EXTRA"
        const val ACTION_KILL = "action_kill"
        const val ACTION_CANCEL = "action_cancel"

        fun newIntent(context: Context, notification: VenomNotification): Intent {
            return Intent(context, VenomService::class.java)
                .putExtra(NOTIFICATION_RESOURCES_EXTRA, notification)
        }
    }
}