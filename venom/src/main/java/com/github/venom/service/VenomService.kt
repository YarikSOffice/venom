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

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.github.venom.CompositionRoot
import com.github.venom.DeathActivity
import com.github.venom.VenomPreferenceManager

internal class VenomService : Service() {

    private lateinit var prefs: VenomPreferenceManager

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        val compositionRoot = CompositionRoot.getCompositionRoot(this)
        prefs = compositionRoot.preferenceManager
        val notificationManager = compositionRoot.notificationManager
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
