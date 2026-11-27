package com.arsh.pods.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EqPresetsTest {
    private fun eq(low: Int, mid: Int, high: Int) = CustomEq(state = 2, low = low, mid = mid, high = high)

    @Test
    fun noPresetRepeatsTheAirPodsOwnTuning() {
        assertNull(EqPreset.matching(eq(50, 50, 50)))
    }

    @Test
    fun everyPresetIsAValidSettingAndDifferent() {
        EqPreset.entries.forEach {
            assertTrue(it.name, it.low in 0..100 && it.mid in 0..100 && it.high in 0..100)
        }
        assertEquals(EqPreset.entries.size, EqPreset.entries.map { Triple(it.low, it.mid, it.high) }.toSet().size)
    }

    @Test
    fun recognisesTheCurrentSettingAsAPreset() {
        assertEquals(EqPreset.BASS_BOOST, EqPreset.matching(eq(80, 50, 50)))
        assertEquals(EqPreset.WARM, EqPreset.matching(eq(62, 54, 40)))
    }

    @Test
    fun aHandMadeSettingIsNoPreset() {
        assertNull(EqPreset.matching(eq(51, 50, 50)))
    }

    @Test
    fun theGridWithTheAirPodsDefaultFillsEveryRow() {
        // The presets are shown two to a row, after the AirPods' own default.
        assertEquals(0, (EqPreset.entries.size + 1) % 2)
    }
}
