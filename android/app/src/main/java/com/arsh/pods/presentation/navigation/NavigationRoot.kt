package com.arsh.pods.presentation.navigation

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.arsh.pods.R
import com.arsh.pods.presentation.MaterialIcons
import com.arsh.pods.presentation.components.StyledIconButton
import com.arsh.pods.presentation.components.StyledScaffold
import com.arsh.pods.presentation.viewmodel.AirPodsViewModel

@Composable
fun NavigationRoot(
    airPodsViewModel: AirPodsViewModel
) {
    val backStack = remember {
        mutableStateListOf<Screen>(Screen.AirPodsSettings)
    }

    val currentScreen = backStack.last()

    val state by airPodsViewModel.uiState.collectAsState()

    val title = when (currentScreen) {
        Screen.AirPodsSettings -> if (state.isLocallyConnected) state.deviceName else stringResource(R.string.app_name)
        Screen.Accessibility -> stringResource(R.string.accessibility)
        Screen.AdaptiveStrength -> stringResource(R.string.customize_adaptive_audio)
        Screen.AppSettings -> stringResource(R.string.settings)
        Screen.HeadTracking -> stringResource(R.string.head_tracking)
        Screen.HearingAid -> stringResource(R.string.hearing_aid)
        Screen.HearingAidAdjustments -> stringResource(R.string.adjustments)
        Screen.HearingProtection -> stringResource(R.string.hearing_protection)
        is Screen.LongPress -> currentScreen.bud
        Screen.OpenSourceLicenses -> stringResource(R.string.open_source_licenses)
        Screen.Rename -> stringResource(R.string.name)
        Screen.TransparencyCustomization -> stringResource(R.string.customize_transparency_mode)
        Screen.UpdateHearingTest -> stringResource(R.string.update_hearing_test)
        Screen.VersionInfo -> stringResource(R.string.version)
        is Screen.CallControl -> currentScreen.action
        Screen.MicrophoneSettings -> stringResource(R.string.microphone_mode)
    }

    val actionButtons = when (currentScreen) {
        Screen.AirPodsSettings -> listOf<@Composable (backdrop: LayerBackdrop) -> Unit>(
            { scaffoldBackdrop ->
                StyledIconButton(
                    onClick = { backStack.add(Screen.AppSettings) },
                    icon = "☰",
                    backdrop = scaffoldBackdrop
                )
            }
        )
        Screen.HeadTracking -> listOf<@Composable (backdrop: LayerBackdrop) -> Unit>(
            { scaffoldBackdrop ->
                StyledIconButton(
                    onClick = {
                        if (!state.headTrackingActive) {
                            airPodsViewModel.startHeadTracking()
                            Log.d("HeadTrackingScreen", "Head tracking started")
                        } else {
                            airPodsViewModel.stopHeadTracking()
                            Log.d("HeadTrackingScreen", "Head tracking stopped")
                        }
                    },
                    icon = if (state.headTrackingActive) "􀊅" else "􀊃",
                    backdrop = scaffoldBackdrop
                )
            }
        )
        else -> listOf()
    }

    StyledScaffold(
        visible = true,
        title = title,
        expandedTitle = currentScreen == Screen.AirPodsSettings,
        showTitle = currentScreen != Screen.AirPodsSettings &&
            currentScreen != Screen.AppSettings &&
            currentScreen != Screen.Accessibility,
        showBackButton = backStack.size > 1,
        onNavigateBack = { backStack.removeAt(backStack.lastIndex) },
        actionButtons = actionButtons
    ) {
        AppNavGraph(
            backStack = backStack,
            airPodsViewModel = airPodsViewModel,
        )
    }
}
