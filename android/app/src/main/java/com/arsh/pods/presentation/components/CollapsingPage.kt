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

package com.arsh.pods.presentation.components

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

// Share of the screen the expanded header takes, with the title in its middle.
private const val ExpandedFraction = 0.375f

/**
 * A page with One UI's expandable header. It opens with the title centred in the top third; scrolling
 * up closes that space first and then scrolls the content, and letting go half way snaps to one end.
 *
 * [content] must already leave [collapsedTop] free above its first item.
 * With [showSmallTitle], the title sits beside the back button while the header is closed and the
 * content is at its top.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingPage(
    title: String,
    state: TopAppBarState,
    collapsedTop: Dp,
    showSmallTitle: Boolean,
    onScrolledChange: (Boolean) -> Unit = {},
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val expandedHeight = with(density) {
        (LocalWindowInfo.current.containerSize.height * ExpandedFraction).toDp()
    }
    // How far the header can travel. Zero on screens too short to have room for it.
    val range = with(density) { (expandedHeight - collapsedTop).toPx() }.coerceAtLeast(0f)
    SideEffect { state.heightOffsetLimit = -range }
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state)
    // Lets pages that do not scroll by themselves still open and close the header.
    val idleScroll = rememberScrollableState { 0f }
    val atTop by remember(state) { derivedStateOf { state.contentOffset > -1f } }
    LaunchedEffect(atTop) { onScrolledChange(!atTop) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .nestedScroll(behavior.nestedScrollConnection)
            .scrollable(idleScroll, Orientation.Vertical)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                // Moved at placement time, so the page is not measured again on every frame.
                .offset { IntOffset(0, (range + state.heightOffset.coerceAtLeast(-range)).roundToInt()) }
        ) {
            content()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(expandedHeight)
                .graphicsLayer {
                    translationY = state.heightOffset / 2
                    alpha = (1f - state.collapsedFraction * 2).coerceIn(0f, 1f)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 36.sp, lineHeight = 44.sp, fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        if (showSmallTitle) {
            val visibility by animateFloatAsState(if (atTop) 1f else 0f, label = "small title")
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 8.dp, start = 60.dp, end = 72.dp)
                    .height(48.dp)
                    .graphicsLayer {
                        alpha = visibility * (state.collapsedFraction * 2 - 1f).coerceIn(0f, 1f)
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
