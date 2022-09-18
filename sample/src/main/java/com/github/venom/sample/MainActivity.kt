package com.github.venom.sample

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import com.github.venom.Venom

class MainActivity : AppCompatActivity() {

    private val venom = Venom.getGlobalInstance()

    private val permissionLauncher = registerForActivityResult(RequestPermission()) { isGranted ->
        if (isGranted) {
            venom.start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val number = intent.getIntExtra(NUMBER_ARG, 1)

        findViewById<TextView>(R.id.pid).text = getString(R.string.pid, Process.myPid())
        findViewById<TextView>(R.id.label).text = getString(R.string.venom_screen_label, number)
        findViewById<View>(R.id.next_activity).setOnClickListener {
            launch(this, number + 1)
        }
        findViewById<View>(R.id.start).setOnClickListener {
            verifyNotificationPermissionAndLaunchVenom()
        }
        findViewById<View>(R.id.stop).setOnClickListener {
            venom.stop()
        }
    }

    private fun verifyNotificationPermissionAndLaunchVenom() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            venom.start()
        } else {
            permissionLauncher.launch(POST_NOTIFICATIONS)
        }
    }

    companion object {
        private const val NUMBER_ARG = "number_arg"

        fun launch(context: Context, number: Int) {
            val intent = Intent(context, MainActivity::class.java)
                .putExtra(NUMBER_ARG, number)
            context.startActivity(intent)
        }
    }
}
