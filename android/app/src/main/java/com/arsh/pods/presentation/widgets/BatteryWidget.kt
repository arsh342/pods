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

@file:OptIn(ExperimentalEncodingApi::class)

package com.arsh.pods.presentation.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Bundle
import com.arsh.pods.services.ServiceManager
import kotlin.io.encoding.ExperimentalEncodingApi

class BatteryWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        ServiceManager.getService()?.updateBattery()
            ?: drawWithoutAirPods(context, appWidgetManager, appWidgetIds)
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        appWidgetIds.forEach { WidgetSettings.remove(context, it) }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        // The widget was resized, so it may need a different arrangement.
        ServiceManager.getService()?.updateBatteryWidget()
            ?: drawWithoutAirPods(context, appWidgetManager, intArrayOf(appWidgetId))
    }

    // Without the service there is nothing to say about the AirPods, but the widget is not left blank.
    private fun drawWithoutAirPods(context: Context, manager: AppWidgetManager, ids: IntArray) =
        BatteryWidgetRenderer.update(context, manager, ids, emptyList())
}
