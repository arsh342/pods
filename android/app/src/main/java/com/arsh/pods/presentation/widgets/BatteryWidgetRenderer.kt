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

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.SizeF
import android.widget.RemoteViews
import androidx.core.graphics.createBitmap
import com.arsh.pods.MainActivity
import com.arsh.pods.R
import com.arsh.pods.data.Battery
import com.arsh.pods.data.BatteryComponent
import com.arsh.pods.data.BatteryStatus

/**
 * Draws the battery widget. Each ring is a bitmap, so it keeps its shape at any widget size.
 * Geometry is expressed on a 90 unit square.
 */
object BatteryWidgetRenderer {
    private const val RING_PIXELS = 270
    private val trackColor = Color.parseColor("#99F5F5F5")
    private val normalColor = Color.parseColor("#00D85B")
    private val lowColor = Color.parseColor("#FC4244")

    private data class Ring(val viewId: Int, val label: String, val bitmap: Bitmap, val level: Int)

    fun update(context: Context, manager: AppWidgetManager, widgetIds: IntArray, battery: List<Battery>) {
        if (widgetIds.isEmpty()) return
        val rings = listOf(
            ring(context, battery, BatteryComponent.LEFT, R.id.left_ring, "Left AirPod",
                R.drawable.airpods_pro_left_notification),
            ring(context, battery, BatteryComponent.RIGHT, R.id.right_ring, "Right AirPod",
                R.drawable.airpods_pro_right_notification),
            ring(context, battery, BatteryComponent.CASE, R.id.case_ring, "Charging case",
                R.drawable.airpods_pro_case_notification)
        )
        widgetIds.forEach { id ->
            manager.updateAppWidget(id, viewsFor(context, manager.getAppWidgetOptions(id), rings))
        }
    }

    /** One layout per size the launcher reports (for example portrait and landscape). */
    private fun viewsFor(context: Context, options: Bundle, rings: List<Ring>): RemoteViews {
        val sizes = options.getParcelableArrayList(
            AppWidgetManager.OPTION_APPWIDGET_SIZES, SizeF::class.java
        ).orEmpty()
        if (sizes.isEmpty()) return views(context, WidgetShape.ROW, rings)
        return RemoteViews(sizes.associateWith { views(context, widgetShape(it.width, it.height), rings) })
    }

    private fun views(context: Context, shape: WidgetShape, rings: List<Ring>): RemoteViews {
        val layout = when (shape) {
            WidgetShape.ROW -> R.layout.battery_widget
            WidgetShape.COLUMN -> R.layout.battery_widget_column
            WidgetShape.GRID -> R.layout.battery_widget_grid
        }
        val openApp = PendingIntent.getActivity(
            context, 0, Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return RemoteViews(context.packageName, layout).apply {
            setOnClickPendingIntent(R.id.battery_widget, openApp)
            rings.forEach {
                setImageViewBitmap(it.viewId, it.bitmap)
                setContentDescription(it.viewId, "${it.label} ${it.level}%")
            }
        }
    }

    private fun ring(
        context: Context, battery: List<Battery>, component: Int,
        viewId: Int, label: String, icon: Int
    ): Ring {
        val reading = battery.find { it.component == component }
        val level = reading?.level?.coerceIn(0, 100) ?: 0
        val charging = reading?.status == BatteryStatus.CHARGING ||
            reading?.status == BatteryStatus.OPTIMIZED_CHARGING
        return Ring(viewId, label, draw(context, level, charging, icon), level)
    }

    private fun draw(context: Context, level: Int, charging: Boolean, icon: Int): Bitmap {
        val bitmap = createBitmap(RING_PIXELS, RING_PIXELS)
        val canvas = Canvas(bitmap)
        val unit = RING_PIXELS / 90f
        canvas.scale(unit, unit)

        val bounds = RectF(16f, 16f, 74f, 74f)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 6f
            color = trackColor
        }
        canvas.drawOval(bounds, paint)
        paint.color = if (isLowBattery(level)) lowColor else normalColor
        canvas.drawArc(bounds, -90f, 360f * level / 100f, false, paint)

        drawIcon(context, canvas, icon, left = 31, top = 31, size = 28)
        if (charging) drawIcon(context, canvas, R.drawable.ic_power, left = 35, top = 6, size = 20)
        return bitmap
    }

    private fun drawIcon(context: Context, canvas: Canvas, icon: Int, left: Int, top: Int, size: Int) {
        context.getDrawable(icon)?.mutate()?.apply {
            setTint(Color.WHITE)
            setBounds(left, top, left + size, top + size)
            draw(canvas)
        }
    }
}
