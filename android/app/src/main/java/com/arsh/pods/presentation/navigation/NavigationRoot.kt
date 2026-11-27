package com.arsh.pods.presentation.navigation

import androidx.compose.runtime.mutableStateMapOf
import com.arsh.pods.presentation.components.OneUiPageTop
import com.arsh.pods.presentation.components.CollapsingPage
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.arsh.pods.presentation.components.FloatingTabBar
import com.arsh.pods.presentation.components.title
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationRoot(
    airPodsViewModel: AirPodsViewModel
) {
    val backStack = remember {
        mutableStateListOf<Screen>(Screen.AirPodsSettings)
    }

    val currentScreen = backStack.last()

    val state by airPodsViewModel.uiState.collectAsState()

    @Composable
    fun titleOf(screen: Screen) = when (screen) {
        Screen.AirPodsSettings -> if (state.isLocallyConnected) state.deviceName else stringResource(R.string.app_name)
        Screen.Accessibility -> stringResource(R.string.accessibility)
        Screen.AdaptiveStrength -> stringResource(R.string.customize_adaptive_audio)
        Screen.AppSettings -> stringResource(R.string.settings)
        Screen.HeadTracking -> stringResource(R.string.head_tracking)
        Screen.HearingAid -> stringResource(R.string.hearing_aid)
        Screen.HearingAidAdjustments -> stringResource(R.string.adjustments)
        Screen.HearingProtection -> stringResource(R.string.hearing_protection)
        is Screen.LongPress -> screen.bud
        Screen.OpenSourceLicenses -> stringResource(R.string.open_source_licenses)
        Screen.Rename -> stringResource(R.string.name)
        Screen.TransparencyCustomization -> stringResource(R.string.customize_transparency_mode)
        Screen.UpdateHearingTest -> stringResource(R.string.update_hearing_test)
        Screen.VersionInfo -> stringResource(R.string.version)
        is Screen.CallControl -> screen.action
        Screen.MicrophoneSettings -> stringResource(R.string.microphone_mode)
        Screen.Equalizer -> stringResource(R.string.equalizer)
    }

    val actionButtons = when (currentScreen) {
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

    var tab by rememberSaveable { mutableStateOf(MainTab.HOME) }
    val scrollStates = MainTab.entries.associateWith { key(it) { rememberScrollState() } }
    val onMainScreen = backStack.size == 1
    val showTabs = onMainScreen && state.isLocallyConnected

    LaunchedEffect(state.isLocallyConnected) {
        if (!state.isLocallyConnected) tab = MainTab.HOME
    }

    // One header position per main page, so switching tabs keeps each as it was left.
    val tabHeaders = MainTab.entries.associateWith { key(it) { rememberTopAppBarState() } }
    val statusBar = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    // Which pages have scrolled under the header buttons.
    val scrolledPages = remember { mutableStateMapOf<Screen, Boolean>() }

    StyledScaffold(
        contentScrolled = scrolledPages[currentScreen] == true,
        showBackButton = !onMainScreen,
        onNavigateBack = { backStack.removeAt(backStack.lastIndex) },
        actionButtons = actionButtons,
        bottomBar = if (showTabs) { hazeState ->
            FloatingTabBar(selected = tab, onSelect = { tab = it }, hazeState = hazeState)
        } else null
    ) {
        AppNavGraph(
            backStack = backStack,
            airPodsViewModel = airPodsViewModel,
            currentTab = { tab },
            scrollState = { scrollStates.getValue(it) },
            pageFrame = { screen, content ->
                when {
                    screen != Screen.AirPodsSettings -> CollapsingPage(
                        title = titleOf(screen),
                        state = rememberTopAppBarState(),
                        // Matches the room these screens leave for the back button.
                        collapsedTop = statusBar + 64.dp,
                        showSmallTitle = true,
                        onScrolledChange = { scrolledPages[screen] = it },
                        content = content
                    )
                    // Without AirPods the first page is a centred message with no header.
                    !state.isLocallyConnected -> content()
                    else -> CollapsingPage(
                        title = if (tab == MainTab.HOME) state.deviceName else tab.title(),
                        state = tabHeaders.getValue(tab),
                        collapsedTop = statusBar + OneUiPageTop,
                        showSmallTitle = false,
                        content = content
                    )
                }
            }
        )
        BackHandler(enabled = onMainScreen && tab != MainTab.HOME) { tab = MainTab.HOME }
    }
}
