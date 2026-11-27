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
 * Starting points for the AirPods' three-band equalizer, where 50 is flat. There is no flat preset:
 * the AirPods' own tuning (the custom equalizer off) sounds the same. The bands are a bass shelf
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
    ACOUSTIC(R.string.eq_preset_acoustic, 46, 56, 60),
    // Fuller and softer: a little more bass, a little less treble.
    WARM(R.string.eq_preset_warm, 62, 54, 40);

    companion object {
        /** The preset that is exactly the current setting, if any. */
        fun matching(eq: CustomEq): EqPreset? =
            entries.find { it.low == eq.low && it.mid == eq.mid && it.high == eq.high }
    }
}
