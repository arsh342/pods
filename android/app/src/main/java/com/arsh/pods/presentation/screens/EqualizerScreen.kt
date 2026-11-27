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

package com.arsh.pods.presentation.screens

import com.arsh.pods.presentation.components.StyledButton
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.arsh.pods.presentation.components.EqualizerGraph
import com.arsh.pods.data.EqPreset
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import com.arsh.pods.presentation.components.SegmentedSelector
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arsh.pods.R
import com.arsh.pods.data.Capability
import com.arsh.pods.data.EqBands
import com.arsh.pods.data.EqChoice
import com.arsh.pods.presentation.components.StyledList
import com.arsh.pods.presentation.components.StyledListItem
import com.arsh.pods.presentation.components.StyledSlider
import com.arsh.pods.presentation.viewmodel.AirPodsViewModel
import com.arsh.pods.utils.openSystemEqualizer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun EqualizerScreen(viewModel: AirPodsViewModel) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    // The equalizer panel only learns who is asking when it is started for a result.
    val equalizerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {}
    val topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 64.dp
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 40.dp

    val eq = state.customEq
    // Held across recompositions so the slider callbacks always read current values.
    val low = remember { mutableFloatStateOf(eq.low.toFloat()) }
    val mid = remember { mutableFloatStateOf(eq.mid.toFloat()) }
    val high = remember { mutableFloatStateOf(eq.high.toFloat()) }
    LaunchedEffect(eq) {
        low.floatValue = eq.low.toFloat()
        mid.floatValue = eq.mid.toFloat()
        high.floatValue = eq.high.toFloat()
    }

    val scope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current
    var sendJob by remember { mutableStateOf<Job?>(null) }
    fun sendBands() {
        sendJob?.cancel()
        sendJob = scope.launch {
            delay(150)
            viewModel.editCustomEq(
                low.floatValue.roundToInt(),
                mid.floatValue.roundToInt(),
                high.floatValue.roundToInt()
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(topPadding))

        if (Capability.CUSTOM_EQ in state.capabilities) {
            val current = state.eqMemory.choice(eq)
            // Recommended lists the choices; Custom is the graph for a hand-made setting.
            var showCustom by rememberSaveable { mutableStateOf(false) }
            /** Shows these bands at once, and drops a graph change still waiting to be sent. */
            fun show(bands: EqBands) {
                sendJob?.cancel()
                low.floatValue = bands.low.toFloat()
                mid.floatValue = bands.mid.toFloat()
                high.floatValue = bands.high.toFloat()
            }
            fun openGraph() {
                showCustom = true
                // The graph only works once the custom equalizer is on, and starts flat.
                if (!eq.isEnabled()) {
                    show(EqBands.FLAT)
                    viewModel.selectEq(null)
                }
            }

            SegmentedSelector(
                title = stringResource(R.string.airpods_equalizer),
                options = listOf(
                    false to stringResource(R.string.recommended),
                    true to stringResource(R.string.custom)
                ),
                selected = showCustom,
                onSelect = { custom -> if (custom) openGraph() else showCustom = false }
            )

            if (!showCustom) {
                // Flat first, then the presets and the saved setting, two to a row.
                val choices = listOf(EqChoice.Flat) +
                    EqPreset.entries.map { EqChoice.Preset(it) } + EqChoice.Custom
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    choices.chunked(2).forEach { pair ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            pair.forEach { choice ->
                                val selected = choice == current
                                val label = when (choice) {
                                    EqChoice.Flat -> R.string.eq_preset_flat
                                    EqChoice.Custom -> R.string.custom
                                    is EqChoice.Preset -> choice.preset.label
                                }
                                Text(
                                    text = stringResource(label),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                                    color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(CircleShape)
                                        .background(
                                            if (selected) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.surface
                                        )
                                        .selectable(selected = selected, role = Role.RadioButton) {
                                            if (!selected) haptics.performHapticFeedback(HapticFeedbackType.SegmentTick)
                                            when (choice) {
                                                EqChoice.Flat -> {
                                                    show(EqBands.FLAT)
                                                    viewModel.selectEq(null)
                                                }
                                                is EqChoice.Preset -> {
                                                    val preset = choice.preset
                                                    show(EqBands(preset.low, preset.mid, preset.high))
                                                    viewModel.selectEq(preset)
                                                }
                                                // With nothing saved yet, the graph is where to make one.
                                                EqChoice.Custom -> {
                                                    state.eqMemory.saved?.let { show(it) }
                                                    if (!viewModel.selectSavedEq()) openGraph()
                                                }
                                            }
                                        }
                                        .padding(vertical = 14.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                val bands = listOf(low, mid, high)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp))
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    EqualizerGraph(
                        values = bands.map { it.floatValue.roundToInt() },
                        labels = listOf(
                            stringResource(R.string.eq_low),
                            stringResource(R.string.eq_mid),
                            stringResource(R.string.eq_high)
                        ),
                        onChange = { band, value ->
                            bands[band].floatValue = value.toFloat()
                            sendBands()
                        }
                    )
                }
                StyledButton(
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                        show(EqBands.FLAT)
                        viewModel.resetCustomEq()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.reset))
                }
            }

            if (!state.customEqReported) {
                Text(
                    text = stringResource(R.string.airpods_equalizer_description),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }

        StyledList(title = stringResource(R.string.phone_sound)) {
            StyledListItem(
                name = stringResource(R.string.phone_equalizer),
                onClick = { openSystemEqualizer(context) { equalizerLauncher.launch(it) } }
            )
        }

        Spacer(modifier = Modifier.height(bottomPadding))
    }
}
