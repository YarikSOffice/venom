package com.github.yariksoffice.venom

import android.content.Context

class Venom private constructor() {

    fun initialize() {}

    fun start() {}

    fun stop() {}

    fun isRunning() = false

    companion object {

        fun createInstance(ignored: Context) = Venom()

        fun setGlobalInstance(ignored: Venom) {}

        fun getGlobalInstance() = Venom()
    }
}