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

class VenomTestActivity : Activity() {

    private var longStop: Boolean = false
    private var longSaveState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val countLeft = intent.getIntExtra(COUNT_LEFT_ARG, 0)
        title = TITLE_PREFIX + countLeft

        val topLongStop = intent.getBooleanExtra(TOP_LONG_STOP_ARG, false)
        val topLongSaveState = intent.getBooleanExtra(TOP_LONG_SAVE_STATE_ARG, false)

        if (countLeft == 0) {
            longStop = topLongStop
            longSaveState = topLongSaveState
        } else {
            val intent = launchIntent(
                context = this,
                countLeft = countLeft - 1,
                topActivityLongStop = topLongStop,
                topActivityLongSaveSate = topLongSaveState
            )
            startActivity(intent)
        }
    }

    override fun onStop() {
        if (longStop) Thread.sleep(LONG_TASK_TIME_MILLIS)
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (longSaveState) Thread.sleep(LONG_TASK_TIME_MILLIS)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val COUNT_LEFT_ARG = "count left"
        private const val TOP_LONG_STOP_ARG = "top activity long stop"
        private const val TOP_LONG_SAVE_STATE_ARG = "top activity long save state"
        private const val LONG_TASK_TIME_MILLIS = 5000L

        const val TITLE_PREFIX = "Activity #"

        fun launchIntent(
            context: Context,
            countLeft: Int,
            topActivityLongStop: Boolean,
            topActivityLongSaveSate: Boolean
        ): Intent {
            return Intent(context, VenomTestActivity::class.java)
                .putExtra(COUNT_LEFT_ARG, countLeft)
                .putExtra(TOP_LONG_STOP_ARG, topActivityLongStop)
                .putExtra(TOP_LONG_SAVE_STATE_ARG, topActivityLongSaveSate)
        }
    }
}