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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arsh.pods.R
import com.arsh.pods.presentation.navigation.MainTab
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.oneuiproject.oneui.R as OneUiR

private val MainTab.label: Int
    get() = when (this) {
        MainTab.HOME -> R.string.tab_home
        MainTab.CONTROLS -> R.string.tab_controls
        MainTab.SOUND -> R.string.tab_sound
        MainTab.SETTINGS -> R.string.tab_settings
    }

// Filled for the selected tab, outlined for the others.
private fun MainTab.icon(selected: Boolean): Int = when (this) {
    MainTab.HOME -> if (selected) OneUiR.drawable.ic_oui_home else R.drawable.ic_tab_home_outline
    MainTab.CONTROLS -> if (selected) R.drawable.ic_tab_controls else OneUiR.drawable.ic_oui_control_device
    MainTab.SOUND -> if (selected) OneUiR.drawable.ic_oui_wave else R.drawable.ic_tab_wave_outline
    MainTab.SETTINGS ->
        if (selected) OneUiR.drawable.ic_oui_settings else OneUiR.drawable.ic_oui_settings_outline
}

@Composable
fun MainTab.title(): String = stringResource(label)

private val ItemHeight = 48.dp
private val MaxBarWidth = 336.dp

/** The floating capsule used to move between the main pages, styled like One UI's bottom tabs. */
@Composable
fun FloatingTabBar(
    selected: MainTab,
    onSelect: (MainTab) -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier
) {
    val barColor = if (isSystemInDarkTheme()) Color(0xFF252527) else Color(0xFFFFFFFF)
    val contentColor = MaterialTheme.colorScheme.onSurface

    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .widthIn(max = MaxBarWidth)
            .fillMaxWidth()
            .shadow(6.dp, CircleShape)
            .clip(CircleShape)
            .hazeEffect(hazeState) {
                blurRadius = 12.dp
                backgroundColor = barColor
                tints = listOf(HazeTint(barColor.copy(alpha = 0.9f)))
                noiseFactor = 0f
            }
            .padding(4.dp)
    ) {
        MainTab.entries.forEach { tab ->
            val isSelected = tab == selected
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(ItemHeight)
                    .clip(CircleShape)
                    .background(if (isSelected) contentColor.copy(alpha = 0.11f) else Color.Transparent)
                    .selectable(selected = isSelected, role = Role.Tab) { onSelect(tab) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(tab.icon(isSelected)),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = tab.title(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                        letterSpacing = 0.sp
                    ),
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
