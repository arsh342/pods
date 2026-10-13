/*
    Pods - AirPods on Android
    Copyright (C) 2025 Pods contributors

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

package com.arsh.pods.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme

// One UI Light Color Tokens
val LightBackground = Color(0xFFF5F5F5)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceContainer = Color(0xFFF5F5F5)
val LightSurfaceContainerHigh = Color(0xFFF0F0F0)
val LightSurfaceContainerHighest = Color(0xFFE7E7E7)
val LightOnBackground = Color(0xFF1C1C1E)
val LightOnSurface = Color(0xFF1C1C1E)
val LightOnSurfaceVariant = Color(0xFF4A4A4A)
val LightPrimary = Color(0xFF0381FE)
val LightPrimaryContainer = Color(0xFFDCEBFF)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightOnPrimaryContainer = Color(0xFF00264D)
val LightSecondary = Color(0xFF4C6F96)
val LightSecondaryContainer = Color(0xFFDCEBFF)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightOnSecondaryContainer = Color(0xFF00264D)
val LightTertiary = Color(0xFF6C5B7B)
val LightTertiaryContainer = Color(0xFFFADEF5)
val LightOnTertiary = Color(0xFFFFFFFF)
val LightOnTertiaryContainer = Color(0xFF231928)
val LightError = Color(0xFFBA1A1A)
val LightErrorContainer = Color(0xFFFFDAD6)
val LightOnError = Color(0xFFFFFFFF)
val LightOnErrorContainer = Color(0xFF410002)
val LightOutline = Color(0xFF79747E)
val LightOutlineVariant = Color(0xFFCAC4D0)
val LightShadow = Color(0xFF000000)
val LightScrim = Color(0xFF000000)
val LightInverseSurface = Color(0xFF2F3033)
val LightInverseOnSurface = Color(0xFFF2F0F4)
val LightInversePrimary = Color(0xFFB0D0FF)

// One UI Dark Color Tokens
val DarkBackground = Color(0xFF000000)
val DarkSurface = Color(0xFF1C1C1E)
val DarkSurfaceContainer = Color(0xFF000000)
val DarkSurfaceContainerHigh = Color(0xFF1C1C1E)
val DarkSurfaceContainerHighest = Color(0xFF2C2C2E)
val DarkOnBackground = Color(0xFFF5F5F5)
val DarkOnSurface = Color(0xFFF5F5F5)
val DarkOnSurfaceVariant = Color(0xFFB8B8BD)
val DarkPrimary = Color(0xFF3F82F7)
val DarkPrimaryContainer = Color(0xFF073B77)
val DarkOnPrimary = Color(0xFFFFFFFF)
val DarkOnPrimaryContainer = Color(0xFFDCEBFF)
val DarkSecondary = Color(0xFF8DBBFF)
val DarkSecondaryContainer = Color(0xFF073B77)
val DarkOnSecondary = Color(0xFF00264D)
val DarkOnSecondaryContainer = Color(0xFFDCEBFF)
val DarkTertiary = Color(0xFFD0BCFF)
val DarkTertiaryContainer = Color(0xFF3D2B44)
val DarkOnTertiary = Color(0xFF231928)
val DarkOnTertiaryContainer = Color(0xFFFADEF5)
val DarkError = Color(0xFFFFB4AB)
val DarkErrorContainer = Color(0xFF93000A)
val DarkOnError = Color(0xFF690005)
val DarkOnErrorContainer = Color(0xFFFFDAD6)
val DarkOutline = Color(0xFF8F8F8F)
val DarkOutlineVariant = Color(0xFF48484A)
val DarkShadow = Color(0xFF000000)
val DarkScrim = Color(0xFF000000)
val DarkInverseSurface = Color(0xFFF2F0F4)
val DarkInverseOnSurface = Color(0xFF2F3033)
val DarkInversePrimary = Color(0xFF0066CC)

// Semantic Colors for Pods
val ConnectedBlue = Color(0xFF0066CC)
val ConnectedBlueLight = Color(0xFF64B5F6)
val DisconnectedGray = Color(0xFF8E8E93)
val BatteryGreen = Color(0xFF34C759)
val BatteryAmber = Color(0xFFFF9F0A)
val BatteryRed = Color(0xFFFF3B30)

val LightColorScheme = lightColorScheme(
    background = LightBackground,
    surface = Color(0xFFFFFFFF),
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    surfaceContainerHighest = LightSurfaceContainerHighest,
    surfaceContainerLow = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1C1C1E),
    onSurface = Color(0xFF1C1C1E),
    onSurfaceVariant = Color(0xFF49454F),
    primary = LightPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimary = Color(0xFFFFFFFF),
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondary = Color(0xFFFFFFFF),
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = Color(0xFF6C5B7B),
    tertiaryContainer = Color(0xFFFADEF5),
    onTertiary = Color(0xFFFFFFFF),
    onTertiaryContainer = Color(0xFF231928),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFF410002),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFF2F3033),
    inverseOnSurface = Color(0xFFF2F0F4),
    inversePrimary = Color(0xFFB0D0FF)
)

val DarkColorScheme = darkColorScheme(
    background = DarkBackground,
    surface = DarkSurface,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest,
    surfaceContainerLow = Color(0xFF1C1C1E),
    onBackground = Color(0xFFF2F2F7),
    onSurface = Color(0xFFF2F2F7),
    onSurfaceVariant = Color(0xFFCAC4D0),
    primary = DarkPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimary = DarkOnPrimary,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondary = DarkOnSecondary,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = Color(0xFFD0BCFF),
    tertiaryContainer = Color(0xFF3D2B44),
    onTertiary = Color(0xFF231928),
    onTertiaryContainer = Color(0xFFFADEF5),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = Color(0xFF8F8F8F),
    outlineVariant = Color(0xFF48484A),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFF2F0F4),
    inverseOnSurface = Color(0xFF2F3033),
    inversePrimary = Color(0xFF0066CC)
)

val ColorScheme.isLight: Boolean
    get() = this == LightColorScheme

fun ColorScheme.connectedColor(): Color = if (isLight) LightPrimary else DarkPrimary
fun ColorScheme.disconnectedColor(): Color = Color(0xFF8E8E93)
fun ColorScheme.batteryColor(level: Int): Color = when {
    level > 60 -> Color(0xFF34C759)
    level > 20 -> Color(0xFFFF9F0A)
    else -> Color(0xFFFF3B30)
}
