package com.github.yariksoffice.venom

import android.content.Context

internal class CompositionRoot private constructor(context: Context) {
    val notificationManager = VenomNotificationManager(context)
    val preferenceManager = VenomPreferenceManager(context)

    companion object {
        private lateinit var root: CompositionRoot

        fun getCompositionRoot(context: Context): CompositionRoot {
            if (!::root.isInitialized) {
                root = CompositionRoot(context)
            }
            return root
        }
    }
}