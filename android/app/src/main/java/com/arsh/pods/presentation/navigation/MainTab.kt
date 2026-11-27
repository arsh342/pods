package com.arsh.pods.presentation.navigation

import com.arsh.pods.data.Capability

enum class MainTab { HOME, CONTROLS, SOUND, SETTINGS }

enum class MainSetting {
    BATTERY, LISTENING_MODE, CONVERSATION_AWARENESS, EQUALIZER, DISCONNECT,
    PRESS_AND_HOLD, CALL_CONTROLS, HEAD_GESTURES,
    PERSONALIZED_VOLUME, ADAPTIVE_AUDIO, MICROPHONE, LOUD_SOUND_REDUCTION, HEARING_PROTECTION, HEARING_AID,
    NAME, EAR_DETECTION, AUTO_CONNECT, OPTIMIZED_CHARGING, SLEEP_DETECTION, CASE_SOUNDS, OFF_LISTENING_MODE,
    ACCESSIBILITY, APP_SETTINGS, ABOUT
}

/** The settings shown on [tab], in display order. Items the AirPods cannot do are left out. */
fun settingsOn(tab: MainTab, capabilities: Set<Capability>, vendorIdHook: Boolean): List<MainSetting> {
    fun has(capability: Capability) = capability in capabilities
    fun MainSetting.onlyIf(condition: Boolean) = takeIf { condition }

    return when (tab) {
            MainTab.HOME -> listOf(
                MainSetting.BATTERY,
                MainSetting.LISTENING_MODE.onlyIf(has(Capability.LISTENING_MODE)),
                MainSetting.CONVERSATION_AWARENESS.onlyIf(has(Capability.CONVERSATION_AWARENESS)),
                MainSetting.EQUALIZER,
                MainSetting.DISCONNECT
            )
            MainTab.CONTROLS -> listOf(
                MainSetting.PRESS_AND_HOLD.onlyIf(has(Capability.STEM_CONFIG)),
                MainSetting.CALL_CONTROLS,
                MainSetting.HEAD_GESTURES.onlyIf(has(Capability.HEAD_GESTURES))
            )
            MainTab.SOUND -> listOf(
                MainSetting.PERSONALIZED_VOLUME.onlyIf(has(Capability.ADAPTIVE_VOLUME)),
                MainSetting.ADAPTIVE_AUDIO.onlyIf(has(Capability.ADAPTIVE_VOLUME)),
                MainSetting.MICROPHONE,
                MainSetting.LOUD_SOUND_REDUCTION.onlyIf(has(Capability.LOUD_SOUND_REDUCTION) && vendorIdHook),
                MainSetting.HEARING_PROTECTION.onlyIf(has(Capability.PPE)),
                MainSetting.HEARING_AID.onlyIf(has(Capability.HEARING_AID) && vendorIdHook)
            )
            MainTab.SETTINGS -> listOf(
                MainSetting.NAME,
                MainSetting.EAR_DETECTION,
                MainSetting.AUTO_CONNECT,
                MainSetting.OPTIMIZED_CHARGING,
                MainSetting.SLEEP_DETECTION.onlyIf(has(Capability.SLEEP_DETECTION)),
                MainSetting.CASE_SOUNDS.onlyIf(has(Capability.CASE_SOUNDS)),
                MainSetting.OFF_LISTENING_MODE.onlyIf(has(Capability.LOUD_SOUND_REDUCTION)),
                MainSetting.ACCESSIBILITY,
                MainSetting.APP_SETTINGS,
                MainSetting.ABOUT
            )
    }.filterNotNull()
}
