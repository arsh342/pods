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
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.Icon
import android.os.BatteryManager
import android.os.Bundle
import android.util.SizeF
import android.view.View
import android.widget.RemoteViews
import androidx.core.graphics.createBitmap
import com.arsh.pods.MainActivity
import com.arsh.pods.R
import com.arsh.pods.data.AirPodsModels
import com.arsh.pods.data.Battery
import com.arsh.pods.data.BatteryComponent
import com.arsh.pods.data.BatteryStatus
import kotlin.math.roundToInt

/**
 * Draws the battery widget to match the Galaxy battery widget: a ring open at the bottom with the
 * device in its middle and the level in the gap. All drawing sizes are dp as seen on screen.
 */
object BatteryWidgetRenderer {
    private const val TILE = 53f
    // A phone is drawn in code; an empty ring has nothing in it.
    private const val PHONE_ICON = 0
    private const val NO_ICON = -1
    private const val RING_DIAMETER = 51f
    private const val RING_THICKNESS = 6.7f
    private const val ARC_START = 156.5f
    private const val ARC_SWEEP = 227f
    private const val ICON = 20f
    private const val LEVEL_TEXT = 13.5f
    private const val LEVEL_BASELINE = 52.2f
    private const val PICTURE = 26f
    private const val GRID_PADDING = 8f
    private const val LARGE_TILE_SHARE = 0.9f
    // Keeps the pictures within what a widget is allowed to send.
    private const val LARGEST_TILE = 140f

    /** The colours that differ between a dark and a light widget. */
    private data class Palette(val background: Int, val content: Int, val track: Int, val phoneDetail: Int)

    private val dark = Palette(Color.BLACK, Color.WHITE, Color.argb(0x33, 0xFF, 0xFF, 0xFF), Color.DKGRAY)
    private val light = Palette(Color.WHITE, Color.parseColor("#171719"), Color.argb(0x24, 0, 0, 0), Color.WHITE)
    private val gradientStart = Color.parseColor("#7ACB4A")
    private val gradientEnd = Color.parseColor("#0AC263")
    private val lowColor = Color.parseColor("#FC4244")

    private val slotViews = intArrayOf(R.id.slot_1, R.id.slot_2, R.id.slot_3, R.id.slot_4)

    /** [level] is null when the device has not reported one. */
    private data class Reading(
        val label: String, val icon: Int, val level: Int?, val charging: Boolean, val picture: Int = 0
    )

    private val emptyRing = Reading("", NO_ICON, null, false)

    fun update(context: Context, manager: AppWidgetManager, widgetIds: IntArray, battery: List<Battery>) {
        if (widgetIds.isEmpty()) return
        val readings = readings(context, battery)
        widgetIds.forEach { id ->
            val appearance = WidgetSettings.load(context, id)
            manager.updateAppWidget(id, viewsFor(context, manager.getAppWidgetOptions(id), readings, appearance))
        }
    }

    /** The widget at three cells wide, for showing the effect of a setting. */
    fun preview(context: Context, battery: List<Battery>, appearance: WidgetAppearance): RemoteViews =
        readings(context, battery).let {
            views(context, widgetArrangement(0f, 0f, isHeadphones(it)), it, 1f, appearance)
        }

    /** One layout per size the launcher reports (for example portrait and landscape). */
    private fun viewsFor(
        context: Context, options: Bundle, readings: Map<WidgetSlot, Reading>, appearance: WidgetAppearance
    ): RemoteViews {
        // Samsung's launcher shows some widgets scaled down and says by how much.
        val scale = options.getFloat("hsResizeRatio", 1f).takeIf { it > 0f } ?: 1f
        val sizes = options.getParcelableArrayList(
            AppWidgetManager.OPTION_APPWIDGET_SIZES, SizeF::class.java
        ).orEmpty()
        if (sizes.isEmpty()) return views(context, widgetArrangement(0f, 0f, isHeadphones(readings)), readings, scale, appearance)
        return RemoteViews(sizes.associateWith {
            val width = it.width * scale
            val height = it.height * scale
            val arrangement = widgetArrangement(width, height, isHeadphones(readings))
            val tile = if (arrangement.pictures) largeTile(width, height, arrangement) else TILE
            views(context, arrangement, readings, scale, appearance, tile)
        })
    }

    private fun views(
        context: Context, arrangement: WidgetArrangement, readings: Map<WidgetSlot, Reading>, scale: Float,
        appearance: WidgetAppearance, tile: Float = TILE
    ): RemoteViews {
        val layout = when (arrangement.shape) {
            WidgetShape.ROW -> R.layout.battery_widget
            WidgetShape.COLUMN -> R.layout.battery_widget_column
            WidgetShape.GRID -> R.layout.battery_widget_grid
        }
        val openApp = PendingIntent.getActivity(
            context, 0, Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // A larger ring is the same drawing at more pixels.
        val pixelsPerDp = context.resources.displayMetrics.density / scale * (tile / TILE)
        return RemoteViews(context.packageName, layout).apply {
            setOnClickPendingIntent(R.id.battery_widget, openApp)
            // For "system" the launcher is given both looks and picks one as the phone's theme changes.
            val followsPhone = appearance.theme == WidgetTheme.SYSTEM
            val fixed = if (appearance.theme.usesDarkColours(phoneIsDark = true)) dark else light
            setImageViewResource(R.id.widget_card, R.drawable.battery_widget_shape)
            if (followsPhone) setColorInt(R.id.widget_card, "setColorFilter", light.background, dark.background)
            else setInt(R.id.widget_card, "setColorFilter", fixed.background)
            setInt(R.id.widget_card, "setImageAlpha", backgroundAlpha(appearance.transparency))
            fun RemoteViews.show(slots: List<WidgetSlot?>) = slotViews.forEachIndexed { index, viewId ->
                val slot = slots.getOrNull(index)
                setViewVisibility(viewId, if (slot == null) View.GONE else View.VISIBLE)
                if (slot == null) return@forEachIndexed
                val reading = readings[slot] ?: emptyRing
                if (followsPhone) {
                    setIcon(
                        viewId, "setImageIcon",
                        Icon.createWithBitmap(draw(context, reading, pixelsPerDp, light, arrangement.pictures)),
                        Icon.createWithBitmap(draw(context, reading, pixelsPerDp, dark, arrangement.pictures))
                    )
                } else {
                    setImageViewBitmap(viewId, draw(context, reading, pixelsPerDp, fixed, arrangement.pictures))
                }
                setContentDescription(viewId, when {
                    slot == WidgetSlot.EMPTY -> null
                    reading.level == null -> "${reading.label} not connected"
                    else -> "${reading.label} ${reading.level}%"
                })
            }
            if (arrangement.shape == WidgetShape.GRID) {
                removeAllViews(R.id.widget_rows)
                arrangement.slots.chunked(arrangement.columns).forEach { slots ->
                    addView(
                        R.id.widget_rows,
                        RemoteViews(context.packageName, R.layout.battery_widget_grid_row).apply { show(slots) }
                    )
                }
            } else {
                show(arrangement.slots)
            }
        }
    }

    /** The ring size that fills one cell of a grid of large rings. */
    private fun largeTile(width: Float, height: Float, arrangement: WidgetArrangement): Float {
        val rows = (arrangement.slots.size + arrangement.columns - 1) / arrangement.columns
        val cell = minOf(
            (width - 2 * GRID_PADDING) / arrangement.columns,
            (height - 2 * GRID_PADDING) / rows
        )
        return (cell * LARGE_TILE_SHARE).coerceIn(TILE, LARGEST_TILE)
    }

    private fun readings(context: Context, battery: List<Battery>): Map<WidgetSlot, Reading> {
        fun part(component: Int, label: String, icon: Int, picture: Int): Reading {
            val reading = battery.find { it.component == component }
                ?.takeIf { it.status != BatteryStatus.DISCONNECTED && it.level in 1..100 }
            val charging = reading?.status == BatteryStatus.CHARGING ||
                reading?.status == BatteryStatus.OPTIMIZED_CHARGING
            return Reading(label, icon, reading?.level, charging, picture)
        }

        // Remembered from the last connection, so the pictures stay right while the service is stopped.
        val model = AirPodsModels.remembered(context)
        val left = part(
            BatteryComponent.LEFT, "Left AirPod",
            model?.leftIconRes ?: R.drawable.airpods_pro_left_notification,
            model?.leftBudsRes ?: R.drawable.airpods_pro_2_left
        )
        val right = part(
            BatteryComponent.RIGHT, "Right AirPod",
            model?.rightIconRes ?: R.drawable.airpods_pro_right_notification,
            model?.rightBudsRes ?: R.drawable.airpods_pro_2_right
        )
        val phone = context.getSystemService(BatteryManager::class.java)
        val headset = part(
            BatteryComponent.HEADSET, "AirPods Max", R.drawable.ic_status_airpods_max,
            model?.takeIf { it.isHeadphones }?.budsRes ?: 0
        )
        val headphones = model?.isHeadphones == true || headset.level != null
        return listOfNotNull(
            (WidgetSlot.HEADSET to headset).takeIf { headphones },
            WidgetSlot.LEFT to left,
            WidgetSlot.RIGHT to right,
            WidgetSlot.CASE to part(
                BatteryComponent.CASE, "Charging case",
                model?.caseIconRes ?: R.drawable.airpods_pro_case_notification,
                model?.caseRes ?: R.drawable.airpods_pro_2_case
            ),
            WidgetSlot.PHONE to Reading(
                "Phone", PHONE_ICON,
                phone.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY).takeIf { it in 1..100 },
                phone.isCharging
            ),
            WidgetSlot.LOWEST_BUD to (listOf(left, right).filter { it.level != null }.minByOrNull { it.level!! } ?: left)
        ).toMap()
    }

    private fun isHeadphones(readings: Map<WidgetSlot, Reading>) = WidgetSlot.HEADSET in readings

    private fun draw(
        context: Context, reading: Reading, pixelsPerDp: Float, palette: Palette, pictures: Boolean
    ): Bitmap {
        val pixels = (TILE * pixelsPerDp).roundToInt()
        val bitmap = createBitmap(pixels, pixels)
        val canvas = Canvas(bitmap)
        canvas.scale(pixels / TILE, pixels / TILE)
        val centre = TILE / 2
        val radius = (RING_DIAMETER - RING_THICKNESS) / 2
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = RING_THICKNESS
            strokeCap = Paint.Cap.ROUND
            color = palette.track
        }
        val left = centre - radius
        val right = centre + radius
        canvas.drawArc(left, left, right, right, ARC_START, ARC_SWEEP, false, paint)

        val level = reading.level
        if (level != null) {
            if (isLowBattery(level)) {
                paint.color = lowColor
            } else {
                paint.color = gradientEnd
                paint.shader = LinearGradient(
                    left, right, centre, left, gradientStart, gradientEnd, Shader.TileMode.CLAMP
                )
            }
            canvas.drawArc(left, left, right, right, ARC_START, ARC_SWEEP * level / 100f, false, paint)
        }

        val alpha = if (level == null) 0x80 else 0xFF
        when {
            reading.icon == NO_ICON -> Unit
            reading.icon == PHONE_ICON -> {
                // Drawn larger beside the pictures, which fill more of their rings than the icons do.
                val grow = if (pictures) 1.3f else 1f
                canvas.save()
                canvas.scale(grow, grow, centre, centre)
                drawPhone(canvas, centre, alpha, palette)
                canvas.restore()
            }
            pictures && reading.picture != 0 -> drawPicture(context, canvas, reading.picture, centre, alpha)
            else -> drawIcon(context, canvas, reading.icon, centre, alpha, palette)
        }

        if (level != null) drawLevel(context, canvas, level, reading.charging && level < 100, palette)
        return bitmap
    }

    private fun drawLevel(context: Context, canvas: Canvas, level: Int, charging: Boolean, palette: Palette) {
        val text = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = palette.content
            textSize = LEVEL_TEXT
            textAlign = Paint.Align.CENTER
            // One UI Sans on Galaxy phones; other phones fall back to their own font.
            typeface = Typeface.create(Typeface.create("sec", Typeface.NORMAL), 600, false)
        }
        val bolt = if (charging) 8f else 0f
        val centre = TILE / 2 + bolt / 2
        canvas.drawText(level.toString(), centre, LEVEL_BASELINE, text)
        if (charging) {
            val boltLeft = centre - text.measureText(level.toString()) / 2 - bolt
            context.getDrawable(R.drawable.ic_power)?.mutate()?.apply {
                setTint(palette.content)
                setBounds(boltLeft.roundToInt(), 43, (boltLeft + 6.5f).roundToInt(), 53)
                draw(canvas)
            }
        }
    }

    private fun drawPhone(canvas: Canvas, centre: Float, alpha: Int, palette: Palette) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = palette.content
            this.alpha = alpha
        }
        canvas.drawRoundRect(centre - 6f, centre - 8.5f, centre + 6f, centre + 8.5f, 2.6f, 2.6f, paint)
        paint.color = palette.phoneDetail
        canvas.drawCircle(centre, centre - 5.8f, 0.9f, paint)
    }

    private fun drawPicture(context: Context, canvas: Canvas, picture: Int, centre: Float, alpha: Int) {
        val options = BitmapFactory.Options().apply { inScaled = false }
        val bitmap = BitmapFactory.decodeResource(context.resources, picture, options) ?: return
        // The case pictures have wide see-through margins, which are cut away.
        val content = contentBounds(bitmap)
        val (width, height) = fittedSize(content.width().toFloat(), content.height().toFloat(), PICTURE)
        val target = RectF(centre - width / 2, centre - height / 2, centre + width / 2, centre + height / 2)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply { this.alpha = alpha }
        canvas.drawBitmap(bitmap, content, target, paint)
    }

    /** The part of a picture that is not see-through. */
    private fun contentBounds(bitmap: Bitmap): Rect {
        val row = IntArray(bitmap.width)
        val bounds = Rect(bitmap.width, bitmap.height, 0, 0)
        for (y in 0 until bitmap.height) {
            bitmap.getPixels(row, 0, bitmap.width, 0, y, bitmap.width, 1)
            val first = row.indexOfFirst { Color.alpha(it) > 25 }
            if (first < 0) continue
            val last = row.indexOfLast { Color.alpha(it) > 25 }
            bounds.set(
                minOf(bounds.left, first), minOf(bounds.top, y),
                maxOf(bounds.right, last + 1), maxOf(bounds.bottom, y + 1)
            )
        }
        return if (bounds.isEmpty) Rect(0, 0, bitmap.width, bitmap.height) else bounds
    }

    private fun drawIcon(context: Context, canvas: Canvas, icon: Int, centre: Float, alpha: Int, palette: Palette) {
        context.getDrawable(icon)?.mutate()?.apply {
            val (width, height) = fittedSize(intrinsicWidth.toFloat(), intrinsicHeight.toFloat(), ICON)
            setTint(palette.content)
            this.alpha = alpha
            // Scaled on the canvas, so the proportions are not rounded to whole units.
            canvas.save()
            canvas.translate(centre - width / 2, centre - height / 2)
            canvas.scale(width / intrinsicWidth, height / intrinsicHeight)
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            draw(canvas)
            canvas.restore()
        }
    }
}
