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
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.roundToInt

// Values this close to the middle settle on it, so flat is easy to find by hand.
private const val SNAP = 3

/** The band value for a touch at [y], where [top] is 100 and [bottom] is 0. */
fun bandValueAt(y: Float, top: Float, bottom: Float): Int {
    val value = ((bottom - y) / (bottom - top) * 100f).roundToInt().coerceIn(0, 100)
    return if (abs(value - 50) <= SNAP) 50 else value
}

/** How far a band is from flat, as shown under the graph. */
fun bandOffsetLabel(value: Int): String = when {
    value > 50 -> "+${value - 50}"
    value < 50 -> "−${50 - value}"
    else -> "0"
}

/**
 * The equalizer as a curve through one handle per band. Drag a handle up or down to change it.
 */
@Composable
fun EqualizerGraph(
    values: List<Int>,
    labels: List<String>,
    onChange: (band: Int, value: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary
    val grid = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    val ring = MaterialTheme.colorScheme.surface
    val haptics = LocalHapticFeedback.current
    val current by rememberUpdatedState(values)
    val report by rememberUpdatedState(onChange)

    Column(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .semantics { contentDescription = labels.joinToString() }
                .pointerInput(Unit) {
                    val inset = 16.dp.toPx()
                    // Generous, so a handle is easy to pick up with a thumb.
                    val grab = 28.dp.toPx()
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val top = inset
                        val bottom = size.height - inset
                        val step = size.width / current.size
                        // Only a touch on a handle moves it; anything else is left to the page, so
                        // scrolling past the graph never changes the sound.
                        val band = current.indices.firstOrNull { i ->
                            val x = step * (i + 0.5f)
                            val y = bottom - (bottom - top) * current[i] / 100f
                            abs(down.position.x - x) <= grab && abs(down.position.y - y) <= grab
                        } ?: return@awaitEachGesture
                        val handleY = bottom - (bottom - top) * current[band] / 100f
                        down.consume()
                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == down.id } ?: break
                            if (!change.pressed) break
                            if (change.positionChange() != Offset.Zero) {
                                // Moved by how far the finger travelled, so picking up a handle
                                // slightly off its centre does not make it jump.
                                val value = bandValueAt(handleY + change.position.y - down.position.y, top, bottom)
                                if (value != current[band]) {
                                    if (value == 50 || value == 0 || value == 100) {
                                        haptics.performHapticFeedback(HapticFeedbackType.SegmentTick)
                                    }
                                    report(band, value)
                                }
                                change.consume()
                            }
                        }
                    }
                }
        ) {
            val inset = 16.dp.toPx()
            val top = inset
            val bottom = size.height - inset
            val middle = (top + bottom) / 2
            fun yOf(value: Int) = bottom - (bottom - top) * value / 100f
            val step = size.width / values.size
            val points = values.mapIndexed { i, v -> Offset(step * (i + 0.5f), yOf(v)) }

            // Guides: the flat line, and faint lines at the limits.
            drawLine(grid, Offset(0f, middle), Offset(size.width, middle), 1.dp.toPx())
            drawLine(grid.copy(alpha = 0.06f), Offset(0f, top), Offset(size.width, top), 1.dp.toPx())
            drawLine(grid.copy(alpha = 0.06f), Offset(0f, bottom), Offset(size.width, bottom), 1.dp.toPx())

            // A smooth curve through the handles, level beyond the outer ones like a shelf filter.
            val all = listOf(Offset(0f, points.first().y)) + points + Offset(size.width, points.last().y)
            val curve = Path().apply {
                moveTo(all.first().x, all.first().y)
                for (i in 0 until all.lastIndex) {
                    val p0 = all[maxOf(i - 1, 0)]
                    val p1 = all[i]
                    val p2 = all[i + 1]
                    val p3 = all[minOf(i + 2, all.lastIndex)]
                    // Control points kept between the two ends, so the curve never dips past a handle.
                    val low = minOf(p1.y, p2.y)
                    val high = maxOf(p1.y, p2.y)
                    cubicTo(
                        p1.x + (p2.x - p0.x) / 6f, (p1.y + (p2.y - p0.y) / 6f).coerceIn(low, high),
                        p2.x - (p3.x - p1.x) / 6f, (p2.y - (p3.y - p1.y) / 6f).coerceIn(low, high),
                        p2.x, p2.y
                    )
                }
            }
            val area = Path().apply {
                addPath(curve)
                lineTo(size.width, middle)
                lineTo(0f, middle)
                close()
            }
            drawPath(
                area,
                Brush.verticalGradient(
                    listOf(primary.copy(alpha = 0.28f), primary.copy(alpha = 0.04f), primary.copy(alpha = 0.28f)),
                    startY = top, endY = bottom
                )
            )
            drawPath(curve, primary, style = Stroke(3.dp.toPx(), cap = StrokeCap.Round))
            points.forEach {
                drawCircle(primary, 11.dp.toPx(), it)
                drawCircle(ring, 5.dp.toPx(), it)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            values.forEachIndexed { i, value ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .semantics {
                            contentDescription = labels[i]
                            progressBarRangeInfo = ProgressBarRangeInfo(value.toFloat(), 0f..100f)
                            setProgress { report(i, it.roundToInt().coerceIn(0, 100)); true }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = bandOffsetLabel(value),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = labels[i],
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
