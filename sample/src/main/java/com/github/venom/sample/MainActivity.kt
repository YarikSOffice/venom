package com.github.venom.sample

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.github.venom.Venom

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

        findViewById<View>(R.id.start).setOnClickListener {
            venom.start()
        }
        findViewById<View>(R.id.stop).setOnClickListener {
            venom.stop()
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