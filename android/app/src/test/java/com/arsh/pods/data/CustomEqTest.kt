package com.arsh.pods.data

import com.arsh.pods.bluetooth.AACPManager
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CustomEqTest {
    private val header = byteArrayOf(0x04, 0x00, 0x04, 0x00)

    @Test
    fun encodesPacket() {
        assertArrayEquals(
            byteArrayOf(0x63, 0x00, 0x05, 0x00, 0x01, 0x02, 10, 50, 90),
            CustomEq(state = 2, low = 10, mid = 50, high = 90).toPacket()
        )
    }

    @Test
    fun parsesWhatItEncodes() {
        val eq = CustomEq(state = 2, low = 0, mid = 55, high = 100)

        assertEquals(eq, AACPManager().parseCustomEqPacket(header + eq.toPacket()))
    }

    @Test
    fun clampsOutOfRangeBandsInsteadOfCrashing() {
        val packet = header + byteArrayOf(0x63, 0x00, 0x05, 0x00, 0x01, 0x02, -1, 101, 50)

        assertEquals(CustomEq(2, 100, 100, 50), AACPManager().parseCustomEqPacket(packet))
    }

    @Test
    fun changesBandsWithinRangeAndKeepsState() {
        val eq = CustomEq(state = 2, low = 50, mid = 50, high = 50)

        assertEquals(CustomEq(2, 0, 70, 100), eq.withBands(low = -5, mid = 70, high = 130))
    }

    @Test
    fun togglesEnabledState() {
        val eq = CustomEq(state = 1, low = 20, mid = 30, high = 40)

        assertTrue(eq.withEnabled(true).isEnabled())
        assertFalse(eq.withEnabled(true).withEnabled(false).isEnabled())
        assertEquals(20, eq.withEnabled(true).low)
    }

    @Test
    fun isOfferedOnAirPods4AncButNotAirPods3() {
        assertTrue(Capability.CUSTOM_EQ in AirPodsModels.getModelByModelNumber("A3055")!!.capabilities)
        assertFalse(Capability.CUSTOM_EQ in AirPodsModels.getModelByModelNumber("A2565")!!.capabilities)
    }
}
