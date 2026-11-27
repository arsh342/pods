/*
    Pods - AirPods liberated from Apple’s ecosystem
    Copyright (C) 2025 LibrePods contributors

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.arsh.pods.data

import android.content.Context
import com.arsh.pods.R

open class AirPodsBase(
    val modelNumber: List<String>,
    val name: String,
    val displayName: String = "AirPods",
    val manufacturer: String = "Apple Inc.",
    val budCaseRes: Int,
    val budsRes: Int,
    val leftBudsRes: Int,
    val rightBudsRes: Int,
    val caseRes: Int,
    val capabilities: Set<Capability>,
    // Small one-colour drawings for the widget.
    val leftIconRes: Int = R.drawable.airpods_pro_left_notification,
    val rightIconRes: Int = R.drawable.airpods_pro_right_notification,
    val caseIconRes: Int = R.drawable.airpods_pro_case_notification,
    // Both earbuds together, for notifications and tiles.
    val statusIconRes: Int = R.drawable.ic_status_airpods_pro,
    // One headset with a single battery, instead of two earbuds and a case.
    val isHeadphones: Boolean = false
)
enum class Capability {
    LISTENING_MODE,
    CONVERSATION_AWARENESS,
    STEM_CONFIG,
    HEAD_GESTURES,
    LOUD_SOUND_REDUCTION,
    PPE,
    SLEEP_DETECTION,
    HEARING_AID,
    ADAPTIVE_AUDIO,
    ADAPTIVE_VOLUME,
    SWIPE_FOR_VOLUME,
    HRM,
    CUSTOM_EQ,
    // The charging case has a speaker whose sounds can be switched off.
    CASE_SOUNDS
}

class AirPods: AirPodsBase(
    modelNumber = listOf("A1523", "A1722"),
    name = "AirPods 1",
    budCaseRes = R.drawable.airpods_1_case,
    budsRes = R.drawable.airpods_1_buds,
    leftBudsRes = R.drawable.airpods_1_left,
    rightBudsRes = R.drawable.airpods_1_right,
    caseRes = R.drawable.airpods_1_case,
    leftIconRes = R.drawable.airpods_classic_left_notification,
    rightIconRes = R.drawable.airpods_classic_right_notification,
    caseIconRes = R.drawable.airpods_classic_case_notification,
    statusIconRes = R.drawable.ic_status_airpods_classic,
    capabilities = emptySet()
)

class AirPods2: AirPodsBase(
    modelNumber = listOf("A2032", "A2031"),
    name = "AirPods 2",
    budCaseRes = R.drawable.airpods_2_case,
    budsRes = R.drawable.airpods_2_buds,
    leftBudsRes = R.drawable.airpods_2_left,
    rightBudsRes = R.drawable.airpods_2_right,
    caseRes = R.drawable.airpods_2_case,
    leftIconRes = R.drawable.airpods_classic_left_notification,
    rightIconRes = R.drawable.airpods_classic_right_notification,
    caseIconRes = R.drawable.airpods_classic_case_notification,
    statusIconRes = R.drawable.ic_status_airpods_classic,
    capabilities = emptySet()
)

class AirPods3: AirPodsBase(
    modelNumber = listOf("A2565", "A2564"),
    name = "AirPods 3",
    budCaseRes = R.drawable.airpods_3_case,
    budsRes = R.drawable.airpods_3_buds,
    leftBudsRes = R.drawable.airpods_3_left,
    rightBudsRes = R.drawable.airpods_3_right,
    caseRes = R.drawable.airpods_3_case,
    leftIconRes = R.drawable.airpods_4_left_notification,
    rightIconRes = R.drawable.airpods_4_right_notification,
    caseIconRes = R.drawable.airpods_4_case_notification,
    statusIconRes = R.drawable.ic_status_airpods_open,
    capabilities = setOf(
        Capability.HEAD_GESTURES
    )
)

class AirPods4: AirPodsBase(
    modelNumber = listOf("A3053", "A3050", "A3054"),
    name = "AirPods 4",
    budCaseRes = R.drawable.airpods_4_case,
    budsRes = R.drawable.airpods_4_buds,
    leftBudsRes = R.drawable.airpods_4_left,
    rightBudsRes = R.drawable.airpods_4_right,
    caseRes = R.drawable.airpods_4_case,
    leftIconRes = R.drawable.airpods_4_left_notification,
    rightIconRes = R.drawable.airpods_4_right_notification,
    caseIconRes = R.drawable.airpods_4_case_notification,
    statusIconRes = R.drawable.ic_status_airpods_open,
    capabilities = setOf(
        Capability.CUSTOM_EQ,
        Capability.HEAD_GESTURES,
        Capability.SLEEP_DETECTION,
        Capability.ADAPTIVE_VOLUME
    )
)

class AirPods4ANC: AirPodsBase(
    modelNumber = listOf("A3056", "A3055", "A3057"),
    name = "AirPods 4 (ANC)",
    budCaseRes = R.drawable.airpods_4_anc_case,
    budsRes = R.drawable.airpods_4_anc_buds,
    leftBudsRes = R.drawable.airpods_4_anc_left,
    rightBudsRes = R.drawable.airpods_4_anc_right,
    caseRes = R.drawable.airpods_4_anc_case,
    leftIconRes = R.drawable.airpods_4_left_notification,
    rightIconRes = R.drawable.airpods_4_right_notification,
    caseIconRes = R.drawable.airpods_4_case_notification,
    statusIconRes = R.drawable.ic_status_airpods_open,
    capabilities = setOf(
        Capability.CASE_SOUNDS,
        Capability.CUSTOM_EQ,
        Capability.LISTENING_MODE,
        Capability.CONVERSATION_AWARENESS,
        Capability.HEAD_GESTURES,
        Capability.ADAPTIVE_AUDIO,
        Capability.SLEEP_DETECTION,
        Capability.ADAPTIVE_VOLUME,
        Capability.STEM_CONFIG
    )
)

class AirPods5: AirPodsBase(
    modelNumber = listOf("A3531", "A3532", "A3533"),
    name = "AirPods 5",
    budCaseRes = R.drawable.airpods_5_case,
    budsRes = R.drawable.airpods_5_buds,
    leftBudsRes = R.drawable.airpods_5_left,
    rightBudsRes = R.drawable.airpods_5_right,
    caseRes = R.drawable.airpods_5_case,
    leftIconRes = R.drawable.airpods_4_left_notification,
    rightIconRes = R.drawable.airpods_4_right_notification,
    caseIconRes = R.drawable.airpods_4_case_notification,
    statusIconRes = R.drawable.ic_status_airpods_open,
    capabilities = setOf(
        Capability.CUSTOM_EQ,
        Capability.LISTENING_MODE,
        Capability.CONVERSATION_AWARENESS,
        Capability.HEAD_GESTURES,
        Capability.ADAPTIVE_AUDIO,
        Capability.SLEEP_DETECTION,
        Capability.ADAPTIVE_VOLUME,
        Capability.STEM_CONFIG
    )
)

class AirPods5WCC: AirPodsBase(
    modelNumber = listOf("A3439", "A3440", "A3441"),
    name = "AirPods 5 with Wireless Charging Case",
    budCaseRes = R.drawable.airpods_5_wireless_case,
    budsRes = R.drawable.airpods_5_wireless_buds,
    leftBudsRes = R.drawable.airpods_5_wireless_left,
    rightBudsRes = R.drawable.airpods_5_wireless_right,
    caseRes = R.drawable.airpods_5_wireless_case,
    leftIconRes = R.drawable.airpods_4_left_notification,
    rightIconRes = R.drawable.airpods_4_right_notification,
    caseIconRes = R.drawable.airpods_4_case_notification,
    statusIconRes = R.drawable.ic_status_airpods_open,
    capabilities = setOf(
        Capability.CUSTOM_EQ,
        Capability.LISTENING_MODE,
        Capability.CONVERSATION_AWARENESS,
        Capability.HEAD_GESTURES,
        Capability.ADAPTIVE_AUDIO,
        Capability.SLEEP_DETECTION,
        Capability.ADAPTIVE_VOLUME,
        Capability.STEM_CONFIG,
        Capability.SWIPE_FOR_VOLUME
    )
)

class AirPodsPro1: AirPodsBase(
    modelNumber = listOf("A2084", "A2083"),
    name = "AirPods Pro 1",
    displayName = "AirPods Pro",
    budCaseRes = R.drawable.airpods_pro_1_case,
    budsRes = R.drawable.airpods_pro_1_buds,
    leftBudsRes = R.drawable.airpods_pro_1_left,
    rightBudsRes = R.drawable.airpods_pro_1_right,
    caseRes = R.drawable.airpods_pro_1_case,
    leftIconRes = R.drawable.airpods_pro_left_notification,
    rightIconRes = R.drawable.airpods_pro_right_notification,
    caseIconRes = R.drawable.airpods_pro_case_notification,
    statusIconRes = R.drawable.ic_status_airpods_pro,
    capabilities = setOf(
        Capability.LISTENING_MODE
    )
)

class AirPodsPro2Lightning: AirPodsBase(
    modelNumber = listOf("A2931", "A2699", "A2698"),
    name = "AirPods Pro 2 with Magsafe Charging Case (Lightning)",
    displayName = "AirPods Pro",
    budCaseRes = R.drawable.airpods_pro_2_case,
    budsRes = R.drawable.airpods_pro_2_buds,
    leftBudsRes = R.drawable.airpods_pro_2_left,
    rightBudsRes = R.drawable.airpods_pro_2_right,
    caseRes = R.drawable.airpods_pro_2_case,
    leftIconRes = R.drawable.airpods_pro_left_notification,
    rightIconRes = R.drawable.airpods_pro_right_notification,
    caseIconRes = R.drawable.airpods_pro_case_notification,
    statusIconRes = R.drawable.ic_status_airpods_pro,
    capabilities = setOf(
        Capability.CASE_SOUNDS,
        Capability.CUSTOM_EQ,
        Capability.LISTENING_MODE,
        Capability.CONVERSATION_AWARENESS,
        Capability.STEM_CONFIG,
        Capability.LOUD_SOUND_REDUCTION,
        Capability.SLEEP_DETECTION,
        Capability.HEARING_AID,
        Capability.ADAPTIVE_AUDIO,
        Capability.ADAPTIVE_VOLUME,
        Capability.SWIPE_FOR_VOLUME,
        Capability.HEAD_GESTURES
    )
)

class AirPodsPro2USBC: AirPodsBase(
    modelNumber = listOf("A3047", "A3048", "A3049"),
    name = "AirPods Pro 2 with Magsafe Charging Case (USB-C)",
    displayName = "AirPods Pro",
    budCaseRes = R.drawable.airpods_pro_2_case,
    budsRes = R.drawable.airpods_pro_2_buds,
    leftBudsRes = R.drawable.airpods_pro_2_left,
    rightBudsRes = R.drawable.airpods_pro_2_right,
    caseRes = R.drawable.airpods_pro_2_case,
    leftIconRes = R.drawable.airpods_pro_left_notification,
    rightIconRes = R.drawable.airpods_pro_right_notification,
    caseIconRes = R.drawable.airpods_pro_case_notification,
    statusIconRes = R.drawable.ic_status_airpods_pro,
    capabilities = setOf(
        Capability.CASE_SOUNDS,
        Capability.CUSTOM_EQ,
        Capability.LISTENING_MODE,
        Capability.CONVERSATION_AWARENESS,
        Capability.STEM_CONFIG,
        Capability.LOUD_SOUND_REDUCTION,
        Capability.SLEEP_DETECTION,
        Capability.HEARING_AID,
        Capability.ADAPTIVE_AUDIO,
        Capability.ADAPTIVE_VOLUME,
        Capability.SWIPE_FOR_VOLUME,
        Capability.HEAD_GESTURES
    )
)

class AirPodsPro3: AirPodsBase(
    modelNumber = listOf("A3063", "A3064", "A3065"),
    name = "AirPods Pro 3",
    displayName = "AirPods Pro",
    budCaseRes = R.drawable.airpods_pro_3_case,
    budsRes = R.drawable.airpods_pro_3_buds,
    leftBudsRes = R.drawable.airpods_pro_3_left,
    rightBudsRes = R.drawable.airpods_pro_3_right,
    caseRes = R.drawable.airpods_pro_3_case,
    leftIconRes = R.drawable.airpods_pro_left_notification,
    rightIconRes = R.drawable.airpods_pro_right_notification,
    caseIconRes = R.drawable.airpods_pro_case_notification,
    statusIconRes = R.drawable.ic_status_airpods_pro,
    capabilities = setOf(
        Capability.CASE_SOUNDS,
        Capability.CUSTOM_EQ,
        Capability.LISTENING_MODE,
        Capability.CONVERSATION_AWARENESS,
        Capability.HEAD_GESTURES,
        Capability.STEM_CONFIG,
        Capability.LOUD_SOUND_REDUCTION,
        Capability.PPE,
        Capability.SLEEP_DETECTION,
        Capability.HEARING_AID,
        Capability.ADAPTIVE_AUDIO,
        Capability.ADAPTIVE_VOLUME,
        Capability.SWIPE_FOR_VOLUME,
        Capability.HRM
    )
)

class AirPodsMaxLightning: AirPodsBase(
    modelNumber = listOf("A2096"),
    name = "AirPods Max (Lightning)",
    displayName = "AirPods Max",
    budCaseRes = R.drawable.airpods_max,
    budsRes = R.drawable.airpods_max,
    leftBudsRes = R.drawable.airpods_max,
    rightBudsRes = R.drawable.airpods_max,
    caseRes = R.drawable.airpods_max,
    leftIconRes = R.drawable.ic_status_airpods_max,
    rightIconRes = R.drawable.ic_status_airpods_max,
    caseIconRes = R.drawable.ic_status_airpods_max,
    statusIconRes = R.drawable.ic_status_airpods_max,
    isHeadphones = true,
    capabilities = setOf(
        Capability.LISTENING_MODE
    )
)

class AirPodsMaxUSBC: AirPodsBase(
    modelNumber = listOf("A3184"),
    name = "AirPods Max (USB-C)",
    displayName = "AirPods Max",
    budCaseRes = R.drawable.airpods_max,
    budsRes = R.drawable.airpods_max,
    leftBudsRes = R.drawable.airpods_max,
    rightBudsRes = R.drawable.airpods_max,
    caseRes = R.drawable.airpods_max,
    leftIconRes = R.drawable.ic_status_airpods_max,
    rightIconRes = R.drawable.ic_status_airpods_max,
    caseIconRes = R.drawable.ic_status_airpods_max,
    statusIconRes = R.drawable.ic_status_airpods_max,
    isHeadphones = true,
    capabilities = setOf(
        Capability.LISTENING_MODE
    )
)

data class AirPodsInstance(
    val name: String,
    val model: AirPodsBase,
    val actualModelNumber: String,
    val serialNumber: String?,
    val leftSerialNumber: String?,
    val rightSerialNumber: String?,
    val version1: String?,
    val version2: String?,
    val version3: String?,
)

object AirPodsModels {
    val models: List<AirPodsBase> = listOf(
        AirPods(),
        AirPods2(),
        AirPods3(),
        AirPods4(),
        AirPods4ANC(),
        AirPods5(),
        AirPods5WCC(),
        AirPodsPro1(),
        AirPodsPro2Lightning(),
        AirPodsPro2USBC(),
        AirPodsPro3(),
        AirPodsMaxLightning(),
        AirPodsMaxUSBC()
    )

    fun remembered(context: Context): AirPodsBase? = getModelByModelNumber(
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            .getString("airpods_model_number", "") ?: ""
    )

    /** The one-colour icon for the connected AirPods, or a general one before any have connected. */
    fun statusIcon(context: Context): Int = remembered(context)?.statusIconRes ?: R.drawable.airpods

    fun getModelByModelNumber(modelNumber: String): AirPodsBase? {
        return models.find { modelNumber in it.modelNumber }
    }
}
