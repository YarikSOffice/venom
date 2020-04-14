package com.github.yariksoffice.venom

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import kotlin.system.exitProcess

internal class DeathActivity : Activity() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.death_activity)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(
            { exitProcess(0) },
            DELAY_TIMEOUT_MILLIS
        )
    }

    override fun onBackPressed() {
        // ignore
    }

    companion object {
        private const val DELAY_TIMEOUT_MILLIS = 1500L

        fun launch(context: Context) {
            val intent = Intent(context, DeathActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

}