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

import android.content.Context
import com.github.venom.service.NotificationConfig
import com.github.venom.service.ServiceDelegate
import com.github.venom.service.VenomNotificationManager

/**
 * Venom is a lightweight tool that simplifies testing of the process death scenario
 * for your android application.
 *
 * Venom makes it possible to kill the app process from the notification drawer making the testing
 * easier and more straightforward in comparison with the traditional ways.
 */
class Venom private constructor(
    private val prefs: VenomPreferenceManager,
    private val notificationManager: VenomNotificationManager,
    private val delegate: ServiceDelegate
) {

    /**
     * Initializes the [Venom] and invalidates its state.
     */
    @JvmOverloads
    fun initialize(config: NotificationConfig? = null) {
        config?.let { notificationManager.config = it }
        if (prefs.isActive()) {
            start()
        }
    }

    /**
     * Starts the [Venom] and puts the notification in the drawer.
     */
    fun start() {
        prefs.setActive(true)
        delegate.startService()
    }

    /**
     * Terminates the [Venom] and removes the notification from the drawer.
     */
    fun stop() {
        prefs.setActive(false)
        delegate.stopService()
    }

    /**
     * Indicates whether the [Venom] is currently running.
     *
     * @return true if the [Venom] is currently running
     */
    fun isRunning(): Boolean {
        return delegate.isServiceRunning()
    }

    companion object {

        private var instance: Venom? = null

        /**
         * Creates a new instance of [Venom].
         *
         * @param context the [Context] to be used
         */
        @JvmStatic
        fun createInstance(context: Context): Venom {
            val root = CompositionRoot.getCompositionRoot(context)
            return Venom(root.preferenceManager, root.notificationManager, root.serviceDelegate)
        }

        /**
         * Sets the global instance returned from [getGlobalInstance].
         *
         * @param venom the [Venom] instance
         *
         * @throws [IllegalStateException] if the global instance is already initialized
         */
        @JvmStatic
        fun setGlobalInstance(venom: Venom) {
            check(instance == null) { "The global instance is already initialized" }
            instance = venom
        }

        /**
         * Returns the global [Venom] instance set via [setGlobalInstance].
         *
         * @throws [IllegalStateException] if the global instance is not initialized
         */
        @JvmStatic
        fun getGlobalInstance(): Venom {
            return checkNotNull(instance) { "The global instance is not initialized" }
        }

        internal fun createInstance(
            prefs: VenomPreferenceManager,
            notificationManager: VenomNotificationManager,
            delegate: ServiceDelegate
        ): Venom = Venom(prefs, notificationManager, delegate)
    }
}
