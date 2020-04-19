package com.github.venom.sample

import android.app.Application
import com.github.venom.Venom

class App : Application() {


    private val appName: CharSequence? get() = applicationInfo.labelRes.let {
        packageManager?.getApplicationLabel(applicationInfo)
    }

    override fun onCreate() {
        super.onCreate()
        val venom = Venom.createInstance(this)
        venom.initialize {
            contentTitle = "Customizing Venom for " + appName
            contentText = "If this is not set a default will be taken"
        }
        Venom.setGlobalInstance(venom)
    }
}