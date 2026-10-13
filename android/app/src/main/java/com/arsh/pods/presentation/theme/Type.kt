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

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.arsh.pods.R

val sfProFamily = FontFamily(Font(R.font.sf_pro))

// One UI Typography Scale (SamsungOne-inspired, using SF Pro as fallback)
val OneUITypography = Typography(
    // Display styles - Large titles for hero sections
    displayLarge = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight(600),
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = -0.5.sp
    ),
    displayMedium = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight(600),
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = -0.25.sp
    ),
    displaySmall = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight(600),
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),

    // Headline styles - Section headers
    headlineLarge = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight(600),
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight(600),
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight(600),
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // Title styles - Component titles
    titleLarge = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight(600),
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight(600),
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight(600),
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Body styles - Main content
    bodyLarge = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // Label styles - Buttons, chips, labels
    labelLarge = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight(600),
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight(500),
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight(500),
        fontSize = 11.sp,
        lineHeight = 13.sp,
        letterSpacing = 0.5.sp
    ),

    // Emphasized variants
    displayLargeEmphasized = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = -0.5.sp
    ),
    displayMediumEmphasized = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = -0.25.sp
    ),
    displaySmallEmphasized = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    headlineLargeEmphasized = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMediumEmphasized = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmallEmphasized = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLargeEmphasized = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMediumEmphasized = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmallEmphasized = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLargeEmphasized = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight(600),
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMediumEmphasized = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight(600),
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmallEmphasized = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight(600),
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLargeEmphasized = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMediumEmphasized = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmallEmphasized = TextStyle(
        fontFamily = sfProFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        lineHeight = 13.sp,
        letterSpacing = 0.5.sp
    )
)