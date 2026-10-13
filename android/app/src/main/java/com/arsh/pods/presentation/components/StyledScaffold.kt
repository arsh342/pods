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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.background
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import com.arsh.pods.R
import com.arsh.pods.presentation.theme.DesignSystem
import com.arsh.pods.presentation.theme.LocalDesignSystem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledScaffold(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    title: String,
    showBackButton: Boolean = false,
    showTitle: Boolean = true,
    onNavigateBack: () -> Unit = {},
    actionButtons: List<@Composable (backdrop: LayerBackdrop) -> Unit> = emptyList(),
    expandedTitle: Boolean = false,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    content: @Composable () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val hazeState = rememberHazeState(blurEnabled = true)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    when (LocalDesignSystem.current) {
        DesignSystem.OneUI -> {
            Scaffold(
                modifier = if (expandedTitle) Modifier.nestedScroll(scrollBehavior.nestedScrollConnection) else Modifier,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
                    ) {
                        val navigation: @Composable () -> Unit = {
                            if (showBackButton) {
                                Row {
                                    Spacer(modifier = Modifier.width(12.dp))
                                    FilledTonalIconButton(
                                        onClick = onNavigateBack,
                                        modifier = Modifier
                                            .minimumInteractiveComponentSize()
                                            .size(IconButtonDefaults.mediumContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow)),
                                        shape = IconButtonDefaults.mediumRoundShape
                                    ) {
                                        Icon(
                                            Icons.AutoMirrored.Default.ArrowBack,
                                            contentDescription = "Back",
                                            modifier = Modifier.size(IconButtonDefaults.mediumIconSize),
                                        )
                                    }
                                }
                            }
                        }
                        val titleContent: @Composable () -> Unit = {
                            Crossfade(targetState = title) {
                                Text(
                                    text = it,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }
                        val actionsContent: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit = {
                            actionButtons.forEach { actionButton -> actionButton(rememberLayerBackdrop()) }
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        if (expandedTitle) LargeTopAppBar(
                            navigationIcon = navigation,
                            title = titleContent,
                            actions = actionsContent,
                            scrollBehavior = scrollBehavior,
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                            )
                        ) else TopAppBar(
                            navigationIcon = {
                                navigation()
                            },
                            title = titleContent,
                            actions = actionsContent,
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                        )
                    }
                },
            ) { paddingValues ->
                Box(
                    modifier = modifier
                        .then(if (visible) Modifier.padding(paddingValues) else Modifier)
                        .fillMaxSize()
                        .hazeSource(hazeState)
                ) {
                    content()
                }
            }
        }
        DesignSystem.Apple -> {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                modifier = Modifier
            ) { paddingValues ->
                val topPadding = paddingValues.calculateTopPadding()
                val startPadding = paddingValues.calculateLeftPadding(LocalLayoutDirection.current)
                val endPadding = paddingValues.calculateRightPadding(LocalLayoutDirection.current)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = startPadding, end = endPadding)
                ) {
                    val backdrop = rememberLayerBackdrop()
                    val bgColor = MaterialTheme.colorScheme.surfaceContainer
                    AnimatedVisibility(
                        visible = visible && !expandedTitle && (showTitle || showBackButton),
                        enter = fadeIn(tween(220)) + slideInVertically(
                            initialOffsetY = { -it / 8 },
                            animationSpec = tween(260)
                        ),
                        exit = fadeOut(tween(180)) + slideOutVertically(
                            targetOffsetY = { -it / 8 },
                            animationSpec = tween(220)
                        ),
                        modifier = Modifier
                            .zIndex(2f)
                            .height(88.dp + topPadding)
                            .fillMaxWidth()
                    ){
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(if (showTitle) bgColor else Color.Transparent)
                            ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterStart)
                                    .padding(start = 8.dp, end = 20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (showBackButton) {
                                    StyledIconButton(
                                        onClick = onNavigateBack,
                                        icon = "􀯶",
                                        backdrop = backdrop
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                } else {
                                    Spacer(modifier = Modifier.width(12.dp))
                                }
                                if (showTitle) {
                                    Crossfade(targetState = title) {
                                        Text(
                                            text = it,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            style = if (showBackButton) {
                                                MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isDarkTheme) Color.White else Color.Black,
                                                    fontFamily = FontFamily(Font(R.font.sf_pro))
                                                )
                                            } else TextStyle(
                                                fontSize = 30.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = if (isDarkTheme) Color.White else Color.Black,
                                                fontFamily = FontFamily(Font(R.font.sf_pro))
                                            ),
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Start
                                        )
                                    }
                                }
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = visible && actionButtons.isNotEmpty(),
                        enter = fadeIn() + scaleIn(
                            initialScale = 0f,
                            animationSpec = tween()
                        ),
                        exit = fadeOut() + scaleOut(
                            targetScale = 0.5f,
                            animationSpec = tween(100)
                        ),
                        modifier = Modifier
                            .zIndex(3f)
                            .padding(top = topPadding, end = 8.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Row{
                            actionButtons.forEach { actionButton ->
                                actionButton(backdrop)
                            }
                        }
                    }

                    Box(
                        modifier = modifier
                            .hazeSource(hazeState)
                            .fillMaxSize()
                    ) {
                        content()
                    }
                }
            }
        }
    }
}
