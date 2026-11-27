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

package com.arsh.pods.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.audiofx.AudioEffect
import android.provider.Settings
import android.widget.Toast
import com.arsh.pods.R

private const val SOUND_ALIVE = "com.sec.android.app.soundalive"

/**
 * Opens the phone's equalizer. Samsung's own equalizer screen comes first, because its answer to
 * Android's standard request is the surrounding sound page. Other phones get that standard panel,
 * and the general sound settings only if nothing handles it.
 */
fun openSystemEqualizer(context: Context, launch: (Intent) -> Unit) {
    val panel = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
        .putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context.packageName)
        .putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)

    val samsungEqualizer = Intent().setClassName(SOUND_ALIVE, "$SOUND_ALIVE.activity.EqualizerActivity")

    for (intent in listOf(samsungEqualizer, panel)) {
        try {
            launch(intent)
            return
        } catch (_: ActivityNotFoundException) {
            // try the next one
        } catch (_: SecurityException) {
            // try the next one
        }
    }
    openGeneralSoundSettings(context)
}

private fun openGeneralSoundSettings(context: Context) {
    // Samsung exposes Sound quality and effects through SoundAlive. On other
    // Android devices, the closest system destination is Sound settings.
    try {
        context.startActivity(Intent(Settings.ACTION_SOUND_SETTINGS))
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(
            context,
            R.string.system_equalizer_unavailable,
            Toast.LENGTH_SHORT
        ).show()
    }
}
