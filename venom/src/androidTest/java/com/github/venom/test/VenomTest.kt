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
import android.app.Application
import android.content.ComponentName
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.github.venom.Venom
import com.github.venom.test.VenomTest.Action.Kill
import com.github.venom.test.VenomTest.Action.Restart
import com.github.venom.test.VenomTest.Behavior.Default
import com.github.venom.test.VenomTest.Behavior.LongSaveState
import com.github.venom.test.VenomTest.Behavior.LongStop
import com.github.venom.test.VenomTest.State.Background
import com.github.venom.test.VenomTestActivity.Companion.InputArg
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("unused")
@RunWith(TestParameterInjector::class)
@RequiresApi(18)
class VenomTest {

    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val device = UiDevice.getInstance(instrumentation)
    private val appContext = instrumentation.targetContext.applicationContext as Application

    @Suppress("DEPRECATION")
    private val testActivity = appContext.packageManager.getActivityInfo(ComponentName(appContext, VenomTestActivity::class.java), 0)
    private val venom = Venom.createInstance(appContext)

    enum class Backstack(val count: Int) {
        Single(1),
        Multiple(MULTIPLE_ACTIVITY_COUNT),
    }

    enum class Behavior { Default, LongStop, LongSaveState }

    enum class Action { Restart, Kill }

    enum class State { Foreground, Background, }

    @Before
    fun setupEach() {
        venom.start()
        collapseNotifications()
    }

    @After
    fun tearDown() {
        venom.stop()
    }

    @Test
    fun matrix(
        @TestParameter backstack: Backstack,
        @TestParameter state: State,
        @TestParameter behavior: Behavior,
        @TestParameter action: Action,
    ) {
        launchActivities(backstack, behavior)
        if (state == Background) device.pressHome()
        if (action == Restart) restart()
        if (action == Kill) kill()
        assertActivities(backstack, action)
        if (action == Kill) assertProcessDeath()
    }

    @Test
    fun stop_oneActivityInForeground_cancelNotification() {
        launchActivities(Backstack.Single)
        stopVenom()

        val cancelBtn = By.desc(appContext.getString(R.string.venom_notification_button_cancel))
        val found = device.wait(Until.hasObject(cancelBtn), WAIT_TIMEOUT)
        assert(found != true)
    }

    private fun launchActivities(
        backstack: Backstack,
        behavior: Behavior = Default,
    ) {
        val args = arrayListOf<InputArg>()
        for (i in 0 until backstack.count) {
            val top = i == backstack.count - 1
            val arg = InputArg(
                number = i + 1,
                longStop = behavior == LongStop && top,
                longSaveSate = behavior == LongSaveState && top
            )
            args.add(arg)
        }
        val rootArg = args.removeAt(0).copy(args = args)
        val rootIntent = VenomTestActivity.launchIntent(context = appContext, arg = rootArg)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        appContext.startActivity(rootIntent)
        device.wait(Until.hasObject(activitySelector(backstack.count)), WAIT_TIMEOUT)
    }

    private fun restart() {
        val restartBtn = By.desc(appContext.getString(R.string.venom_notification_button_restart))

        device.openNotification()
        device.wait(Until.findObject(restartBtn), WAIT_TIMEOUT)
        device.findObject(restartBtn).click()
        collapseNotifications()

        device.wait(Until.gone(activitySelector(activityCount())), WAIT_TIMEOUT)
        device.wait(Until.hasObject(anyActivitySelector), WAIT_TIMEOUT)
    }

    private fun kill() {
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
            .singleOrNull { it.baseActivity?.className == VenomTestActivity::class.qualifiedName }
        return testActivitiesTask?.numActivities ?: 0
    }

    private fun assertActivities(backstack: Backstack, action: Action) {
        assertEquals("Activity count", backstack.count, activityCount())
        if (action == Restart) assertTrue("Activity displayed", device.hasObject(activitySelector(backstack.count)))
    }

    private fun assertProcessDeath(repeat: Int = 10) {
        var result: Result<Unit>? = null
        repeat(repeat) {
            val am = appContext.getSystemService<ActivityManager>()!!
            val testProcesses = am.runningAppProcesses.filter { it.processName == testActivity.processName }
            result = kotlin.runCatching {
                val message = "Process list must be empty ${testProcesses.map { it.processName }}"
                assertTrue(message, testProcesses.isEmpty())
            }.onFailure {
                Thread.sleep(2 * WAIT_TIMEOUT / repeat)
            }.onSuccess {
                return
            }
        }
        result!!.getOrThrow()
    }

    companion object {
        private const val WAIT_TIMEOUT = 10000L
        private const val MULTIPLE_ACTIVITY_COUNT = 5

        private val anyActivitySelector = By.textStartsWith(VenomTestActivity.TITLE_PREFIX)
        private fun activitySelector(number: Int) = By.text(VenomTestActivity.TITLE_PREFIX + number)
    }
}
