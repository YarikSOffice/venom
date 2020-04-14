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
            val compositionRoot = CompositionRoot.getCompositionRoot(context)
            return Venom(context, compositionRoot.preferenceManager)
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