package com.github.venom.sample

import android.app.Application
import com.github.venom.Venom
import com.github.venom.service.NotificationConfig

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val venom = Venom.createInstance(this)

        val notification = NotificationConfig.Builder(this)
            .buttonCancel(R.string.venom_notification_button_cancel_override)
            .buttonKill(getString(R.string.venom_notification_button_kill_override))
            .build()
        venom.initialize(notification)
        Venom.setGlobalInstance(venom)
    }
}