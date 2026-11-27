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

import androidx.compose.ui.unit.dp
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

/** The line between rows of a list block. */
val ColorScheme.listDivider: Color
    get() = onSurface.copy(alpha = 0.16f)

val ListDividerThickness = 0.7.dp

val ColorScheme.sectionHeader: Color
    get() = onSurfaceVariant

val ColorScheme.connectedColor: Color
    get() = if (isLight) ConnectedBlue else ConnectedBlueLight

val ColorScheme.disconnectedColor: Color
    get() = DisconnectedGray

// One UI Light Color Scheme
private val OneUiLightColorScheme = lightColorScheme(
    background = LightBackground,
    surface = LightSurface,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    surfaceContainerHighest = LightSurfaceContainerHighest,
    surfaceContainerLow = LightSurface,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface,
    onSurfaceVariant = LightOnSurfaceVariant,
    primary = LightPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimary = LightOnPrimary,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondary = LightOnSecondary,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiary = LightOnTertiary,
    onTertiaryContainer = LightOnTertiaryContainer,
    error = LightError,
    errorContainer = LightErrorContainer,
    onError = LightOnError,
    onErrorContainer = LightOnErrorContainer,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    scrim = LightScrim,
    inverseSurface = LightInverseSurface,
    inverseOnSurface = LightInverseOnSurface,
    inversePrimary = LightInversePrimary
)

// One UI Dark Color Scheme
private val OneUiDarkColorScheme = darkColorScheme(
    background = DarkBackground,
    surface = DarkSurface,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest,
    surfaceContainerLow = DarkSurface,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfaceVariant,
    primary = DarkPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimary = DarkOnPrimary,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondary = DarkOnSecondary,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiary = DarkOnTertiary,
    onTertiaryContainer = DarkOnTertiaryContainer,
    error = DarkError,
    errorContainer = DarkErrorContainer,
    onError = DarkOnError,
    onErrorContainer = DarkOnErrorContainer,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    scrim = DarkScrim,
    inverseSurface = DarkInverseSurface,
    inverseOnSurface = DarkInverseOnSurface,
    inversePrimary = DarkInversePrimary
)

@Composable
fun PodsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    m3eEnabled: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) OneUiDarkColorScheme else OneUiLightColorScheme

    CompositionLocalProvider(
        LocalDesignSystem provides if (m3eEnabled) DesignSystem.OneUI else DesignSystem.Apple
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = OneUITypography,
            shapes = androidx.compose.material3.Shapes(
                extraSmall = ShapeExtraSmall,
                small = ShapeSmall,
                medium = ShapeMedium,
                large = ShapeLarge,
                extraLarge = ShapeExtraLarge
            ),
            content = content
        )
    }
}
