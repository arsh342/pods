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

package com.arsh.pods.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arsh.pods.R
import kotlin.math.abs

private val ThumbRadius = 12.dp
private val TrackThickness = 18.dp

/** A One UI slider: thick rounded track and a ringed thumb. */
@Composable
fun StyledSlider(
    modifier: Modifier = Modifier,
    label: String? = null,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    snapPoints: List<Float> = emptyList(),
    snapThreshold: Float = 0.05f,
    startIcon: String? = null,
    endIcon: String? = null,
    startLabel: String? = null,
    endLabel: String? = null,
    independent: Boolean = false,
    description: String? = null,
    enabled: Boolean = true,
    index: Int = 0,
    count: Int = 1
) {
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    // Icons are SF Symbols characters, which only the bundled font can draw.
    val glyphs = FontFamily(Font(R.font.sf_pro))

    val control = @Composable {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (startLabel != null || endLabel != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(startLabel ?: "", style = MaterialTheme.typography.bodyMedium, color = muted)
                    Text(endLabel ?: "", style = MaterialTheme.typography.bodyMedium, color = muted)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                startIcon?.let {
                    Text(it, fontFamily = glyphs, color = muted, modifier = Modifier.padding(end = 12.dp))
                }
                SliderTrack(
                    modifier = Modifier.weight(1f),
                    value = value,
                    valueRange = valueRange,
                    snapPoints = snapPoints,
                    snapThreshold = snapThreshold,
                    enabled = enabled,
                    onValueChange = onValueChange
                )
                endIcon?.let {
                    Text(it, fontFamily = glyphs, color = muted, modifier = Modifier.padding(start = 12.dp))
                }
            }
        }
    }

    if (!independent) {
        Column(modifier = modifier.fillMaxWidth()) { control() }
        return
    }

    Column(modifier = modifier.fillMaxWidth()) {
        label?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = muted,
                modifier = Modifier.padding(start = 20.dp, bottom = 8.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) { control() }
        description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = muted,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun SliderTrack(
    modifier: Modifier,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    snapPoints: List<Float>,
    snapThreshold: Float,
    enabled: Boolean,
    onValueChange: (Float) -> Unit
) {
    val currentOnValueChange by rememberUpdatedState(onValueChange)
    val currentSnapPoints by rememberUpdatedState(snapPoints)
    val span = valueRange.endInclusive - valueRange.start
    val fraction = ((value - valueRange.start) / span).coerceIn(0f, 1f)
    var dragX by remember { mutableFloatStateOf(0f) }
    var width by remember { mutableFloatStateOf(0f) }

    val inactive = if (isSystemInDarkTheme()) Color(0xFF848487) else Color(0xFFD8D8DC)
    val active = if (enabled) MaterialTheme.colorScheme.primary else inactive

    // Maps a touch position to a value, keeping the thumb inside the track.
    fun select(x: Float, radius: Float) {
        val usable = width - radius * 2
        if (usable <= 0f) return
        val raw = valueRange.start + ((x - radius) / usable).coerceIn(0f, 1f) * span
        val snap = currentSnapPoints.minByOrNull { abs(it - raw) }
        currentOnValueChange(if (snap != null && abs(snap - raw) <= snapThreshold) snap else raw)
    }

    var radiusPx by remember { mutableFloatStateOf(0f) }
    val gestures = if (!enabled) Modifier else Modifier
        .pointerInput(valueRange) {
            detectTapGestures { select(it.x, radiusPx) }
        }
        .draggable(
            orientation = Orientation.Horizontal,
            state = rememberDraggableState { delta ->
                dragX += delta
                select(dragX, radiusPx)
            },
            onDragStarted = { dragX = it.x }
        )

    val thumbFill = MaterialTheme.colorScheme.surface

    Canvas(
        modifier = modifier
            .height(36.dp)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(value, valueRange)
                if (enabled) setProgress {
                    currentOnValueChange(it.coerceIn(valueRange.start, valueRange.endInclusive))
                    true
                }
            }
            .then(gestures)
    ) {
        val radius = ThumbRadius.toPx()
        radiusPx = radius
        width = size.width
        val y = size.height / 2
        val thumbX = radius + (size.width - radius * 2) * fraction
        val thickness = TrackThickness.toPx()

        drawLine(inactive, Offset(radius, y), Offset(size.width - radius, y), thickness, StrokeCap.Round)
        drawLine(active, Offset(radius, y), Offset(thumbX, y), thickness, StrokeCap.Round)
        drawCircle(thumbFill, radius, Offset(thumbX, y))
        drawCircle(active, radius - 1.dp.toPx(), Offset(thumbX, y), style = androidx.compose.ui.graphics.drawscope.Stroke(2.dp.toPx()))
    }
}
