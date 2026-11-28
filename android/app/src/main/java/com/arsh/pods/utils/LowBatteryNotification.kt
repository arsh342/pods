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
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Icon
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.graphics.createBitmap
import com.arsh.pods.MainActivity
import com.arsh.pods.R
import com.arsh.pods.data.AirPodsModels
import com.arsh.pods.data.Battery
import com.arsh.pods.data.BatteryComponent
import com.arsh.pods.data.BatteryStatus
import com.arsh.pods.presentation.widgets.isLowBattery
import kotlin.math.roundToInt
import dev.oneuiproject.oneui.R as OneUiR

/**
 * The notification for whatever is running low, which pops up at each threshold. It is drawn like the
 * app's home page: a picture of each part above its battery ring and level.
 */
object LowBatteryNotification {
    const val PREFERENCES = "low_battery"
    private const val ID = 500

    // The separate alerts used by earlier versions, one per part.
    private val OLD_IDS = listOf(502, 504, 508)

    private const val RING_DP = 34f
    private const val STROKE_DP = 4f
    private const val BOLT_DP = 16f

    /** The ring colours of the app, for a light and a dark notification. */
    private data class Ring(val track: Int, val fill: Int, val low: Int)

    private val light = Ring(0xFFE3E3E8.toInt(), 0xFF35C759.toInt(), 0xFFFE373C.toInt())
    private val dark = Ring(0xFF3A3A3C.toInt(), 0xFF2ED158.toInt(), 0xFFFC4244.toInt())

    private class Slot(val view: Int, val picture: Int, val ring: Int, val level: Int)

    private val slots = listOf(
        Slot(R.id.part_1, R.id.picture_1, R.id.ring_1, R.id.level_1),
        Slot(R.id.part_2, R.id.picture_2, R.id.ring_2, R.id.level_2),
        Slot(R.id.part_3, R.id.picture_3, R.id.ring_3, R.id.level_3)
    )

    fun remove(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.cancel(ID)
        OLD_IDS.forEach(manager::cancel)
    }

    /** [newThreshold] is true when a part has just reached the next step down. */
    fun refresh(context: Context, channel: String, battery: List<Battery>, newThreshold: Boolean) {
        val low = lowBatteryParts(battery)
        if (low.isEmpty()) {
            remove(context)
            return
        }
        if (!newThreshold) return
        val manager = context.getSystemService(NotificationManager::class.java)
        OLD_IDS.forEach(manager::cancel)
        // Posted afresh, so it pops up again over whatever is on screen.
        manager.cancel(ID)
        manager.notify(ID, build(context, channel, low, shownBatteryParts(battery)))
    }

    private fun build(context: Context, channel: String, low: List<Battery>, shown: List<Battery>): Notification {
        val open = PendingIntent.getActivity(
            context, 50, Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val levels = low.joinToString(" · ") { "${name(it.component)} ${it.level}%" }
        val compact = views(context, R.layout.notification_battery, shown)
        return NotificationCompat.Builder(context, channel)
            .setSmallIcon(AirPodsModels.statusIcon(context))
            .setContentTitle("AirPods battery low")
            .setContentText(levels)
            .setContentIntent(open)
            .setCategory(Notification.CATEGORY_STATUS)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(compact)
            .setCustomHeadsUpContentView(compact)
            .setCustomBigContentView(views(context, R.layout.notification_battery_big, shown))
            .build()
    }

    private fun views(context: Context, layout: Int, parts: List<Battery>): RemoteViews {
        val model = AirPodsModels.remembered(context)
        return RemoteViews(context.packageName, layout).apply {
            slots.forEachIndexed { index, slot ->
                val part = parts.getOrNull(index)
                setViewVisibility(slot.view, if (part == null) View.GONE else View.VISIBLE)
                if (part == null) return@forEachIndexed
                val picture = when (part.component) {
                    BatteryComponent.LEFT -> model?.leftBudsRes ?: R.drawable.airpods_pro_2_left
                    BatteryComponent.RIGHT -> model?.rightBudsRes ?: R.drawable.airpods_pro_2_right
                    BatteryComponent.HEADSET -> model?.budsRes ?: R.drawable.airpods_pro_2_buds
                    else -> model?.caseRes ?: R.drawable.airpods_pro_2_case
                }
                setImageViewResource(slot.picture, picture)
                // The system picks one of the two as the phone's theme changes.
                setIcon(
                    slot.ring, "setImageIcon",
                    Icon.createWithBitmap(ring(context, part, light)),
                    Icon.createWithBitmap(ring(context, part, dark))
                )
                setTextViewText(slot.level, "${part.level}%")
                setContentDescription(slot.view, "${name(part.component)} ${part.level}%")
            }
        }
    }

    private fun name(component: Int) = when (component) {
        BatteryComponent.LEFT -> "Left"
        BatteryComponent.RIGHT -> "Right"
        BatteryComponent.HEADSET -> "Headphones"
        else -> "Case"
    }

    /** A battery ring as on the home page, with a bolt in it while charging. */
    private fun ring(context: Context, part: Battery, colours: Ring): Bitmap {
        val density = context.resources.displayMetrics.density
        val size = (RING_DP * density).roundToInt()
        val bitmap = createBitmap(size, size)
        val canvas = Canvas(bitmap)
        val stroke = STROKE_DP * density
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = stroke
            strokeCap = Paint.Cap.ROUND
            color = colours.track
        }
        val start = stroke / 2
        val end = size - stroke / 2
        val fill = if (isLowBattery(part.level)) colours.low else colours.fill
        canvas.drawArc(start, start, end, end, 0f, 360f, false, paint)
        paint.color = fill
        canvas.drawArc(start, start, end, end, -90f, 360f * part.level / 100f, false, paint)
        if (part.status == BatteryStatus.CHARGING || part.status == BatteryStatus.OPTIMIZED_CHARGING) {
            context.getDrawable(OneUiR.drawable.ic_oui_charging)?.mutate()?.apply {
                val inset = ((size - BOLT_DP * density) / 2).roundToInt()
                setTint(fill)
                setBounds(inset, inset, size - inset, size - inset)
                draw(canvas)
            }
        }
        return bitmap
    }
}
