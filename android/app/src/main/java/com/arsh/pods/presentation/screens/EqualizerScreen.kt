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
    var sendJob by remember { mutableStateOf<Job?>(null) }
    fun sendBands() {
        sendJob?.cancel()
        sendJob = scope.launch {
            delay(150)
            viewModel.setCustomEq(
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
            SegmentedSelector(
                title = stringResource(R.string.airpods_equalizer),
                options = listOf(
                    false to stringResource(R.string.recommended),
                    true to stringResource(R.string.custom)
                ),
                selected = eq.isEnabled(),
                onSelect = { viewModel.setCustomEqEnabled(it) }
            )

            if (eq.isEnabled()) {
                listOf(
                    R.string.eq_low to low,
                    R.string.eq_mid to mid,
                    R.string.eq_high to high
                ).forEach { (label, band) ->
                    StyledSlider(
                        label = stringResource(label),
                        value = band.floatValue,
                        onValueChange = {
                            band.floatValue = it
                            sendBands()
                        },
                        valueRange = 0f..100f,
                        snapPoints = listOf(50f),
                        startLabel = "−",
                        endLabel = "+",
                        independent = true
                    )
                }

                StyledList {
                    StyledListItem(
                        name = stringResource(R.string.reset),
                        onClick = {
                            low.floatValue = 50f
                            mid.floatValue = 50f
                            high.floatValue = 50f
                            sendBands()
                        }
                    )
                }
            }

            Text(
                text = stringResource(R.string.airpods_equalizer_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
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
