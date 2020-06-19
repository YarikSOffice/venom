package com.github.venom.service

import android.app.ActivityManager
import android.app.ActivityManager.RunningServiceInfo
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.os.Build
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ServiceDelegateTest {

    private val context = mockk<Context>(relaxed = true)

    private lateinit var delegate: ServiceDelegate

    @Before
    fun setup() {
        delegate = ServiceDelegate(context)
    }

    @Test
    fun startService_startServiceIfNotRunning() {
        setRunningState(false)
        delegate.startService()
        // it's not possible to verify the Intent unfortunately
        verify {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(any())
            } else {
                context.startService(any())
            }
        }
    }

    @Test
    fun startService_dontStartServiceIfAlreadyRunning() {
        setRunningState(true)
        delegate.startService()

        verify(exactly = 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(any())
            } else {
                context.startService(any())
            }
        }
    }

    @Test
    fun stopService_stopService() {
        delegate.stopService()

        verify { context.stopService(any()) }
    }

    @Test
    fun isServiceRunning_returnTrueIfRunning() {
        setRunningState(true)

        assertTrue(delegate.isServiceRunning())
    }

    @Test
    fun isServiceRunning_returnFalseIfNotRunning() {
        setRunningState(false)

        assertFalse(delegate.isServiceRunning())
    }

    @Suppress("DEPRECATION")
    private fun setRunningState(running: Boolean) {
        val serviceInfo = mockk<RunningServiceInfo>()
        serviceInfo.service = mockk {
            every { className } returns if (running) VenomService::class.java.name else ""
        }
        every {
            context.getSystemService(ACTIVITY_SERVICE)
        } returns mockk<ActivityManager> {
            every { getRunningServices(any()) } returns listOf(serviceInfo)
        }
    }
}
