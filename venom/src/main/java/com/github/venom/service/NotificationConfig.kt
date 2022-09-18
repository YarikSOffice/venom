package com.github.venom.service

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.github.venom.R

@Suppress("DataClassPrivateConstructor")
data class NotificationConfig private constructor(
    val title: String,
    val text: String,
    val buttonKill: String,
    val buttonRestart: String,
    val buttonCancel: String,
    val iconRes: Int,
    val colorRes: Int
) {

    class Builder(private val c: Context) {
        private var title: String = c.getString(R.string.venom_foreground_service_title)
        private var text: String = c.getString(R.string.venom_foreground_service_text)
        private var buttonKill: String = c.getString(R.string.venom_notification_button_kill)
        private var buttonRestart: String = c.getString(R.string.venom_notification_button_restart)
        private var buttonCancel: String = c.getString(R.string.venom_notification_button_cancel)
        private var icon: Int = R.drawable.android_adb
        private var color: Int = R.color.venom_primary

        fun title(title: String) = apply { this.title = title }
        fun title(@StringRes title: Int) = apply { this.title = c.getString(title) }
        fun text(text: String) = apply { this.text = text }
        fun text(@StringRes text: Int) = apply { this.text = c.getString(text) }
        fun buttonKill(text: String) = apply { buttonKill = text }
        fun buttonKill(@StringRes text: Int) = apply { buttonKill = c.getString(text) }
        fun buttonRestart(text: String) = apply { buttonRestart = text }
        fun buttonRestart(@StringRes text: Int) = apply { buttonRestart = c.getString(text) }
        fun buttonCancel(text: String) = apply { buttonCancel = text }
        fun buttonCancel(@StringRes text: Int) = apply { buttonCancel = c.getString(text) }
        fun icon(@DrawableRes icon: Int) = apply { this.icon = icon }
        fun color(@ColorRes color: Int) = apply { this.color = color }

        fun build() = NotificationConfig(
            title,
            text,
            buttonKill,
            buttonRestart,
            buttonCancel,
            icon,
            color
        )
    }
}

internal fun defaultNotification(c: Context) = NotificationConfig.Builder(c).build()
