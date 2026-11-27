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

import android.content.Context
import androidx.core.content.edit

data class WidgetAppearance(
    val theme: WidgetTheme = WidgetTheme.SYSTEM,
    // The default matches the Galaxy battery widget.
    val transparency: Int = 35
)

/** Each widget on the home screen keeps its own appearance. */
object WidgetSettings {
    private fun preferences(context: Context) =
        context.getSharedPreferences("widget_settings", Context.MODE_PRIVATE)

    fun load(context: Context, widgetId: Int): WidgetAppearance {
        val stored = preferences(context)
        val defaults = WidgetAppearance()
        return WidgetAppearance(
            theme = WidgetTheme.entries.find { it.name == stored.getString("theme_$widgetId", null) }
                ?: defaults.theme,
            transparency = stored.getInt("transparency_$widgetId", defaults.transparency)
        )
    }

    fun save(context: Context, widgetId: Int, appearance: WidgetAppearance) {
        preferences(context).edit {
            putString("theme_$widgetId", appearance.theme.name)
            putInt("transparency_$widgetId", appearance.transparency)
        }
    }

    fun remove(context: Context, widgetId: Int) {
        preferences(context).edit {
            remove("theme_$widgetId")
            remove("transparency_$widgetId")
        }
    }
}
