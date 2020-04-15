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

package com.github.yariksoffice.venom

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import java.lang.IllegalStateException

/**
 * Venom is a lightweight tool that simplifies testing of the process death scenario
 * for your android application.
 *
 * Venom makes it possible to kill the app process from the notification drawer making the testing
 * easier and more straightforward in comparison with the traditional ways.
 */
class Venom private constructor(
    private val context: Context,
    private val prefs: VenomPreferenceManager
) {

    /**
     * Initializes the [Venom] and invalidates its state
     */
    fun initialize() {
        if (prefs.isActive()) {
            start()
        }
    }

    /**
     * Starts the [Venom] and puts the notification in the drawer
     */
    fun start() {
        prefs.setActive(true)
        if (!isVenomServiceRunning()) {
            context.startService(Intent(context, VenomService::class.java))
        }
    }

    /**
     * Terminates the [Venom] and removes the notification from the drawer
     */
    fun stop() {
        prefs.setActive(false)
        context.stopService(Intent(context, VenomService::class.java))
    }

    /**
     * Indicates whether the [Venom] is currently running
     */
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

        /**
         * Creates a new instance of [Venom]
         */
        fun createInstance(context: Context): Venom {
            val compositionRoot = CompositionRoot.getCompositionRoot(context)
            return Venom(context, compositionRoot.preferenceManager)
        }

        /**
         * Sets the global instance returned from [getGlobalInstance].
         *
         * throws an [IllegalStateException] if the global instance is already initialized
         */
        fun setGlobalInstance(venom: Venom) {
            if (instance != null) {
                throw IllegalStateException("The global instance is already initialized")
            }
            instance = venom
        }

        /**
         * Returns the global [Venom] instance set via [setGlobalInstance].
         *
         * throws an [IllegalStateException] if the global instance is not initialized
         */
        fun getGlobalInstance(): Venom {
            return instance ?: throw IllegalStateException("The global instance is not initialized")
        }
    }
}