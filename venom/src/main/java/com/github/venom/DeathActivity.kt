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

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process

internal class DeathActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application.registerActivityLifecycleCallbacks(SuicidalLifecycleCallbacks())
    }

    override fun onBackPressed() {
        // No-op
    }

    /**
     * Terminates the process when the app is ready to commit suicide.
     *
     * If the system kills a process, the associated app can restore its activities that have
     * a state in a new process. An activity is considered to have a state in two cases:
     * 1. It hasn't resumed yet (an empty state).
     * 2. The activity manager service has gotten its last saved state. This happens right
     * after [onStop] and [onSaveInstanceState] events.
     *
     * Our goal is to restore the same back stack once the app is recreated. To achieve this,
     * we kill the process as soon as:
     * 1. [DeathActivity] is resumed - to remove it from the stack.
     * 2. The activity manager service gets the last saved state of the activities that
     * haven't stopped yet - to keep them in the stack.
     *
     * When [DeathActivity] is being resumed we schedule a suicide attempt with a delay.
     * This delay is long enough to check if there are any activities that haven't stopped yet.
     * In case we have them, we postpone the suicide until the activity manager service gets
     * the last saved state of such the activities.
     */
    private class SuicidalLifecycleCallbacks : ActivityLifecycleCallbacks {

        private val handler = Handler(Looper.getMainLooper())

        /** Kills the app process. */
        private val venomousRunnable = Runnable { Process.killProcess(Process.myPid()) }

        /** Schedules the suicide with a delay when [DeathActivity] resumes. */
        override fun onActivityResumed(activity: Activity) {
            if (activity !is DeathActivity) return
            handler.postDelayed(venomousRunnable, DELAY_TIMEOUT_MILLIS)
        }

        /**
         * Postpones the suicide until the activity manager service gets the last saved state
         * of every stopping activity except [DeathActivity].
         *
         * The Android framework stops an activity in the following steps:
         * 1. Invokes [onStop] and [onSaveInstanceState] methods along with associated
         * [ActivityLifecycleCallbacks] (the order depends on the targetSdkVersion).
         * 2. Reports the last activity saved state to the activity manager service. This *Report*
         * is enqueued via the UI thread [Handler] in the same call with the previous step.
         *
         * Given that, our goal is to terminate the process as soon as the *Report* is processed.
         * To achieve this, we use a nested [Handler.post] call:
         * 1. The outer runnable is enqueued inside [onActivityStopped] callback and is processed
         * when the *Report* is already in the message queue.
         * 2. We post the inner runnable which is guaranteed to be processed after the *Report*.
         **/
        override fun onActivityStopped(activity: Activity) {
            if (activity is DeathActivity) return
            // cancel the suicide and let the activity give up
            handler.removeCallbacksAndMessages(null)
            handler.post { handler.post(venomousRunnable) }
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            // No-op
        }
        override fun onActivityStarted(activity: Activity) {
            // No-op
        }
        override fun onActivityPaused(activity: Activity) {
            // No-op
        }
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            // No-op
        }
        override fun onActivityDestroyed(activity: Activity) {
            // No-op
        }
    }

    companion object {
        private const val DELAY_TIMEOUT_MILLIS = 1000L // 1s

        fun launch(context: Context) {
            val intent = Intent(context, DeathActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
