package com.github.venom.service

import android.content.Context

@Suppress("DataClassPrivateConstructor")
data class NotificationConfig private constructor(
    val title: String,
    val text: String,
    val buttonKill: String,
    val buttonCancel: String,
    val iconRes: Int,
    val colorRes: Int
) {

    class Builder(c: Context) {

        fun title(title: String) = this
        fun title(title: Int) = this
        fun text(text: String) = this
        fun text(text: Int) = this
        fun buttonKill(text: String) = this
        fun buttonKill(text: Int) = this
        fun buttonCancel(text: String) = this
        fun buttonCancel(text: Int) = this
        fun icon(icon: Int) = this
        fun color(color: Int) = this

        fun build() = NotificationConfig("", "", "", "", 0, 0)
    }
}
