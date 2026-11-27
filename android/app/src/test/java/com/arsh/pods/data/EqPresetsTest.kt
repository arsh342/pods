package com.arsh.pods.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EqPresetsTest {
    private fun eq(low: Int, mid: Int, high: Int) = CustomEq(state = 2, low = low, mid = mid, high = high)

    @Test
    fun noPresetRepeatsFlat() {
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
        assertEquals(EqPreset.ACOUSTIC, EqPreset.matching(eq(46, 56, 60)))
    }

    @Test
    fun aHandMadeSettingIsNoPreset() {
        assertNull(EqPreset.matching(eq(51, 50, 50)))
    }

    @Test
    fun theGridWithFlatAndCustomFillsEveryRow() {
        // Shown two to a row: Flat, the presets, then the saved custom setting.
        assertEquals(0, (EqPreset.entries.size + 2) % 2)
    }

    private val fresh = EqMemory()
    private val handMade = EqBands(60, 50, 40)

    @Test
    fun aFreshStartShowsFlat() {
        assertEquals(EqChoice.Flat, fresh.choice(CustomEq(state = 1, low = 50, mid = 50, high = 50)))
        assertEquals(EqChoice.Flat, fresh.choice(eq(50, 50, 50)))
        // Bands left over while the equalizer is off are not heard.
        assertEquals(EqChoice.Flat, fresh.choice(CustomEq(state = 1, low = 80, mid = 50, high = 50)))
    }

    @Test
    fun aPresetShowsItself() {
        assertEquals(EqChoice.Preset(EqPreset.BASS_BOOST), fresh.picked().choice(eq(80, 50, 50)))
    }

    @Test
    fun anEditIsSavedAndShownAsCustom() {
        val memory = fresh.edited(handMade)

        assertEquals(handMade, memory.saved)
        assertEquals(EqChoice.Custom, memory.choice(eq(60, 50, 40)))
    }

    @Test
    fun anEditThatLandsOnAPresetIsStillCustom() {
        val memory = fresh.edited(EqBands(80, 50, 50))

        assertEquals(EqChoice.Custom, memory.choice(eq(80, 50, 50)))
        // Picking that preset afterwards shows the preset.
        assertEquals(EqChoice.Preset(EqPreset.BASS_BOOST), memory.picked().choice(eq(80, 50, 50)))
    }

    @Test
    fun pickingSomethingElseKeepsTheSavedSetting() {
        val memory = fresh.edited(handMade).picked()

        assertEquals(handMade, memory.saved)
        assertEquals(EqChoice.Flat, memory.choice(eq(50, 50, 50)))
        assertEquals(EqChoice.Custom, memory.restored().choice(eq(60, 50, 40)))
    }

    @Test
    fun editingBackToFlatForgetsTheSavedSetting() {
        assertEquals(EqMemory(), fresh.edited(handMade).edited(EqBands(50, 50, 50)))
    }

    @Test
    fun aHandMadeSettingFromTheAirPodsIsSaved() {
        val memory = fresh.reported(eq(60, 50, 40))

        assertEquals(handMade, memory.saved)
        assertEquals(EqChoice.Custom, memory.choice(eq(60, 50, 40)))
    }

    @Test
    fun reportsOfFlatPresetsOrAnEqualizerThatIsOffChangeNothing() {
        val memory = fresh.edited(handMade).picked()

        assertEquals(memory, memory.reported(eq(50, 50, 50)))
        assertEquals(memory, memory.reported(eq(80, 50, 50)))
        assertEquals(memory, memory.reported(CustomEq(state = 1, low = 70, mid = 50, high = 50)))
    }
}
