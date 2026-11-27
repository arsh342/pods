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

package com.arsh.pods.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp

/** A gradient from the page colour to clear, so content fades out towards the top or bottom edge. */
@Composable
fun BoxScope.EdgeFade(atTop: Boolean, height: Dp) {
    val background = MaterialTheme.colorScheme.surfaceContainer
    // Light enough that content stays visible while it passes under the edge.
    val stops = if (atTop) arrayOf(
        0f to background.copy(alpha = 0.95f),
        0.6f to background.copy(alpha = 0.75f),
        1f to background.copy(alpha = 0f)
    ) else arrayOf(
        0f to background.copy(alpha = 0f),
        0.5f to background.copy(alpha = 0.6f),
        1f to background.copy(alpha = 0.95f)
    )

    Box(
        modifier = Modifier
            .align(if (atTop) Alignment.TopCenter else Alignment.BottomCenter)
            .fillMaxWidth()
            .height(height)
            .background(Brush.verticalGradient(colorStops = stops))
    )
}
