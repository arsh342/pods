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

@file:OptIn(ExperimentalEncodingApi::class)

package com.arsh.pods.presentation.components

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import android.provider.Settings
import com.arsh.pods.R
import kotlin.io.encoding.ExperimentalEncodingApi

@Composable
fun AudioSettings(
    adaptiveVolumeCapability: Boolean,
    conversationalAwarenessCapability: Boolean,
    loudSoundReductionCapability: Boolean,
    adaptiveAudioCapability: Boolean,
    adaptiveVolumeChecked: Boolean,
    onAdaptiveVolumeCheckedChange: (Boolean) -> Unit,

    conversationalAwarenessChecked: Boolean,
    onConversationalAwarenessCheckedChange: (Boolean) -> Unit,

    loudSoundReductionChecked: Boolean,
    onLoudSoundReductionCheckedChange: (Boolean) -> Unit,

    navigateToAdaptiveStrength: () -> Unit,

    vendorIdHook: Boolean,
) {
    val context = LocalContext.current
    StyledList(title = stringResource(R.string.audio)) {
        if (adaptiveVolumeCapability) {
            StyledToggle(
                label = stringResource(R.string.personalized_volume),
                description = stringResource(R.string.personalized_volume_description),
                checked = adaptiveVolumeChecked,
                onCheckedChange = onAdaptiveVolumeCheckedChange,
            )
        }

        if (conversationalAwarenessCapability) {
            StyledToggle(
                label = stringResource(R.string.conversational_awareness),
                description = stringResource(R.string.conversational_awareness_description),
                checked = conversationalAwarenessChecked,
                onCheckedChange = onConversationalAwarenessCheckedChange,
            )
        }

        if (loudSoundReductionCapability && vendorIdHook) {
            StyledToggle(
                label = stringResource(R.string.loud_sound_reduction),
                description = stringResource(R.string.loud_sound_reduction_description),
                checked = loudSoundReductionChecked,
                onCheckedChange = onLoudSoundReductionCheckedChange,
            )
        }

        if (adaptiveAudioCapability) {
            StyledListItem(
                name = stringResource(R.string.adaptive_audio),
                onClick = navigateToAdaptiveStrength,
            )
        }

        StyledListItem(
            name = stringResource(R.string.equalizer),
            onClick = {
                try {
                    val soundQualityIntent = Intent().setClassName(
                        "com.sec.android.app.soundalive",
                        "com.sec.android.app.soundalive.SAControlPanelListActivity"
                    )
                    context.startActivity(soundQualityIntent)
                } catch (_: ActivityNotFoundException) {
                    openGeneralSoundSettings(context)
                } catch (_: SecurityException) {
                    openGeneralSoundSettings(context)
                }
            },
        )
    }
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
