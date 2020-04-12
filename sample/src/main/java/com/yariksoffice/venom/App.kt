package com.yariksoffice.venom

import android.app.Application
import com.github.yariksoffice.venom.Venom

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val venom = Venom.createInstance(this)
        venom.initialize()
        Venom.setGlobalInstance(venom)
    }
}