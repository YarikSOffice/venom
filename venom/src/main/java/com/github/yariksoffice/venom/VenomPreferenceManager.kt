package com.github.yariksoffice.venom

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

internal class VenomPreferenceManager(context: Context) {

    private val preference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)

    fun setActive(active: Boolean) {
        preference.edit().putBoolean(ACTIVE_KEY, active).apply()
    }

    fun isActive(): Boolean {
        return preference.getBoolean(ACTIVE_KEY, false)
    }

    companion object {
        private const val PREFERENCE_NAME = "venom"
        private const val ACTIVE_KEY = "active_key"
    }
}