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
import android.content.ComponentName
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.content.pm.PackageManager.ComponentInfoFlags
import android.os.Build
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

private const val POST_NOTIFICATIONS = "android.permission.POST_NOTIFICATIONS"

@RunWith(AndroidJUnit4::class)
@RequiresApi(18)
class VenomTest {

    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val device = UiDevice.getInstance(instrumentation)
    private val appContext = instrumentation.targetContext
    private val venom = Venom.createInstance(appContext)

    @Before
    fun setupEach() {
        grantNotificationsPermission()
        venom.start()
        collapseNotifications()
    }

    private fun grantNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            instrumentation.uiAutomation.executeShellCommand(
                "pm grant ${appContext.packageName} $POST_NOTIFICATIONS"
            )
        }
    }

    @After
    fun tearDown() {
        venom.stop()
    }

    @Test
    fun suicide_oneActivityInForeground_restart() {
        launchActivities(count = 1)
        restartProcess()
        assertDisplayedActivities(count = 1)
    }

    @Test
    fun suicide_oneActivityInBackground_restart() {
        launchActivities(count = 1)
        moveToBackgroundAndRestartProcess()
        assertDisplayedActivities(count = 1)
    }

    @Test
    fun suicide_oneActivityInForegroundWithLongStop_restart() {
        launchActivities(count = 1, longStop = true)
        restartProcess()
        assertDisplayedActivities(count = 1)
    }

    @Test
    fun suicide_oneActivityInBackgroundWithLongStop_restart() {
        launchActivities(count = 1, longStop = true)
        moveToBackgroundAndRestartProcess()
        assertDisplayedActivities(count = 1)
    }

    @Test
    fun suicide_oneActivityInForegroundWithLongSaveState_restart() {
        launchActivities(count = 1, longSaveState = true)
        restartProcess()
        assertDisplayedActivities(count = 1)
    }

    @Test
    fun suicide_oneActivityInBackgroundWithLongSaveState_restart() {
        launchActivities(count = 1, longSaveState = true)
        moveToBackgroundAndRestartProcess()
        assertDisplayedActivities(count = 1)
    }

    @Test
    fun suicide_multipleActivityInForeground_restart() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT)
        restartProcess()
        assertDisplayedActivities(count = MULTIPLE_ACTIVITY_COUNT)
    }

    @Test
    fun suicide_multipleActivityInBackground_restart() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT)
        moveToBackgroundAndRestartProcess()
        assertDisplayedActivities(count = MULTIPLE_ACTIVITY_COUNT)
    }

    @Test
    fun suicide_multipleActivityInForegroundWithLongStop_restart() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT, longStop = true)
        restartProcess()
        assertDisplayedActivities(count = MULTIPLE_ACTIVITY_COUNT)
    }

    @Test
    fun suicide_multipleActivityInBackgroundWithLongStop_restart() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT, longStop = true)
        moveToBackgroundAndRestartProcess()
        assertDisplayedActivities(count = MULTIPLE_ACTIVITY_COUNT)
    }

    @Test
    fun suicide_multipleActivityInForegroundWithLongSaveState_restart() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT, longSaveState = true)
        restartProcess()
        assertDisplayedActivities(count = MULTIPLE_ACTIVITY_COUNT)
    }

    @Test
    fun suicide_multipleActivityInBackgroundWithLongSaveState_restart() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT, longSaveState = true)
        moveToBackgroundAndRestartProcess()
        assertDisplayedActivities(count = MULTIPLE_ACTIVITY_COUNT)
    }

    @Test
    fun stop_oneActivityInForeground_cancelNotification() {
        launchActivities(count = 1)
        stopVenom()

        val cancelBtn = By.desc(appContext.getString(R.string.venom_notification_button_cancel))
        val found = device.wait(Until.hasObject(cancelBtn), WAIT_TIMEOUT)
        assert(found != true)
    }

    @Test
    fun suicide_oneActivityInForeground_kill() {
        launchActivities(count = 1)
        killProcess()
        assertProcessDeath()
    }

    @Test
    fun suicide_oneActivityInBackground_kill() {
        launchActivities(count = 1)
        moveToBackgroundAndKillProcess()
        assertProcessDeath()
    }

    @Test
    fun suicide_oneActivityInForegroundWithLongStop_kill() {
        launchActivities(count = 1, longStop = true)
        killProcess()
        assertProcessDeath()
    }

    @Test
    fun suicide_oneActivityInBackgroundWithLongStop_kill() {
        launchActivities(count = 1, longStop = true)
        moveToBackgroundAndKillProcess()
        assertProcessDeath()
    }

    @Test
    fun suicide_oneActivityInForegroundWithLongSaveState_kill() {
        launchActivities(count = 1, longSaveState = true)
        killProcess()
        assertProcessDeath()
    }

    @Test
    fun suicide_oneActivityInBackgroundWithLongSaveState_kill() {
        launchActivities(count = 1, longSaveState = true)
        moveToBackgroundAndKillProcess()
        assertProcessDeath()
    }

    @Test
    fun suicide_multipleActivityInForeground_kill() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT)
        killProcess()
        assertProcessDeath()
    }

    @Test
    fun suicide_multipleActivityInBackground_kill() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT)
        moveToBackgroundAndKillProcess()
        assertProcessDeath()
    }

    @Test
    fun suicide_multipleActivityInForegroundWithLongStop_kill() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT, longStop = true)
        killProcess()
        assertProcessDeath()
    }

    @Test
    fun suicide_multipleActivityInBackgroundWithLongStop_kill() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT, longStop = true)
        moveToBackgroundAndKillProcess()
        assertProcessDeath()
    }

    @Test
    fun suicide_multipleActivityInForegroundWithLongSaveState_kill() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT, longSaveState = true)
        killProcess()
        assertProcessDeath()
    }

    @Test
    fun suicide_multipleActivityInBackgroundWithLongSaveState_kill() {
        launchActivities(count = MULTIPLE_ACTIVITY_COUNT, longSaveState = true)
        moveToBackgroundAndKillProcess()
        assertProcessDeath()
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

    private fun restartProcess() {
        val restartBtn = By.desc(appContext.getString(R.string.venom_notification_button_restart))

        device.openNotification()
        device.wait(Until.findObject(restartBtn), WAIT_TIMEOUT)
        device.findObject(restartBtn).click()
        collapseNotifications()

        device.wait(Until.gone(activitySelector(activityCount())), WAIT_TIMEOUT)
        device.wait(Until.hasObject(anyActivitySelector), WAIT_TIMEOUT)
    }

    private fun killProcess() {
        val killBtn = By.desc(appContext.getString(R.string.venom_notification_button_kill))

        device.openNotification()
        device.wait(Until.findObject(killBtn), WAIT_TIMEOUT)
        device.findObject(killBtn).click()
        collapseNotifications()
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

    private fun assertDisplayedActivities(count: Int) {
        assertEquals("Activity count", count, activityCount())
        assertTrue("Activity displayed", device.hasObject(activitySelector(count)))
    }

    private fun moveToBackgroundAndRestartProcess() {
        device.pressHome()
        restartProcess()
    }

    private fun moveToBackgroundAndKillProcess() {
        device.pressHome()
        killProcess()
    }

    private fun assertProcessDeath() {
        val componentName = ComponentName(appContext, VenomTestActivity::class.java)
        val testActivity = appContext.packageManager.getActivityInfo(componentName, ComponentInfoFlags.of(0))
        val am = appContext.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val repeat = PROCESS_DEATH_ASSERT_TIMES
        var result: Result<Unit>? = null
        repeat(repeat) {
            val testProcesses = am.runningAppProcesses
                .filter { it.processName == testActivity.processName }
                .map { it.processName }
            result = runCatching {
                val message = "Process list must be empty $testProcesses"
                assertTrue(message, testProcesses.isEmpty())
            }.onFailure {
                Thread.sleep(PROCESS_DEATH_ASSERT_DURATION / repeat)
            }.onSuccess {
                return
            }
        }
        requireNotNull(result).getOrThrow()
    }

    companion object {
        private const val PROCESS_DEATH_ASSERT_DURATION = 15000L
        private const val PROCESS_DEATH_ASSERT_TIMES = 10
        private const val WAIT_TIMEOUT = 10000L
        private const val MULTIPLE_ACTIVITY_COUNT = 5

        private val anyActivitySelector = By.textStartsWith(VenomTestActivity.TITLE_PREFIX)
        private fun activitySelector(number: Int) = By.text(VenomTestActivity.TITLE_PREFIX + number)
    }
}
