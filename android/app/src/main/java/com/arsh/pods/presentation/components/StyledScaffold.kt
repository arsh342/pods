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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

/**
 * Page frame: content runs edge to edge with a short fade at the top and bottom. The back button,
 * action buttons and tab bar float above it. Pages draw their own titles.
 */
@Composable
fun StyledScaffold(
    showBackButton: Boolean = false,
    // True once the page has scrolled under the header buttons.
    contentScrolled: Boolean = false,
    onNavigateBack: () -> Unit = {},
    actionButtons: List<@Composable (backdrop: LayerBackdrop) -> Unit> = emptyList(),
    bottomBar: (@Composable (HazeState) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val hazeState = rememberHazeState(blurEnabled = true)
    val backdrop = rememberLayerBackdrop()
    val top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val background = MaterialTheme.colorScheme.surfaceContainer

    // Surface supplies the default text and icon colour for the active theme.
    Surface(color = background, contentColor = MaterialTheme.colorScheme.onSurface) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(hazeState)
                    .background(background)
            ) {
                content()
            }

            EdgeFade(atTop = true, height = top + 16.dp)
            EdgeFade(atTop = false, height = bottom + if (bottomBar != null) 72.dp else 16.dp)

            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showBackButton) {
                    StyledIconButton(
                        onClick = onNavigateBack,
                        icon = "􀯶",
                        backdrop = backdrop,
                        raised = contentScrolled
                    )
                } else {
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Spacer(modifier = Modifier.weight(1f).height(48.dp))
                actionButtons.forEach { it(backdrop) }
            }

            bottomBar?.let {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(bottom = 9.dp)
                ) {
                    it(hazeState)
                }
            }
        }
    }
}
