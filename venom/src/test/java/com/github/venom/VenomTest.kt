package com.github.venom

import android.content.Context
import com.github.venom.service.ServiceDelegate
import com.github.venom.service.VenomNotificationManager
import com.github.venom.service.defaultNotification
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class VenomTest {

    private val preferenceManager = mockk<VenomPreferenceManager>(relaxed = true)
    private val notificationManager = mockk<VenomNotificationManager>(relaxed = true)
    private val delegate = mockk<ServiceDelegate>(relaxed = true)

    private lateinit var venom: Venom

    @Before
    fun setup() {
        venom = Venom.createInstance(preferenceManager, notificationManager, delegate)
    }

    @Test
    fun initialize_setNotificationConfigDefault() {
        venom.initialize()

        verify(exactly = 0) { notificationManager.config }
    }

    @Test
    fun initialize_setNotificationConfig() {
        val context = mockk<Context>()
        every { context.getString(any()) } returns ""
        val notification = defaultNotification(context)

        venom.initialize(notification)

        verify { notificationManager.config = notification }
    }

    @Test
    fun initialize_startIfActive() {
        setActive()
        venom.initialize()

        verify { delegate.startService() }
    }

    @Test
    fun initialize_dontStartIfInactive() {
        setInactive()
        venom.initialize()

        verify(exactly = 0) { delegate.startService() }
    }

    @Test
    fun start_persistSetting() {
        venom.start()

        verify { preferenceManager.setActive(true) }
    }

    @Test
    fun start_startService() {
        venom.start()

        verify { delegate.startService() }
    }

    @Test
    fun stop_persistSetting() {
        venom.stop()

        verify { preferenceManager.setActive(false) }
    }

    @Test
    fun stopService() {
        venom.stop()

        verify { delegate.stopService() }
    }

    @Test
    fun isRunning_returnTrueWhenRunning() {
        every { delegate.isServiceRunning() } returns true

        assertTrue(venom.isRunning())
    }

    @Test
    fun isRunning_returnFalseWhenNotRunning() {
        every { delegate.isServiceRunning() } returns false

        assertFalse(venom.isRunning())
    }

    private fun setActive() {
        every { preferenceManager.isActive() } returns true
    }

    private fun setInactive() {
        every { preferenceManager.isActive() } returns false
    }
}
