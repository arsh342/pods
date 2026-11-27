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

package com.arsh.pods.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arsh.pods.R
import com.arsh.pods.presentation.components.StyledList
import com.arsh.pods.presentation.components.StyledListItem
import com.arsh.pods.presentation.viewmodel.AirPodsViewModel

@Composable
fun VersionScreen(viewModel: AirPodsViewModel) {
    val state by viewModel.uiState.collectAsState()    

val topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 64.dp
val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 40.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(topPadding))
        StyledList(title = stringResource(R.string.version)) {
            StyledListItem(
                name = stringResource(R.string.version) + " 1",
                description = state.version1,
                enabled = false
            )

            StyledListItem(
                name = stringResource(R.string.version) + " 2",
                description = state.version2,
                enabled = false
            )

            StyledListItem(
                name = stringResource(R.string.version) + " 3",
                description = state.version3,
                enabled = false
            )
        }
        Spacer(modifier = Modifier.height(bottomPadding))
    }
}
