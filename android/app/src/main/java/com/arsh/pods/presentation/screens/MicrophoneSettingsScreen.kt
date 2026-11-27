package com.arsh.pods.presentation.screens

import com.arsh.pods.presentation.components.SegmentedSelector
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arsh.pods.R
import com.arsh.pods.bluetooth.AACPManager
import com.arsh.pods.presentation.components.StyledList
import com.arsh.pods.presentation.components.StyledListItem
import com.arsh.pods.presentation.viewmodel.AirPodsViewModel

@Composable
fun MicrophoneSettingsRoute(
    viewModel: AirPodsViewModel
) {
    val state by viewModel.uiState.collectAsState()    

val topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 64.dp
val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 40.dp

    val id = AACPManager.Companion.ControlCommandIdentifiers.MIC_MODE

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        MicrophoneSettingsScreen(
            selectedMode = state.controlStates[id]?.getOrNull(0)?.toInt() ?: 0,
            topPadding = topPadding,
            bottomPadding = bottomPadding,
            onMicrophoneSettingsChanged = {
                viewModel.setControlCommandInt(id, it)
            }
        )
    }
}

@Composable
fun MicrophoneSettingsScreen(
    selectedMode: Int,
    topPadding: Dp = 16.dp,
    bottomPadding: Dp = 16.dp,
    onMicrophoneSettingsChanged: (Int) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .verticalScroll(scrollState)
            .padding(top = 8.dp)
            .padding(horizontal = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(topPadding))

        SegmentedSelector(
            options = listOf(
                0 to stringResource(R.string.microphone_automatic),
                1 to stringResource(R.string.microphone_always_right),
                2 to stringResource(R.string.microphone_always_left)
            ),
            selected = selectedMode,
            onSelect = onMicrophoneSettingsChanged
        )

        Spacer(modifier = Modifier.height(bottomPadding))
    }
}
