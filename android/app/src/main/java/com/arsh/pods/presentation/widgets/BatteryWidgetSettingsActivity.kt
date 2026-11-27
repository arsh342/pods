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

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.arsh.pods.R
import com.arsh.pods.data.Battery
import com.arsh.pods.presentation.components.SegmentedSelector
import com.arsh.pods.presentation.components.StyledButton
import com.arsh.pods.presentation.components.StyledSlider
import com.arsh.pods.presentation.theme.PodsTheme
import com.arsh.pods.services.ServiceManager
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.roundToInt

/** Opened by the launcher from "Settings" in the menu shown when a widget is touched and held. */
@OptIn(ExperimentalEncodingApi::class)
class BatteryWidgetSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val manager = AppWidgetManager.getInstance(this)
        val widgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
        )
        val result = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        setResult(RESULT_CANCELED, result)
        // Launchers have to be able to start this screen, so only accept one of our own widgets.
        if (manager.getAppWidgetInfo(widgetId)?.provider != ComponentName(this, BatteryWidget::class.java)) {
            finish()
            return
        }
        enableEdgeToEdge()

        val stored = WidgetSettings.load(this, widgetId)
        val battery: List<Battery> = ServiceManager.getService()?.batteryNotification?.getBattery() ?: emptyList()

        setContent {
            PodsTheme {
                var theme by rememberSaveable { mutableStateOf(stored.theme) }
                var transparency by rememberSaveable { mutableFloatStateOf(stored.transparency.toFloat()) }
                val appearance = WidgetAppearance(theme, transparency.roundToInt())

                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .safeDrawingPadding()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 12.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.widget_settings),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        // Stands in for a wallpaper, so that transparency can be seen.
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .background(
                                    Brush.linearGradient(listOf(Color(0xFF8FB7C9), Color(0xFF3C5A48))),
                                    RoundedCornerShape(28.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            val views = remember(appearance) {
                                BatteryWidgetRenderer.preview(this@BatteryWidgetSettingsActivity, battery, appearance)
                            }
                            AndroidView(
                                factory = { FrameLayout(it) },
                                update = {
                                    it.removeAllViews()
                                    it.addView(views.apply(it.context, it))
                                },
                                modifier = Modifier.size(231.dp, 75.dp)
                            )
                        }
                        SegmentedSelector(
                            title = stringResource(R.string.widget_background_colour),
                            options = listOf(
                                WidgetTheme.SYSTEM to stringResource(R.string.widget_theme_system),
                                WidgetTheme.DARK to stringResource(R.string.widget_theme_dark),
                                WidgetTheme.LIGHT to stringResource(R.string.widget_theme_light)
                            ),
                            selected = theme,
                            onSelect = { theme = it }
                        )
                        StyledSlider(
                            label = stringResource(R.string.widget_transparency, transparency.roundToInt()),
                            value = transparency,
                            onValueChange = { transparency = it },
                            valueRange = 0f..100f,
                            independent = true
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            StyledButton(onClick = { finish() }, modifier = Modifier.weight(1f)) {
                                Text(stringResource(R.string.cancel))
                            }
                            StyledButton(
                                onClick = {
                                    val context = this@BatteryWidgetSettingsActivity
                                    WidgetSettings.save(context, widgetId, appearance)
                                    // Battery levels may have arrived since the screen opened.
                                    ServiceManager.getService()?.updateBatteryWidget()
                                        ?: BatteryWidgetRenderer.update(context, manager, intArrayOf(widgetId), battery)
                                    setResult(RESULT_OK, result)
                                    finish()
                                },
                                modifier = Modifier.weight(1f),
                                surfaceColor = MaterialTheme.colorScheme.primary
                            ) {
                                Text(stringResource(R.string.save))
                            }
                        }
                    }
                }
            }
        }
    }
}
