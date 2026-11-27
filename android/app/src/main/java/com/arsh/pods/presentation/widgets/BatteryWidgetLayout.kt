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

package com.arsh.pods.presentation.widgets

import kotlin.math.roundToInt

enum class WidgetShape { ROW, COLUMN, GRID }

/**
 * What one ring of the widget shows. [LOWEST_BUD] is whichever earbud has less charge, and an
 * [EMPTY] ring only fills spare room.
 */
enum class WidgetSlot { LEFT, RIGHT, CASE, PHONE, LOWEST_BUD, HEADSET, EMPTY }

/** [columns] applies to a grid. With [pictures], large rings show product pictures instead of icons. */
data class WidgetArrangement(
    val shape: WidgetShape,
    val slots: List<WidgetSlot>,
    val columns: Int = 1,
    val pictures: Boolean = false
)

// The space one ring needs, so that two home screen cells hold two rings across or down.
private const val RING_PITCH_ACROSS = 70f
private const val RING_PITCH_DOWN = 80f

// A grid with more cells than this (four by two) has room for large rings with pictures.
private const val LARGEST_SMALL_GRID = 8

private val inOrder = listOf(WidgetSlot.LEFT, WidgetSlot.RIGHT, WidgetSlot.CASE, WidgetSlot.PHONE)

private val withHeadphones = listOf(WidgetSlot.HEADSET, WidgetSlot.PHONE)

/** What fits in a widget of this size, measured in dp on screen. */
fun widgetArrangement(widthDp: Float, heightDp: Float, headphones: Boolean = false): WidgetArrangement {
    val known = widthDp > 0f && heightDp > 0f
    val across = (widthDp / RING_PITCH_ACROSS).toInt().coerceIn(1, inOrder.size)
    val down = (heightDp / RING_PITCH_DOWN).toInt().coerceIn(1, inOrder.size)
    if (known && across >= 2 && down >= 2) return grid(across, down, if (headphones) withHeadphones else inOrder)
    if (headphones) {
        // Only the headphones and the phone to show: side by side where possible.
        val sideBySide = widthDp <= 0f || widthDp >= 2 * RING_PITCH_ACROSS
        return when {
            sideBySide -> WidgetArrangement(WidgetShape.ROW, withHeadphones)
            heightDp >= 2 * RING_PITCH_DOWN -> WidgetArrangement(WidgetShape.COLUMN, withHeadphones)
            else -> WidgetArrangement(WidgetShape.ROW, listOf(WidgetSlot.HEADSET))
        }
    }
    return when {
        !known -> WidgetArrangement(WidgetShape.ROW, inOrder.take(3))
        across == 1 && down == 1 -> WidgetArrangement(WidgetShape.ROW, listOf(WidgetSlot.LOWEST_BUD))
        across == 1 -> WidgetArrangement(WidgetShape.COLUMN, inOrder.take(down))
        else -> WidgetArrangement(WidgetShape.ROW, inOrder.take(across))
    }
}

private fun grid(across: Int, down: Int, readings: List<WidgetSlot>): WidgetArrangement {
    val cells = across * down
    if (cells > LARGEST_SMALL_GRID) {
        return WidgetArrangement(WidgetShape.GRID, readings, columns = 2, pictures = true)
    }
    val empty = List(cells - readings.size) { WidgetSlot.EMPTY }
    return WidgetArrangement(WidgetShape.GRID, readings + empty, columns = across)
}

/** Rings turn red from here down, in the widget and in the app; alerts start later, at 10%. */
fun isLowBattery(level: Int): Boolean = level in 1..20

/** The largest size with the drawing's own proportions that fits a square of side [box]. */
fun fittedSize(width: Float, height: Float, box: Float): Pair<Float, Float> {
    if (width <= 0f || height <= 0f) return box to box
    val scale = box / maxOf(width, height)
    return width * scale to height * scale
}

/** The background colour chosen for a widget. [SYSTEM] follows the phone's dark mode. */
enum class WidgetTheme { SYSTEM, DARK, LIGHT }

fun WidgetTheme.usesDarkColours(phoneIsDark: Boolean): Boolean = when (this) {
    WidgetTheme.SYSTEM -> phoneIsDark
    WidgetTheme.DARK -> true
    WidgetTheme.LIGHT -> false
}

fun backgroundAlpha(transparencyPercent: Int): Int =
    ((100 - transparencyPercent.coerceIn(0, 100)) * 255 / 100f).roundToInt()
