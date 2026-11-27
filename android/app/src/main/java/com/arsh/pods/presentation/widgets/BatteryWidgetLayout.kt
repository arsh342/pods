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

enum class WidgetShape { ROW, COLUMN, GRID }

private const val ROW_FROM_RATIO = 1.6f
private const val COLUMN_UP_TO_RATIO = 0.65f

/** Picks the arrangement of the three rings for a widget of this size. */
fun widgetShape(widthDp: Float, heightDp: Float): WidgetShape {
    if (widthDp <= 0f || heightDp <= 0f) return WidgetShape.ROW
    val ratio = widthDp / heightDp
    return when {
        ratio >= ROW_FROM_RATIO -> WidgetShape.ROW
        ratio <= COLUMN_UP_TO_RATIO -> WidgetShape.COLUMN
        else -> WidgetShape.GRID
    }
}

fun isLowBattery(level: Int): Boolean = level in 1..10
