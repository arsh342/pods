package com.arsh.pods.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BatteryAlertsTest {
    /** Feeds readings through the alert logic and returns the thresholds that notified. */
    private fun alertsFor(vararg levels: Int, charging: Boolean = false): List<Int> {
        var lowestSent: Int? = null
        return levels.toList().mapNotNull { level ->
            val decision = nextBatteryAlert(level, charging, lowestSent)
            lowestSent = decision.lowestSent
            decision.notify
        }
    }

    @Test
    fun notifiesOnceAtEachThresholdWhileDraining() {
        assertEquals(listOf(10, 5, 2, 1), alertsFor(12, 11, 10, 10, 9, 6, 5, 5, 4, 3, 2, 2, 1, 1))
    }

    @Test
    fun aBigDropNotifiesOnlyTheLowestThresholdReached() {
        assertEquals(listOf(5), alertsFor(20, 4, 4))
        assertEquals(listOf(1), alertsFor(30, 1))
    }

    @Test
    fun aReadingThatWobblesAroundTenDoesNotRepeat() {
        assertEquals(listOf(10), alertsFor(10, 11, 10, 12, 9))
    }

    @Test
    fun startsAgainAfterRechargingToFifteen() {
        assertEquals(listOf(10, 10), alertsFor(10, 15, 10))
    }

    @Test
    fun staysQuietWhileCharging() {
        assertEquals(emptyList<Int>(), alertsFor(10, 5, 2, 1, charging = true))
    }

    @Test
    fun ignoresUnknownLevels() {
        assertNull(nextBatteryAlert(0, charging = false, lowestSent = null).notify)
        assertNull(nextBatteryAlert(-1, charging = false, lowestSent = null).notify)
        assertEquals(5, nextBatteryAlert(0, charging = false, lowestSent = 5).lowestSent)
    }

    @Test
    fun rememberedStatePreventsRepeatsAfterARestart() {
        assertNull(nextBatteryAlert(9, charging = false, lowestSent = 10).notify)
        assertEquals(5, nextBatteryAlert(5, charging = false, lowestSent = 10).notify)
    }
}
