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

/** The parts that belong in the low battery live update: at 10% or below and running down. */
fun lowBatteryParts(battery: List<Battery>): List<Battery> = battery
    .filter { it.level in 1..10 && it.status == BatteryStatus.NOT_CHARGING }
    .sortedBy { order.indexOf(it.component) }

enum class LiveAction {
    /** Take the live update away. */
    REMOVE,
    /** Show it and make a sound: a part has just reached a new threshold. */
    ALERT,
    /** Show the current levels silently. */
    UPDATE,
    /** Post nothing: it was swiped away, or without live updates no new threshold was reached. */
    LEAVE
}

/** [live] is whether the phone shows live updates, which stay up and follow the levels. */
fun liveAction(live: Boolean, anyLow: Boolean, newThreshold: Boolean, dismissed: Boolean): LiveAction = when {
    !anyLow -> LiveAction.REMOVE
    newThreshold -> LiveAction.ALERT
    !live || dismissed -> LiveAction.LEAVE
    else -> LiveAction.UPDATE
}
