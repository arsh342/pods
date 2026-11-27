package com.arsh.pods.presentation.navigation

import androidx.compose.foundation.ScrollState
import androidx.activity.BackEventCompat.Companion.EDGE_LEFT
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.arsh.pods.bluetooth.AACPManager
import com.arsh.pods.data.updates.updates
import com.arsh.pods.presentation.screens.AccessibilitySettingsScreen
import com.arsh.pods.presentation.screens.AdaptiveStrengthScreen
import com.arsh.pods.presentation.screens.AirPodsSettingsRoute
import com.arsh.pods.presentation.screens.AppSettingsScreen
import com.arsh.pods.presentation.screens.CallControlScreen
import com.arsh.pods.presentation.screens.HeadTrackingScreen
import com.arsh.pods.presentation.screens.HearingAidAdjustmentsScreen
import com.arsh.pods.presentation.screens.HearingAidScreen
import com.arsh.pods.presentation.screens.HearingProtectionScreen
import com.arsh.pods.presentation.screens.LoadingScreen
import com.arsh.pods.presentation.screens.LongPress
import com.arsh.pods.presentation.screens.EqualizerScreen
import com.arsh.pods.presentation.screens.MicrophoneSettingsRoute
import com.arsh.pods.presentation.screens.OpenSourceLicensesScreen
import com.arsh.pods.presentation.screens.RenameScreen
import com.arsh.pods.presentation.screens.TransparencySettingsScreen
import com.arsh.pods.presentation.screens.UpdateHearingTestRoute
import com.arsh.pods.presentation.screens.VersionScreen
import com.arsh.pods.presentation.viewmodel.AirPodsViewModel
import com.arsh.pods.presentation.viewmodel.AppSettingsViewModel

@Composable
fun AppNavGraph(
    backStack: SnapshotStateList<Screen>,
    airPodsViewModel: AirPodsViewModel,
    currentTab: () -> MainTab,
    scrollState: (MainTab) -> ScrollState,
    pageFrame: @Composable (Screen, @Composable () -> Unit) -> Unit,
) {
    val navigate: (Screen) -> Unit = { screen ->
        backStack.add(screen)
    }

    fun page(screen: Screen, content: @Composable () -> Unit) =
        NavEntry(screen) { pageFrame(screen, content) }

    SharedTransitionLayout {
        NavDisplay(
            sharedTransitionScope = this,
            backStack = backStack,
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeAt(backStack.lastIndex)
                }
            },
            entryProvider = { screen ->
                when (screen) {
                    Screen.AirPodsSettings ->
                        page(screen) {
                            if (!airPodsViewModel.isReady) LoadingScreen()
                            val tab = currentTab()
                            AirPodsSettingsRoute(
                                viewModel = airPodsViewModel,
                                tab = tab,
                                scrollState = scrollState(tab),
                                navigateToAppSettings = { navigate(Screen.AppSettings) },
                                navigateToRename = { navigate(Screen.Rename) },
                                navigateToHearingProtection = { navigate(Screen.HearingProtection) },
                                navigateToHearingAid = { navigate(Screen.HearingAid) },
                                navigateToLeftLongPress = {
                                    navigate(
                                        Screen.LongPress("Left")
                                    )
                                },
                                navigateToRightLongPress = {
                                    navigate(
                                        Screen.LongPress("Right")
                                    )
                                },
                                navigateToAdaptiveStrength = { navigate(Screen.AdaptiveStrength) },
                                navigateToHeadTracking = { navigate(Screen.HeadTracking) },
                                navigateToAccessibility = { navigate(Screen.Accessibility) },
                                navigateToVersion = { navigate(Screen.VersionInfo) },
                                navigateToCallControlScreen = { navigate(Screen.CallControl(it)) },
                                navigateToMicrophoneSettings = { navigate(Screen.MicrophoneSettings) },
                                navigateToEqualizer = { navigate(Screen.Equalizer) },
                            )
                        }

                    Screen.Rename ->
                        page(screen) {
                            if (!airPodsViewModel.isReady) LoadingScreen()
                            RenameScreen(airPodsViewModel)
                        }

                    Screen.AppSettings ->
                        page(screen) {
                            val vm: AppSettingsViewModel = viewModel()
                            AppSettingsScreen(
                                viewModel = vm,
                                navigateToOpenSourceLicenses = { navigate(Screen.OpenSourceLicenses) }
                            )
                        }

                    Screen.HeadTracking ->
                        page(screen) {
                            if (!airPodsViewModel.isReady) LoadingScreen()
                            HeadTrackingScreen(airPodsViewModel)
                        }

                    Screen.Accessibility ->
                        page(screen) {
                            if (!airPodsViewModel.isReady) LoadingScreen()
                            AccessibilitySettingsScreen(
                                viewModel = airPodsViewModel,
                                navigateToTransparencyCustomization = { navigate(Screen.TransparencyCustomization) }
                            )
                        }

                    Screen.TransparencyCustomization ->
                        page(screen) {
                            if (!airPodsViewModel.isReady) LoadingScreen()
                            TransparencySettingsScreen(airPodsViewModel)
                        }

                    Screen.HearingAid ->
                        page(screen) {
                            if (!airPodsViewModel.isReady) LoadingScreen()
                            HearingAidScreen(
                                viewModel = airPodsViewModel,
                                onNavigateHearingAidAdjustments = { navigate(Screen.HearingAidAdjustments) },
                                onNavigateHearingTest = { navigate(Screen.UpdateHearingTest) },
                            )
                        }

                    Screen.HearingAidAdjustments ->
                        page(screen) {
                            if (!airPodsViewModel.isReady) LoadingScreen()
                            HearingAidAdjustmentsScreen(airPodsViewModel)
                        }

                    Screen.AdaptiveStrength ->
                        page(screen) {
                            if (!airPodsViewModel.isReady) LoadingScreen()
                            AdaptiveStrengthScreen(airPodsViewModel)
                        }

                    Screen.OpenSourceLicenses ->
                        page(screen) {
                            OpenSourceLicensesScreen()
                        }

                    Screen.UpdateHearingTest ->
                        page(screen) {
                            UpdateHearingTestRoute(airPodsViewModel)
                        }

                    Screen.VersionInfo ->
                        page(screen) {
                            if (!airPodsViewModel.isReady) LoadingScreen()
                            VersionScreen(airPodsViewModel)
                        }

                    Screen.HearingProtection ->
                        page(screen) {
                            if (!airPodsViewModel.isReady) LoadingScreen()
                            HearingProtectionScreen(
                                viewModel = airPodsViewModel,
                            )
                        }

                    is Screen.LongPress ->
                        page(screen) {
                            if (!airPodsViewModel.isReady) LoadingScreen()
                            LongPress(
                                viewModel = airPodsViewModel,
                                name = screen.bud,
                            )
                        }

                    is Screen.CallControl ->
                        page(screen) {
                            if (!airPodsViewModel.isReady) LoadingScreen()
                            CallControlScreen(
                                viewModel = airPodsViewModel,
                                action = screen.action,
                                onCallControlValueChanged = { flipped ->
                                    airPodsViewModel.setControlCommandValue(
                                        AACPManager.Companion.ControlCommandIdentifiers.CALL_MANAGEMENT_CONFIG,
                                        if (flipped) byteArrayOf(0x00, 0x02) else byteArrayOf(
                                            0x00,
                                            0x03
                                        )
                                    )
                                }
                            )
                        }

                    Screen.Equalizer ->
                        page(screen) {
                            if (!airPodsViewModel.isReady) LoadingScreen()
                            else EqualizerScreen(airPodsViewModel)
                        }

                    is Screen.MicrophoneSettings ->
                        page(screen) {
                            if (!airPodsViewModel.isReady) LoadingScreen()
                            MicrophoneSettingsRoute(viewModel = airPodsViewModel)
                        }
                }
            },
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { it / 4 },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeIn(tween(220)) togetherWith
                    slideOutHorizontally(
                        targetOffsetX = { -it / 6 },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    ) + fadeOut(tween(220))
            },
            popTransitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { -it / 4 },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeIn(tween(220)) togetherWith
                    slideOutHorizontally(
                        targetOffsetX = { it / 6 },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    ) + fadeOut(tween(220))
            },
            predictivePopTransitionSpec = { swipeEdge ->
                val enterOffset: (Int) -> Int =
                    if (swipeEdge == EDGE_LEFT) {
                        { -it / 4 }
                    } else {
                        { it / 4 }
                    }

                val exitOffset: (Int) -> Int =
                    if (swipeEdge == EDGE_LEFT) {
                        { it / 6 }
                    } else {
                        { -it / 6 }
                    }

                fadeIn(tween(220)) + slideInHorizontally(
                    initialOffsetX = enterOffset,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) togetherWith fadeOut(tween(220)) + slideOutHorizontally(
                    targetOffsetX = exitOffset,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            },
        )
    }
}
