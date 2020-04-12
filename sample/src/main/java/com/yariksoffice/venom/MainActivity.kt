package com.yariksoffice.venom

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TextView
import com.github.yariksoffice.venom.Venom

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val venom = Venom.getGlobalInstance()
        val number = intent.getIntExtra(NUMBER_ARG, 1)

        findViewById<TextView>(R.id.label).text = getString(R.string.venom_screen_label, number)
        findViewById<View>(R.id.next_activity).setOnClickListener {
            launch(this, number + 1)
        }

        val switch = findViewById<Switch>(R.id.switch_button)
        switch.isChecked = venom.isRunning()
        switch.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                venom.start()
            } else {
                venom.stop()
            }
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