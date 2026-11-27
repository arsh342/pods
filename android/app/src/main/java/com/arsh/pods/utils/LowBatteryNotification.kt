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

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.edit
import com.arsh.pods.MainActivity
import com.arsh.pods.data.AirPodsModels
import com.arsh.pods.data.Battery
import com.arsh.pods.data.BatteryComponent
import com.arsh.pods.receivers.LowBatteryDismissReceiver

/**
 * One live update for whatever is running low. Android 16 and later promote it to the lock screen and
 * a status bar chip where the phone allows that; otherwise it is an ordinary ongoing notification.
 */
object LowBatteryNotification {
    const val PREFERENCES = "low_battery"
    const val DISMISSED = "dismissed"
    private const val ID = 500
    private const val RED = 0xFFFC4244.toInt()

    fun remove(context: Context) {
        context.getSystemService(NotificationManager::class.java).cancel(ID)
    }

    /** [parts] is what is low now; [newThreshold] is true when one has just reached the next step down. */
    fun refresh(context: Context, channel: String, parts: List<Battery>, newThreshold: Boolean) {
        val preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val action = liveAction(parts.isNotEmpty(), newThreshold, preferences.getBoolean(DISMISSED, false))
        when (action) {
            LiveAction.REMOVE -> remove(context)
            LiveAction.LEAVE -> Unit
            LiveAction.ALERT, LiveAction.UPDATE -> {
                if (action == LiveAction.ALERT) preferences.edit { putBoolean(DISMISSED, false) }
                context.getSystemService(NotificationManager::class.java)
                    .notify(ID, build(context, channel, parts, alert = action == LiveAction.ALERT))
            }
        }
    }

    private fun build(context: Context, channel: String, parts: List<Battery>, alert: Boolean): Notification {
        val lowest = parts.minOf { it.level }
        val open = PendingIntent.getActivity(
            context, 50, Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val dismissed = PendingIntent.getBroadcast(
            context, 51, Intent(context, LowBatteryDismissReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val levels = parts.joinToString(" · ") {
            val name = when (it.component) {
                BatteryComponent.LEFT -> "Left"
                BatteryComponent.RIGHT -> "Right"
                BatteryComponent.HEADSET -> "Headphones"
                else -> "Case"
            }
            "$name ${it.level}%"
        }
        return NotificationCompat.Builder(context, channel)
            .setSmallIcon(AirPodsModels.statusIcon(context))
            .setContentTitle("AirPods battery low")
            .setContentText(levels)
            .setContentIntent(open)
            .setDeleteIntent(dismissed)
            .setCategory(Notification.CATEGORY_STATUS)
            .setOngoing(true)
            .setSilent(!alert)
            .setShowWhen(false)
            .setRequestPromotedOngoing(true)
            // The few characters shown in the status bar chip.
            .setShortCriticalText("$lowest%")
            .setStyle(
                NotificationCompat.ProgressStyle()
                    .setProgressSegments(listOf(NotificationCompat.ProgressStyle.Segment(100).setColor(RED)))
                    .setProgress(lowest)
            )
            .build()
    }
}
