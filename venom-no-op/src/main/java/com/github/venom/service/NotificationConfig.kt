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

@file:Suppress("unused")

package com.github.venom.service

import android.content.Context

@Suppress("DataClassPrivateConstructor", "UNUSED_PARAMETER")
data class NotificationConfig private constructor(
    val title: String,
    val text: String,
    val buttonKill: String,
    val buttonCancel: String,
    val iconRes: Int,
    val colorRes: Int
) {

    class Builder(c: Context) {

        fun title(title: String) = this
        fun title(title: Int) = this
        fun text(text: String) = this
        fun text(text: Int) = this
        fun buttonKill(text: String) = this
        fun buttonKill(text: Int) = this
        fun buttonCancel(text: String) = this
        fun buttonCancel(text: Int) = this
        fun icon(icon: Int) = this
        fun color(color: Int) = this

        fun build() = NotificationConfig("", "", "", "", 0, 0)
    }
}
