package com.arsh.pods.presentation

import com.arsh.pods.data.Capability
import com.arsh.pods.data.AirPodsModels
import com.arsh.pods.presentation.navigation.MainSetting
import com.arsh.pods.presentation.navigation.MainTab
import com.arsh.pods.presentation.navigation.settingsOn
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MainTabTest {
    private val everything = Capability.entries.toSet()

    private fun all(capabilities: Set<Capability>, vendor: Boolean) =
        MainTab.entries.flatMap { settingsOn(it, capabilities, vendor) }

    @Test
    fun everySettingIsReachableExactlyOnce() {
        val shown = all(everything, vendor = true)

        assertEquals(MainSetting.entries.toSet(), shown.toSet())
        assertEquals(shown.size, shown.toSet().size)
    }

    @Test
    fun homeShowsOnlyTheEssentials() {
        assertEquals(
            listOf(
                MainSetting.BATTERY, MainSetting.LISTENING_MODE,
                MainSetting.CONVERSATION_AWARENESS, MainSetting.EQUALIZER, MainSetting.DISCONNECT
            ),
            settingsOn(MainTab.HOME, everything, true)
        )
    }

    @Test
    fun modelWithoutCapabilitiesKeepsOnlyUniversalSettings() {
        assertEquals(
            listOf(MainSetting.BATTERY, MainSetting.EQUALIZER, MainSetting.DISCONNECT),
            settingsOn(MainTab.HOME, emptySet(), false)
        )
        assertEquals(listOf(MainSetting.CALL_CONTROLS), settingsOn(MainTab.CONTROLS, emptySet(), false))
        assertEquals(listOf(MainSetting.MICROPHONE), settingsOn(MainTab.SOUND, emptySet(), false))
        assertFalse(MainSetting.SLEEP_DETECTION in settingsOn(MainTab.SETTINGS, emptySet(), false))
        assertTrue(MainSetting.APP_SETTINGS in settingsOn(MainTab.SETTINGS, emptySet(), false))
    }

    @Test
    fun rootOnlyItemsNeedTheVendorSetting() {
        val without = all(everything, vendor = false)

        assertFalse(MainSetting.HEARING_AID in without)
        assertFalse(MainSetting.LOUD_SOUND_REDUCTION in without)
        assertTrue(MainSetting.HEARING_PROTECTION in without)
    }

    @Test
    fun airPods4AncGetsItsControls() {
        val capabilities = AirPodsModels.getModelByModelNumber("A3055")!!.capabilities

        assertEquals(
            listOf(MainSetting.PRESS_AND_HOLD, MainSetting.CALL_CONTROLS, MainSetting.HEAD_GESTURES),
            settingsOn(MainTab.CONTROLS, capabilities, false)
        )
        assertEquals(
            listOf(MainSetting.PERSONALIZED_VOLUME, MainSetting.ADAPTIVE_AUDIO, MainSetting.MICROPHONE),
            settingsOn(MainTab.SOUND, capabilities, false)
        )
    }

    @Test
    fun caseSoundsAppearOnlyForModelsWhoseCaseHasASpeaker() {
        fun settingsFor(model: String) = settingsOn(
            MainTab.SETTINGS, AirPodsModels.getModelByModelNumber(model)!!.capabilities, false
        )

        // AirPods 4 (ANC), Pro 2 (Lightning and USB-C) and Pro 3
        listOf("A3055", "A2931", "A3047", "A3064").forEach {
            assertTrue(it, MainSetting.CASE_SOUNDS in settingsFor(it))
        }
        // AirPods 4 without ANC has no case speaker
        assertFalse(MainSetting.CASE_SOUNDS in settingsFor("A3053"))
    }
}
