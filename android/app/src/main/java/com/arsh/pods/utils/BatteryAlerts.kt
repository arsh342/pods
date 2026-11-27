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

/** [notify] is the threshold to alert for now; [lowestSent] is what to remember for next time. */
data class AlertDecision(val notify: Int?, val lowestSent: Int?)

private val thresholds = listOf(1, 2, 5, 10)

// Alerts re-arm only after a real recharge, so a reading hovering around 10% cannot repeat.
private const val RESET_LEVEL = 15

/** Alerts once each at 10%, 5%, 2% and 1% while a battery drains. */
fun nextBatteryAlert(level: Int, charging: Boolean, lowestSent: Int?): AlertDecision {
    val quiet = AlertDecision(null, lowestSent)
    // 0 and below mean the level is unknown.
    if (level !in 1..100) return quiet
    if (level >= RESET_LEVEL) return AlertDecision(null, null)
    if (charging) return quiet

    val reached = thresholds.firstOrNull { level <= it } ?: return quiet
    if (lowestSent != null && reached >= lowestSent) return quiet
    return AlertDecision(reached, reached)
}
