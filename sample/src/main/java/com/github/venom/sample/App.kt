package com.github.venom.sample

import android.app.Application
import com.github.venom.Venom

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val venom = Venom.createInstance(this)
        venom.initialize()
        Venom.setGlobalInstance(venom)
    }
}