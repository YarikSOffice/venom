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

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.github.venom.Venom
import com.github.venom.test.VenomTestActivity.Companion.InputArg
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@RequiresApi(18)
class VenomTest {

    private lateinit var device: UiDevice
    private lateinit var appContext: Context
    private lateinit var venom: Venom

    @Before
    fun setupEach() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        device = UiDevice.getInstance(instrumentation)
        appContext = instrumentation.targetContext
        venom = Venom.createInstance(appContext)

        venom.start()
        collapseNotifications()
    }

    @After
    fun tearDown() {
        venom.stop()
    }

    @Test
    fun suicide_oneActivityInForeground_sameActivityCount() {
        launchActivities(count = 1)
        commitSuicide()
        assertEquals(1, activityCount())
    }

    @Test
    fun suicide_oneActivityInForeground_topRestored() {
        launchActivities(count = 1)
        commitSuicide()
        assertTrue(device.hasObject(activitySelector(1)))
    }

    @Test
    fun suicide_oneActivityInBackground_sameActivityCount() {
        launchActivities(count = 1)
        moveToBackgroundAndCommitSuicide()
        assertEquals(1, activityCount())
    }

    @Test
    fun suicide_oneActivityInBackground_topRestored() {
        launchActivities(count = 1)
        moveToBackgroundAndCommitSuicide()
        assertTrue(device.hasObject(activitySelector(1)))
    }

    @Test
    fun suicide_oneActivityInForegroundWithLongStop_sameActivityCount() {
        launchActivities(count = 1, longStop = true)
        commitSuicide()
        assertEquals(1, activityCount())
    }

    @Test
    fun suicide_oneActivityInForegroundWithLongStop_topRestored() {
        launchActivities(count = 1, longStop = true)
        commitSuicide()
        assertTrue(device.hasObject(activitySelector(1)))
    }

    @Test
    fun suicide_oneActivityInBackgroundWithLongStop_sameActivityCount() {
        launchActivities(count = 1, longStop = true)
        moveToBackgroundAndCommitSuicide()
        assertEquals(1, activityCount())
    }

    @Test
    fun suicide_oneActivityInBackgroundWithLongStop_topRestored() {
        launchActivities(count = 1, longStop = true)
        moveToBackgroundAndCommitSuicide()
        assertTrue(device.hasObject(activitySelector(1)))
    }

    @Test
    fun suicide_oneActivityInForegroundWithLongSaveState_sameActivityCount() {
        launchActivities(count = 1, longSaveState = true)
        commitSuicide()
        assertEquals(1, activityCount())
    }

    @Test
    fun suicide_oneActivityInForegroundWithLongSaveState_topRestored() {
        launchActivities(count = 1, longSaveState = true)
        commitSuicide()
        assertTrue(device.hasObject(activitySelector(1)))
    }

    @Test
    fun suicide_oneActivityInBackgroundWithLongSaveState_sameActivityCount() {
        launchActivities(count = 1, longSaveState = true)
        moveToBackgroundAndCommitSuicide()
        assertEquals(1, activityCount())
    }

    @Test
    fun suicide_oneActivityInBackgroundWithLongSaveState_topRestored() {
        launchActivities(count = 1, longSaveState = true)
        moveToBackgroundAndCommitSuicide()
        assertTrue(device.hasObject(activitySelector(1)))
    }

    @Test
    fun suicide_multipleActivityInForeground_sameActivityCount() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT)
        commitSuicide()
        assertEquals(MULTIPLE_ACTIVITY_COUNT, activityCount())
    }

    @Test
    fun suicide_multipleActivityInForeground_topRestored() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT)
        commitSuicide()
        assertTrue(device.hasObject(activitySelector(MULTIPLE_ACTIVITY_COUNT)))
    }

    @Test
    fun suicide_multipleActivityInBackground_sameActivityCount() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT)
        moveToBackgroundAndCommitSuicide()
        assertEquals(MULTIPLE_ACTIVITY_COUNT, activityCount())
    }

    @Test
    fun suicide_multipleActivityInBackground_topRestored() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT)
        moveToBackgroundAndCommitSuicide()
        assertTrue(device.hasObject(activitySelector(MULTIPLE_ACTIVITY_COUNT)))
    }

    @Test
    fun suicide_multipleActivityInForegroundWithLongStop_sameActivityCount() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT, longStop = true)
        commitSuicide()
        assertEquals(MULTIPLE_ACTIVITY_COUNT, activityCount())
    }

    @Test
    fun suicide_multipleActivityInForegroundWithLongStop_topRestored() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT, longStop = true)
        commitSuicide()
        assertTrue(device.hasObject(activitySelector(MULTIPLE_ACTIVITY_COUNT)))
    }

    @Test
    fun suicide_multipleActivityInBackgroundWithLongStop_sameActivityCount() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT, longStop = true)
        moveToBackgroundAndCommitSuicide()
        assertEquals(MULTIPLE_ACTIVITY_COUNT, activityCount())
    }

    @Test
    fun suicide_multipleActivityInBackgroundWithLongStop_topRestored() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT, longStop = true)
        moveToBackgroundAndCommitSuicide()
        assertTrue(device.hasObject(activitySelector(MULTIPLE_ACTIVITY_COUNT)))
    }

    @Test
    fun suicide_multipleActivityInForegroundWithLongSaveState_sameActivityCount() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT, longSaveState = true)
        commitSuicide()
        assertEquals(MULTIPLE_ACTIVITY_COUNT, activityCount())
    }

    @Test
    fun suicide_multipleActivityInForegroundWithLongSaveState_topRestored() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT, longSaveState = true)
        commitSuicide()
        assertTrue(device.hasObject(activitySelector(MULTIPLE_ACTIVITY_COUNT)))
    }

    @Test
    fun suicide_multipleActivityInBackgroundWithLongSaveState_sameActivityCount() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT, longSaveState = true)
        moveToBackgroundAndCommitSuicide()
        assertEquals(MULTIPLE_ACTIVITY_COUNT, activityCount())
    }

    @Test
    fun suicide_multipleActivityInBackgroundWithLongSaveState_topRestored() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT, longSaveState = true)
        moveToBackgroundAndCommitSuicide()
        assertTrue(device.hasObject(activitySelector(MULTIPLE_ACTIVITY_COUNT)))
    }

    @Test
    fun stop_oneActivityInForeground_cancelNotification() {
        launchActivities(count = 1)
        stopVenom()

        val cancelBtn = By.desc(appContext.getString(R.string.venom_notification_button_cancel))
        val found = device.wait(Until.hasObject(cancelBtn), WAIT_TIMEOUT)
        assert(found != true)
    }

    private fun launchActivities(
        @IntRange(from = 1) count: Int,
        longStop: Boolean = false,
        longSaveState: Boolean = false
    ) {
        val args = arrayListOf<InputArg>()
        for (i in 0 until count) {
            val top = i == count - 1
            val arg = InputArg(
                number = i + 1,
                longStop = longStop && top,
                longSaveSate = longSaveState && top
            )
            args.add(arg)
        }
        val rootArg = args.removeAt(0).copy(args = args)
        val rootIntent = VenomTestActivity.launchIntent(context = appContext, arg = rootArg)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        appContext.startActivity(rootIntent)
        device.wait(Until.hasObject(activitySelector(count)), WAIT_TIMEOUT)
    }

    private fun commitSuicide() {
        val killBtn = By.desc(appContext.getString(R.string.venom_notification_button_kill))

        device.openNotification()
        device.wait(Until.findObject(killBtn), WAIT_TIMEOUT)
        device.findObject(killBtn).click()
        collapseNotifications()

        device.wait(Until.gone(activitySelector(activityCount())), WAIT_TIMEOUT)
        device.wait(Until.hasObject(anyActivitySelector), WAIT_TIMEOUT)
    }

    private fun stopVenom() {
        val cancelBtn = By.desc(appContext.getString(R.string.venom_notification_button_cancel))
        device.openNotification()
        device.wait(Until.findObject(cancelBtn), WAIT_TIMEOUT)
        device.findObject(cancelBtn).click()
        collapseNotifications()

        device.wait(Until.hasObject(activitySelector(activityCount())), WAIT_TIMEOUT)
        device.openNotification()
    }

    private fun collapseNotifications() {
        @Suppress("DEPRECATION") // still eligible for tests
        appContext.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
    }

    @Suppress("DEPRECATION")
    private fun activityCount(): Int {
        val am = appContext.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val testActivitiesTask = am.getRunningTasks(Int.MAX_VALUE)
            .firstOrNull { it.baseActivity?.className == VenomTestActivity::class.qualifiedName }
        return testActivitiesTask?.numActivities ?: 0
    }

    private fun moveToBackgroundAndCommitSuicide() {
        device.pressHome()
        commitSuicide()
    }

    companion object {
        private const val WAIT_TIMEOUT = 10000L
        private const val MULTIPLE_ACTIVITY_COUNT = 5

        private val anyActivitySelector = By.textStartsWith(VenomTestActivity.TITLE_PREFIX)
        private fun activitySelector(number: Int) = By.text(VenomTestActivity.TITLE_PREFIX + number)
    }
}
