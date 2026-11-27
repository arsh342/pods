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

import com.arsh.pods.R

/**
 * Starting points for the AirPods' three-band equalizer, where 50 is flat. The bands are a bass shelf
 * below about 150 Hz, a mid band around 1 kHz and a treble shelf above about 5 kHz, each roughly
 * ±8 dB, so a step of about 6 is 1 dB. Changes are kept within a few decibels, as tuning guides advise.
 */
enum class EqPreset(val label: Int, val low: Int, val mid: Int, val high: Int) {
    BASS_BOOST(R.string.eq_preset_bass_boost, 80, 50, 50),
    LESS_BASS(R.string.eq_preset_less_bass, 30, 50, 50),
    TREBLE_BOOST(R.string.eq_preset_treble_boost, 50, 50, 76),
    LESS_TREBLE(R.string.eq_preset_less_treble, 50, 50, 30),
    VOCAL(R.string.eq_preset_vocal, 40, 66, 58),
    SPOKEN_WORD(R.string.eq_preset_spoken_word, 32, 64, 56),
    ROCK(R.string.eq_preset_rock, 68, 42, 66),
    POP(R.string.eq_preset_pop, 58, 62, 46),
    ELECTRONIC(R.string.eq_preset_electronic, 76, 44, 62),
    ACOUSTIC(R.string.eq_preset_acoustic, 46, 56, 60);

    companion object {
        /** The preset that is exactly the current setting, if any. */
        fun matching(eq: CustomEq): EqPreset? =
            entries.find { it.low == eq.low && it.mid == eq.mid && it.high == eq.high }
    }
}

data class EqBands(val low: Int, val mid: Int, val high: Int) {
    companion object {
        val FLAT = EqBands(50, 50, 50)
    }
}

val CustomEq.bands: EqBands get() = EqBands(low, mid, high)

/** Which option of the equalizer is the current one. There is always exactly one. */
sealed interface EqChoice {
    data object Flat : EqChoice
    data object Custom : EqChoice
    data class Preset(val preset: EqPreset) : EqChoice
}

/**
 * The hand-made setting kept by the app. [chosen] says it was edited or picked last, which keeps it
 * shown as Custom even when its bands happen to equal a preset.
 */
data class EqMemory(val saved: EqBands? = null, val chosen: Boolean = false) {
    fun choice(eq: CustomEq): EqChoice {
        if (!eq.isEnabled() || eq.bands == EqBands.FLAT) return EqChoice.Flat
        if (chosen && eq.bands == saved) return EqChoice.Custom
        return EqPreset.matching(eq)?.let { EqChoice.Preset(it) } ?: EqChoice.Custom
    }

    /** After a change on the graph. A flat result is nothing worth keeping. */
    fun edited(bands: EqBands): EqMemory =
        if (bands == EqBands.FLAT) EqMemory() else EqMemory(bands, chosen = true)

    /** After Flat or a preset is picked. */
    fun picked(): EqMemory = copy(chosen = false)

    /** After the saved setting is picked. */
    fun restored(): EqMemory = copy(chosen = true)

    /** Keeps a setting the AirPods report that is neither flat nor a preset. */
    fun reported(eq: CustomEq): EqMemory =
        if (eq.isEnabled() && eq.bands != EqBands.FLAT && EqPreset.matching(eq) == null) edited(eq.bands)
        else this
}
