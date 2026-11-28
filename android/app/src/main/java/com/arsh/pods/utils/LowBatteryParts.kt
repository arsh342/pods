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

import com.arsh.pods.data.Battery
import com.arsh.pods.data.BatteryComponent
import com.arsh.pods.data.BatteryStatus

private val order =
    listOf(BatteryComponent.HEADSET, BatteryComponent.LEFT, BatteryComponent.RIGHT, BatteryComponent.CASE)

/** The parts that call for the low battery notification: at 10% or below and running down. */
fun lowBatteryParts(battery: List<Battery>): List<Battery> = shownBatteryParts(battery)
    .filter { it.level <= 10 && it.status == BatteryStatus.NOT_CHARGING }

/** The parts the notification pictures: every one with a known level, left to right. */
fun shownBatteryParts(battery: List<Battery>): List<Battery> = battery
    .filter { it.level in 1..100 && it.status != BatteryStatus.DISCONNECTED }
    .sortedBy { order.indexOf(it.component) }
