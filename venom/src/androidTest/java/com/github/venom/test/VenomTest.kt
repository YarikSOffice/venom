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
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.github.venom.Venom
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class VenomTest {

    private lateinit var device: UiDevice

    @Before
    fun setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        Venom.getGlobalInstance().start()
        collapseNotifications()
    }

    @After
    fun tearDown() {
        Venom.getGlobalInstance().stop()
    }

    @Test
    fun testSameNumActivities_oneActivity_topInForeground() {
        launchActivities(count = 1)
        commitSuicide()
        assertEquals(1, numTestActivities())
    }

    @Test
    fun testTopRestored_oneActivity_topInForeground() {
        launchActivities(count = 1)
        commitSuicide()
        assertTrue(device.hasObject(topActivitySelector))
    }

    @Test
    fun testSameNumActivities_oneActivity_topInBackground() {
        launchActivities(count = 1)
        moveToBackgroundAndCommitSuicide()
        assertEquals(1, numTestActivities())
    }

    @Test
    fun testTopRestored_oneActivity_topInBackground() {
        launchActivities(count = 1)
        moveToBackgroundAndCommitSuicide()
        assertTrue(device.hasObject(topActivitySelector))
    }

    @Test
    fun testSameNumActivities_oneActivity_topInForegroundLongTermStop() {
        launchActivities(count = 1, topActivityLongTermStop = true)
        commitSuicide()
        assertEquals(1, numTestActivities())
    }

    @Test
    fun testTopRestored_oneActivity_topInForegroundLongTermStop() {
        launchActivities(count = 1, topActivityLongTermStop = true)
        commitSuicide()
        assertTrue(device.hasObject(topActivitySelector))
    }

    @Test
    fun testSameNumActivities_oneActivity_topInBackgroundLongTermStop() {
        launchActivities(count = 1, topActivityLongTermStop = true)
        moveToBackgroundAndCommitSuicide()
        assertEquals(1, numTestActivities())
    }

    @Test
    fun testTopRestored_oneActivity_topInBackgroundLongTermStop() {
        launchActivities(count = 1, topActivityLongTermStop = true)
        moveToBackgroundAndCommitSuicide()
        assertTrue(device.hasObject(topActivitySelector))
    }

    @Test
    fun testSameNumActivities_oneActivity_topInForegroundLongTermSaveState() {
        launchActivities(count = 1, topActivityLongTermSaveState = true)
        commitSuicide()
        assertEquals(1, numTestActivities())
    }

    @Test
    fun testTopRestored_oneActivity_topInForegroundLongTermSaveState() {
        launchActivities(count = 1, topActivityLongTermSaveState = true)
        commitSuicide()
        assertTrue(device.hasObject(topActivitySelector))
    }

    @Test
    fun testSameNumActivities_oneActivity_topInBackgroundLongTermSaveState() {
        launchActivities(count = 1, topActivityLongTermSaveState = true)
        moveToBackgroundAndCommitSuicide()
        assertEquals(1, numTestActivities())
    }

    @Test
    fun testTopRestored_oneActivity_topInBackgroundLongTermSaveState() {
        launchActivities(count = 1, topActivityLongTermSaveState = true)
        moveToBackgroundAndCommitSuicide()
        assertTrue(device.hasObject(topActivitySelector))
    }

    @Test
    fun testSameNumActivities_multipleActivities_topInForeground() {
        launchActivities(count = MULTIPLE_ACTIVITIES_COUNT)
        commitSuicide()
        assertEquals(MULTIPLE_ACTIVITIES_COUNT, numTestActivities())
    }

    @Test
    fun testTopRestored_multipleActivities_topInForeground() {
        launchActivities(count = MULTIPLE_ACTIVITIES_COUNT)
        commitSuicide()
        assertTrue(device.hasObject(topActivitySelector))
    }

    @Test
    fun testSameNumActivities_multipleActivities_topInBackground() {
        launchActivities(count = MULTIPLE_ACTIVITIES_COUNT)
        moveToBackgroundAndCommitSuicide()
        assertEquals(MULTIPLE_ACTIVITIES_COUNT, numTestActivities())
    }

    @Test
    fun testTopRestored_multipleActivities_topInBackground() {
        launchActivities(count = MULTIPLE_ACTIVITIES_COUNT)
        moveToBackgroundAndCommitSuicide()
        assertTrue(device.hasObject(topActivitySelector))
    }

    @Test
    fun testSameNumActivities_multipleActivities_topInForegroundLongTermStop() {
        launchActivities(count = MULTIPLE_ACTIVITIES_COUNT, topActivityLongTermStop = true)
        commitSuicide()
        assertEquals(MULTIPLE_ACTIVITIES_COUNT, numTestActivities())
    }

    @Test
    fun testTopRestored_multipleActivities_topInForegroundLongTermStop() {
        launchActivities(count = MULTIPLE_ACTIVITIES_COUNT, topActivityLongTermStop = true)
        commitSuicide()
        assertTrue(device.hasObject(topActivitySelector))
    }

    @Test
    fun testSameNumActivities_multipleActivities_topInBackgroundLongTermStop() {
        launchActivities(count = MULTIPLE_ACTIVITIES_COUNT, topActivityLongTermStop = true)
        moveToBackgroundAndCommitSuicide()
        assertEquals(MULTIPLE_ACTIVITIES_COUNT, numTestActivities())
    }

    @Test
    fun testTopRestored_multipleActivities_topInBackgroundLongTermStop() {
        launchActivities(count = MULTIPLE_ACTIVITIES_COUNT, topActivityLongTermStop = true)
        moveToBackgroundAndCommitSuicide()
        assertTrue(device.hasObject(topActivitySelector))
    }

    @Test
    fun testSameNumActivities_multipleActivities_topInForegroundLongTermSaveState() {
        launchActivities(count = MULTIPLE_ACTIVITIES_COUNT, topActivityLongTermSaveState = true)
        commitSuicide()
        assertEquals(MULTIPLE_ACTIVITIES_COUNT, numTestActivities())
    }

    @Test
    fun testTopRestored_multipleActivities_topInForegroundLongTermSaveState() {
        launchActivities(count = MULTIPLE_ACTIVITIES_COUNT, topActivityLongTermSaveState = true)
        commitSuicide()
        assertTrue(device.hasObject(topActivitySelector))
    }

    @Test
    fun testSameNumActivities_multipleActivities_topInBackgroundLongTermSaveState() {
        launchActivities(count = MULTIPLE_ACTIVITIES_COUNT, topActivityLongTermSaveState = true)
        moveToBackgroundAndCommitSuicide()
        assertEquals(MULTIPLE_ACTIVITIES_COUNT, numTestActivities())
    }

    @Test
    fun testTopRestored_multipleActivities_topInBackgroundLongTermSaveState() {
        launchActivities(count = MULTIPLE_ACTIVITIES_COUNT, topActivityLongTermSaveState = true)
        moveToBackgroundAndCommitSuicide()
        assertTrue(device.hasObject(topActivitySelector))
    }

    private fun launchActivities(
        count: Int,
        topActivityLongTermStop: Boolean = false,
        topActivityLongTermSaveState: Boolean = false
    ) {
        val intent = VenomTestActivity.launchIntent(
            context = getApplicationContext(),
            countLeft = count - 1,
            topActivityLongTermStop = topActivityLongTermStop,
            topActivityLongTermSaveSate = topActivityLongTermSaveState
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        getApplicationContext().startActivity(intent)
        device.wait(Until.hasObject(topActivitySelector), WAIT_TIMEOUT)
    }

    private fun commitSuicide() {
        val killBtnSelector =
            By.desc(getApplicationContext().getString(R.string.venom_notification_button_kill))

        device.openNotification()
        device.wait(Until.findObject(killBtnSelector), WAIT_TIMEOUT)
        device.findObject(killBtnSelector).click()
        collapseNotifications()

        device.wait(Until.gone(topActivitySelector), WAIT_TIMEOUT)
        device.wait(Until.hasObject(anyActivitySelector), WAIT_TIMEOUT)
    }

    private fun collapseNotifications() {
        getApplicationContext().sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
    }

    private fun numTestActivities(): Int {
        val am =
            getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
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
        private const val MULTIPLE_ACTIVITIES_COUNT = 5

        private val anyActivitySelector = By.textStartsWith(VenomTestActivity.TITLE_PREFIX)
        private val topActivitySelector = By.text(VenomTestActivity.TITLE_PREFIX + 0)

        private fun getApplicationContext() =
            InstrumentationRegistry.getInstrumentation().targetContext

        @JvmStatic
        @BeforeClass
        fun setupAll() {
            Venom.createInstance(getApplicationContext()).also {
                Venom.setGlobalInstance(it)
            }
        }
    }
}