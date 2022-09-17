/*
 * The MIT License (MIT)
 *
 * Copyright 2020 Yaroslav Berezanskyi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.venom.test

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import java.io.Serializable

class VenomTestActivity : Activity() {

    private lateinit var arg: InputArg
    private var processedInput = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        processedInput = savedInstanceState?.getBoolean(PROCESSED_INPUT_KEY) ?: false

        arg = intent.getSerializableExtra(INPUT_ARG, InputArg::class.java)!!
        title = TITLE_PREFIX + arg.number

        val args = arg.args
        if (!processedInput && !args.isNullOrEmpty()) {
            processedInput = true
            val intents = args.map { launchIntent(this, it) }
            startActivities(intents.toTypedArray())
        }
    }

    override fun onStop() {
        if (arg.longStop) Thread.sleep(LONG_TASK_TIME_MILLIS)
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (arg.longSaveSate) Thread.sleep(LONG_TASK_TIME_MILLIS)
        outState.putBoolean(PROCESSED_INPUT_KEY, processedInput)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val INPUT_ARG = "input"
        private const val PROCESSED_INPUT_KEY = "processed input"
        private const val LONG_TASK_TIME_MILLIS = 5000L

        const val TITLE_PREFIX = "Activity #"

        data class InputArg(
            val number: Int,
            val longStop: Boolean,
            val longSaveSate: Boolean,
            val args: ArrayList<InputArg>? = null
        ) : Serializable

        fun launchIntent(context: Context, arg: InputArg): Intent {
            return Intent(context, VenomTestActivity::class.java)
                .putExtra(INPUT_ARG, arg)
        }
    }
}
