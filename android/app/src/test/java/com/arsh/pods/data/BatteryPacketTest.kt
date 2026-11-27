package com.arsh.pods.data

import org.junit.Assert.assertEquals
import org.junit.Test

class BatteryPacketTest {
    private fun packet(vararg bytes: Int) = ByteArray(bytes.size) { bytes[it].toByte() }

    private val headphones = packet(4, 0, 4, 0, 4, 0, 1, 1, 1, 64, 2, 1)
    private val earbuds = packet(
        4, 0, 4, 0, 4, 0, 3,
        4, 1, 90, 2, 1,
        2, 1, 80, 1, 1,
        8, 1, 50, 2, 1
    )

    private fun read(vararg packets: ByteArray): List<Battery> {
        val notification = AirPodsNotifications.BatteryNotification()
        packets.forEach { notification.setBattery(it) }
        return notification.getBattery()
    }

    @Test
    fun readsTheSingleBatteryOfAirPodsMax() {
        val battery = read(headphones).single { it.component == BatteryComponent.HEADSET }

        assertEquals(Battery(BatteryComponent.HEADSET, 64, BatteryStatus.NOT_CHARGING), battery)
    }

    @Test
    fun keepsThePlacesForEarbudsEmptyForHeadphones() {
        val battery = read(headphones)

        assertEquals(
            listOf(BatteryStatus.DISCONNECTED, BatteryStatus.DISCONNECTED, BatteryStatus.DISCONNECTED),
            battery.take(3).map { it.status }
        )
    }

    @Test
    fun earbudsReplaceAnyHeadphoneReading() {
        val battery = read(headphones, earbuds)

        assertEquals(listOf(90, 80, 50), battery.map { it.level })
    }
}
