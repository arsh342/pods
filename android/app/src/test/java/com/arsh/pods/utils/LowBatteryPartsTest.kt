package com.arsh.pods.utils

import com.arsh.pods.data.Battery
import com.arsh.pods.data.BatteryComponent.CASE
import com.arsh.pods.data.BatteryComponent.HEADSET
import com.arsh.pods.data.BatteryComponent.LEFT
import com.arsh.pods.data.BatteryComponent.RIGHT
import com.arsh.pods.data.BatteryStatus.CHARGING
import com.arsh.pods.data.BatteryStatus.DISCONNECTED
import com.arsh.pods.data.BatteryStatus.NOT_CHARGING
import com.arsh.pods.data.BatteryStatus.OPTIMIZED_CHARGING
import org.junit.Assert.assertEquals
import org.junit.Test

class LowBatteryPartsTest {
    @Test
    fun listsEveryPartAtTenPercentOrBelowInLeftRightCaseOrder() {
        val battery = listOf(
            Battery(CASE, 9, NOT_CHARGING),
            Battery(RIGHT, 10, NOT_CHARGING),
            Battery(LEFT, 11, NOT_CHARGING)
        )

        assertEquals(listOf(RIGHT, CASE), lowBatteryParts(battery).map { it.component })
    }

    @Test
    fun leavesOutPartsThatAreChargingAbsentOrUnknown() {
        val battery = listOf(
            Battery(LEFT, 5, CHARGING),
            Battery(RIGHT, 5, OPTIMIZED_CHARGING),
            Battery(CASE, 5, DISCONNECTED),
            Battery(CASE, 0, NOT_CHARGING)
        )

        assertEquals(emptyList<Battery>(), lowBatteryParts(battery))
    }

    @Test
    fun includesHeadphones() {
        val battery = listOf(Battery(HEADSET, 7, NOT_CHARGING))

        assertEquals(listOf(HEADSET), lowBatteryParts(battery).map { it.component })
    }

    @Test
    fun theNotificationShowsEveryPartWithAKnownLevelAsTheAppDoes() {
        val battery = listOf(
            Battery(CASE, 60, NOT_CHARGING),
            Battery(RIGHT, 40, CHARGING),
            Battery(LEFT, 8, NOT_CHARGING)
        )

        assertEquals(listOf(LEFT, RIGHT, CASE), shownBatteryParts(battery).map { it.component })
    }

    @Test
    fun theNotificationLeavesOutAbsentAndUnknownParts() {
        val battery = listOf(
            Battery(LEFT, 8, NOT_CHARGING),
            Battery(RIGHT, 0, NOT_CHARGING),
            Battery(CASE, 50, DISCONNECTED)
        )

        assertEquals(listOf(LEFT), shownBatteryParts(battery).map { it.component })
    }
}
