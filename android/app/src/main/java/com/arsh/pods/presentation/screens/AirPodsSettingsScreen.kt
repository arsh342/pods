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

@file:OptIn(ExperimentalEncodingApi::class)

package com.arsh.pods.presentation.screens

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.ScrollState
import com.arsh.pods.presentation.components.OneUiPage
import com.arsh.pods.presentation.components.title
import com.arsh.pods.presentation.navigation.MainSetting
import com.arsh.pods.presentation.navigation.MainTab
import com.arsh.pods.presentation.navigation.settingsOn
import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.material3.toPath
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.net.toUri
import androidx.graphics.shapes.Morph
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import kotlinx.coroutines.delay
import com.arsh.pods.R
import com.arsh.pods.bluetooth.AACPManager
import com.arsh.pods.bluetooth.ATTHandles
import com.arsh.pods.presentation.MaterialIcons
import com.arsh.pods.presentation.components.AboutCard
import com.arsh.pods.presentation.components.BatteryView
import com.arsh.pods.presentation.components.CallControlSettings
import com.arsh.pods.presentation.components.HearingHealthSettings
import com.arsh.pods.presentation.components.NoiseControlSettings
import com.arsh.pods.presentation.components.PressAndHoldSettings
import com.arsh.pods.presentation.components.StyledListItem
import com.arsh.pods.presentation.components.StyledList
import com.arsh.pods.presentation.components.StyledToggle
import com.arsh.pods.presentation.theme.DesignSystem
import com.arsh.pods.presentation.theme.PodsTheme
import com.arsh.pods.presentation.theme.LocalDesignSystem
import com.arsh.pods.presentation.viewmodel.AirPodsUiState
import com.arsh.pods.presentation.viewmodel.AirPodsViewModel
import com.arsh.pods.presentation.viewmodel.demoState
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.min
import kotlin.time.Duration.Companion.seconds

@Composable
fun AirPodsSettingsRoute(
    viewModel: AirPodsViewModel,
    navigateToRename: () -> Unit,
    navigateToHearingProtection: () -> Unit,
    navigateToHearingAid: () -> Unit,
    navigateToLeftLongPress: () -> Unit,
    navigateToRightLongPress: () -> Unit,
        navigateToAdaptiveStrength: () -> Unit,
    navigateToHeadTracking: () -> Unit,
    navigateToAccessibility: () -> Unit,
    navigateToVersion: () -> Unit,
    navigateToCallControlScreen: (action: String) -> Unit,
    navigateToMicrophoneSettings: () -> Unit,
    navigateToEqualizer: () -> Unit,
    navigateToAppSettings: () -> Unit,
    tab: MainTab,
    scrollState: ScrollState,
) {
    val state by viewModel.uiState.collectAsState()

    val m3eEnabled = LocalDesignSystem.current == DesignSystem.OneUI
    val topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() +
        if (m3eEnabled) 64.dp else 8.dp
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 40.dp

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        AirPodsSettingsScreen(
            state = state,

            topPadding = topPadding,
            bottomPadding = bottomPadding,

            setControlCommandInt = viewModel::setControlCommandInt,
            setControlCommandBoolean = viewModel::setControlCommandBoolean,
            setControlCommandByte = viewModel::setControlCommandByte,

            setATTCharacteristicValue = viewModel::setATTCharacteristicValue,

            onAutomaticEarDetectionChanged = viewModel::setAutomaticEarDetectionEnabled,
            onAutomaticConnectionChanged = viewModel::setAutomaticConnectionEnabled,
            setDynamicEndOfCharge = viewModel::setDynamicEndOfCharge,
            setCaseSounds = viewModel::setCaseSounds,
            setOffListeningMode = viewModel::setOffListeningMode,
            disconnect = viewModel::disconnect,

            navigateToRename = navigateToRename,
            navigateToHearingProtection = navigateToHearingProtection,
            navigateToHearingAid = navigateToHearingAid,
            navigateToLeftLongPress = navigateToLeftLongPress,
            navigateToRightLongPress = navigateToRightLongPress,
                        navigateToAdaptiveStrength = navigateToAdaptiveStrength,
            navigateToHeadTracking = navigateToHeadTracking,
            navigateToAccessibility = navigateToAccessibility,
            navigateToVersion = navigateToVersion,
            navigateToCallControlScreen = navigateToCallControlScreen,
            navigateToMicrophoneSettings = navigateToMicrophoneSettings,
            navigateToEqualizer = navigateToEqualizer,

            activateDemoMode = viewModel::activateDemoMode,
            reconnectFromSavedMac = viewModel::reconnectFromSavedMac,
            navigateToAppSettings = navigateToAppSettings,
            tab = tab,
            scrollState = scrollState
        )
    }
}

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@SuppressLint("MissingPermission", "UnspecifiedRegisterReceiverFlag")
@Composable
fun AirPodsSettingsScreen(
        state: AirPodsUiState,

        topPadding: Dp = 16.dp,
        bottomPadding: Dp = 16.dp,

        setControlCommandInt: (AACPManager.Companion.ControlCommandIdentifiers, Int) -> Unit,
        setControlCommandBoolean: (AACPManager.Companion.ControlCommandIdentifiers, Boolean) -> Unit,
        setControlCommandByte: (AACPManager.Companion.ControlCommandIdentifiers, Byte) -> Unit,
        setATTCharacteristicValue: (ATTHandles, ByteArray) -> Unit,

        onAutomaticEarDetectionChanged: (Boolean) -> Unit,
        onAutomaticConnectionChanged: (Boolean) -> Unit,
        setDynamicEndOfCharge: (Boolean) -> Unit,
        setCaseSounds: (Boolean) -> Unit = {},
        setOffListeningMode: (Boolean) -> Unit,
        disconnect: () -> Unit,

        navigateToRename: () -> Unit,
        navigateToHearingProtection: () -> Unit,
        navigateToHearingAid: () -> Unit,
        navigateToLeftLongPress: () -> Unit,
        navigateToRightLongPress: () -> Unit,
                navigateToAdaptiveStrength: () -> Unit,
            navigateToHeadTracking: () -> Unit,
        navigateToAccessibility: () -> Unit,
        navigateToVersion: () -> Unit,
            navigateToCallControlScreen: (action: String) -> Unit,
        navigateToMicrophoneSettings: () -> Unit,
        navigateToEqualizer: () -> Unit,

        activateDemoMode: () -> Unit,
        reconnectFromSavedMac: () -> Unit,
        navigateToAppSettings: () -> Unit = {},
        tab: MainTab = MainTab.HOME,
        scrollState: ScrollState = rememberScrollState(),
) {
    val sharedPreferences = LocalContext.current.getSharedPreferences("settings", MODE_PRIVATE)
    val isAppleDesign = LocalDesignSystem.current == DesignSystem.Apple
    var deviceName by remember {
        mutableStateOf(
            TextFieldValue(
                sharedPreferences.getString("name", state.deviceName).toString()
            )
        )
    }

    val nameChangeListener = remember {
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == "name") {
                deviceName =
                    TextFieldValue(sharedPreferences.getString("name", "AirPods Pro").toString())
            }
        }
    }

    DisposableEffect(Unit) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(nameChangeListener)
        onDispose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(nameChangeListener)
        }
    }

    if (state.isLocallyConnected) {
        val settings = settingsOn(tab, state.capabilities, state.vendorIdHook)
        fun shown(setting: MainSetting) = setting in settings
        fun any(vararg group: MainSetting) = group.any(::shown)
        fun flag(id: AACPManager.Companion.ControlCommandIdentifiers) =
            state.controlStates[id]?.getOrNull(0) == 0x01.toByte()

        OneUiPage(scrollState = scrollState) {
            if (shown(MainSetting.BATTERY)) Column {
                BatteryView(
                    batteryList = state.battery,
                    budsRes = state.instance?.model?.budsRes ?: R.drawable.airpods_pro_2_buds,
                    caseRes = state.instance?.model?.caseRes ?: R.drawable.airpods_pro_2_case
                )
            }

            if (shown(MainSetting.LISTENING_MODE)) Column {
                NoiseControlSettings(
                    showOffListeningMode = state.offListeningMode,
                    noiseControlModeValue = state.controlStates[AACPManager.Companion.ControlCommandIdentifiers.LISTENING_MODE]
                        ?.getOrNull(0)?.toInt() ?: 3,
                    onNoiseControlModeChanged = {
                        setControlCommandInt(AACPManager.Companion.ControlCommandIdentifiers.LISTENING_MODE, it)
                    },
                )
            }

            if (any(MainSetting.CONVERSATION_AWARENESS, MainSetting.EQUALIZER)) Column {
                StyledList {
                    if (shown(MainSetting.CONVERSATION_AWARENESS)) {
                        val id = AACPManager.Companion.ControlCommandIdentifiers.CONVERSATION_DETECT_CONFIG
                        StyledToggle(
                            label = stringResource(R.string.conversational_awareness),
                            checked = flag(id),
                            onCheckedChange = { setControlCommandBoolean(id, it) }
                        )
                    }
                    if (shown(MainSetting.EQUALIZER)) {
                        StyledListItem(name = stringResource(R.string.equalizer), onClick = navigateToEqualizer)
                    }
                }
            }

            if (shown(MainSetting.PRESS_AND_HOLD)) Column {
                PressAndHoldSettings(
                    leftAction = state.leftAction,
                    rightAction = state.rightAction,
                    navigateToLeftLongPress = navigateToLeftLongPress,
                    navigateToRightLongPress = navigateToRightLongPress
                )
            }

            if (shown(MainSetting.CALL_CONTROLS)) Column {
                val bytes = state.controlStates[AACPManager.Companion.ControlCommandIdentifiers.CALL_MANAGEMENT_CONFIG]
                CallControlSettings(
                    flipped = bytes?.getOrNull(1) == 0x02.toByte(),
                    navigateToCallControlScreen = navigateToCallControlScreen
                )
            }

            if (shown(MainSetting.HEAD_GESTURES)) Column {
                StyledList {
                    StyledListItem(name = stringResource(R.string.head_gestures), onClick = navigateToHeadTracking)
                }
            }

            if (any(
                    MainSetting.PERSONALIZED_VOLUME, MainSetting.ADAPTIVE_AUDIO,
                    MainSetting.MICROPHONE, MainSetting.LOUD_SOUND_REDUCTION
                )
            ) Column {
                StyledList {
                    if (shown(MainSetting.PERSONALIZED_VOLUME)) {
                        val id = AACPManager.Companion.ControlCommandIdentifiers.ADAPTIVE_VOLUME_CONFIG
                        StyledToggle(
                            label = stringResource(R.string.personalized_volume),
                            description = stringResource(R.string.personalized_volume_description),
                            checked = flag(id),
                            onCheckedChange = { setControlCommandBoolean(id, it) }
                        )
                    }
                    if (shown(MainSetting.ADAPTIVE_AUDIO)) {
                        StyledListItem(name = stringResource(R.string.adaptive_audio), onClick = navigateToAdaptiveStrength)
                    }
                    if (shown(MainSetting.MICROPHONE)) {
                        StyledListItem(name = stringResource(R.string.microphone_mode), onClick = navigateToMicrophoneSettings)
                    }
                    if (shown(MainSetting.LOUD_SOUND_REDUCTION)) {
                        StyledToggle(
                            label = stringResource(R.string.loud_sound_reduction),
                            description = stringResource(R.string.loud_sound_reduction_description),
                            checked = state.loudSoundReductionEnabled,
                            onCheckedChange = {
                                setATTCharacteristicValue(
                                    ATTHandles.LOUD_SOUND_REDUCTION,
                                    byteArrayOf(if (it) 0x01 else 0x00)
                                )
                            }
                        )
                    }
                }
            }

            if (any(MainSetting.HEARING_PROTECTION, MainSetting.HEARING_AID)) Column {
                HearingHealthSettings(
                    hasPPECapability = shown(MainSetting.HEARING_PROTECTION),
                    hasHearingAidCapability = shown(MainSetting.HEARING_AID),
                    vendorIdHook = state.vendorIdHook,
                    navigateToHearingProtection = navigateToHearingProtection,
                    navigateToHearingAid = navigateToHearingAid
                )
            }

            if (shown(MainSetting.NAME)) Column {
                StyledList {
                    StyledListItem(
                        name = stringResource(R.string.name),
                        description = deviceName.text,
                        onClick = navigateToRename
                    )
                }
            }

            if (any(
                    MainSetting.EAR_DETECTION, MainSetting.AUTO_CONNECT, MainSetting.OPTIMIZED_CHARGING,
                    MainSetting.SLEEP_DETECTION, MainSetting.CASE_SOUNDS, MainSetting.OFF_LISTENING_MODE
                )
            ) Column {
                StyledList {
                    if (shown(MainSetting.EAR_DETECTION)) {
                        StyledToggle(
                            label = stringResource(R.string.ear_detection),
                            checked = state.automaticEarDetectionEnabled,
                            onCheckedChange = onAutomaticEarDetectionChanged
                        )
                    }
                    if (shown(MainSetting.AUTO_CONNECT)) {
                        StyledToggle(
                            label = stringResource(R.string.automatically_connect),
                            description = stringResource(R.string.automatically_connect_description),
                            checked = state.automaticConnectionEnabled,
                            onCheckedChange = onAutomaticConnectionChanged
                        )
                    }
                    if (shown(MainSetting.OPTIMIZED_CHARGING)) {
                        StyledToggle(
                            label = stringResource(R.string.optimized_charging),
                            checked = state.dynamicEndOfCharge,
                            onCheckedChange = setDynamicEndOfCharge
                        )
                    }
                    if (shown(MainSetting.SLEEP_DETECTION)) {
                        val id = AACPManager.Companion.ControlCommandIdentifiers.SLEEP_DETECTION_CONFIG
                        StyledToggle(
                            label = stringResource(R.string.sleep_detection),
                            checked = flag(id),
                            onCheckedChange = { setControlCommandBoolean(id, it) }
                        )
                    }
                    if (shown(MainSetting.CASE_SOUNDS)) {
                        StyledToggle(
                            label = stringResource(R.string.case_sounds),
                            description = stringResource(R.string.case_sounds_description),
                            checked = state.caseSounds,
                            onCheckedChange = setCaseSounds
                        )
                    }
                    if (shown(MainSetting.OFF_LISTENING_MODE)) {
                        StyledToggle(
                            label = stringResource(R.string.off_listening_mode),
                            description = stringResource(R.string.off_listening_mode_description),
                            checked = flag(AACPManager.Companion.ControlCommandIdentifiers.ALLOW_OFF_OPTION),
                            onCheckedChange = setOffListeningMode
                        )
                    }
                }
            }

            if (any(MainSetting.ACCESSIBILITY, MainSetting.APP_SETTINGS)) Column {
                StyledList {
                    if (shown(MainSetting.ACCESSIBILITY)) {
                        StyledListItem(name = stringResource(R.string.accessibility), onClick = navigateToAccessibility)
                    }
                    if (shown(MainSetting.APP_SETTINGS)) {
                        StyledListItem(name = stringResource(R.string.app_settings), onClick = navigateToAppSettings)
                    }
                }
            }

            if (shown(MainSetting.ABOUT)) Column {
                AboutCard(
                    modelName = state.modelName,
                    actualModel = state.actualModel,
                    serialNumbers = state.serialNumbers,
                    version = state.version3,
                    navigateToVersion = navigateToVersion
                )
            }

            if (shown(MainSetting.DISCONNECT)) Column {
                val red = MaterialTheme.colorScheme.error
                Button(
                    onClick = disconnect,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = red.copy(alpha = 0.16f),
                        contentColor = red
                    )
                ) {
                    Text(
                        text = stringResource(R.string.disconnect),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp, bottom = bottomPadding),
            contentAlignment = Alignment.Center
        ) {
            val tapCount = remember { mutableIntStateOf(0) }
            val lastTapTime = remember { mutableLongStateOf(0L) }

            var reconnecting by remember { mutableStateOf(false) }

            LaunchedEffect(reconnecting) {
                if (reconnecting) {
                    delay(5.seconds)
                    reconnecting = false
                }
            }

            when (LocalDesignSystem.current) {
                DesignSystem.OneUI -> {
                    val polygons = remember {
                        listOf(
                            MaterialShapes.Cookie9Sided,
                            MaterialShapes.Clover4Leaf,
                            MaterialShapes.SoftBurst,
                            MaterialShapes.Sunny,
                            MaterialShapes.Pentagon,
                            MaterialShapes.Cookie4Sided,
                            MaterialShapes.Oval,
                        )
                    }

                    val morphs = remember {
                        buildList {
                            for (i in polygons.indices) {
                                add(
                                    Morph(
                                        polygons[i].normalized(),
                                        polygons[(i + 1) % polygons.size].normalized()
                                    )
                                )
                            }
                        }
                    }

                    var currentMorphIndex by remember { mutableIntStateOf(0) }

                    val morphProgress = remember { Animatable(0f) }

                    LaunchedEffect(reconnecting) {
                        if (!reconnecting) {
                            currentMorphIndex = 0
                            morphProgress.snapTo(0f)
                            return@LaunchedEffect
                        }

                        while (reconnecting) {
                            morphProgress.snapTo(0f)

                            morphProgress.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(
                                    durationMillis = 650,
                                    easing = FastOutSlowInEasing
                                )
                            )

                            currentMorphIndex = (currentMorphIndex + 1) % morphs.size
                        }
                    }

                    val path = remember { Path() }
                    val scaleMatrix = remember { Matrix() }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        activateDemoMode()
                                    }
                                )
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 32.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = {
                                            val now = System.currentTimeMillis()

                                            if (now - lastTapTime.longValue > 400) {
                                                tapCount.intValue = 0
                                            }

                                            tapCount.intValue++
                                            lastTapTime.longValue = now

                                            if (tapCount.intValue >= 5) {
                                                tapCount.intValue = 0
                                                activateDemoMode()
                                            }
                                        }
                                    )
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val primaryContainerColor = MaterialTheme.colorScheme.tertiaryContainer
                            val secondaryContainerColor = MaterialTheme.colorScheme.secondaryContainer

                            val animatedShapeColor by animateColorAsState(if (reconnecting) primaryContainerColor else secondaryContainerColor)

                            if (state.connectionSuccessful) {
                                Box(
                                    modifier = Modifier
                                        .size(240.dp)
                                        .background(
                                            MaterialTheme.colorScheme.surfaceBright,
                                            CircleShape
                                        )
                                        .clickable(
                                            interactionSource = null,
                                            indication = ripple(
                                                bounded = false,
                                                radius = 120.dp
                                            ),
                                            enabled = !reconnecting,
                                            onClick = {}
                                        )
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onTap = {
                                                    if (!reconnecting) {
                                                        currentMorphIndex = 1
                                                        reconnecting = true
                                                        reconnectFromSavedMac()
                                                    }
                                                },
                                                onPress = {
                                                    if (!reconnecting) {
                                                        morphProgress.animateTo(
                                                            targetValue = 1f,
                                                            animationSpec = spring(
                                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                                stiffness = Spring.StiffnessLow
                                                            )
                                                        )
                                                        tryAwaitRelease()
                                                        morphProgress.animateTo(
                                                            targetValue = 0f,
                                                            animationSpec = spring(
                                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                                stiffness = Spring.StiffnessLow
                                                            )
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                        .drawWithContent {
                                            val activeMorph = morphs[currentMorphIndex]

                                            val shapePath = activeMorph.toPath(
                                                progress = morphProgress.value,
                                                path = path
                                            )

                                            val bounds = shapePath.getBounds()

                                            val scale = min(
                                                size.width / bounds.width,
                                                size.height / bounds.height
                                            ) * 0.8f

                                            scaleMatrix.reset()

                                            scaleMatrix.scale(x = scale, y = scale)

                                            shapePath.transform(scaleMatrix)

                                            shapePath.translate(size.center - shapePath.getBounds().center)

                                            drawPath(
                                                path = shapePath,
                                                color = animatedShapeColor
                                            )

                                            drawContent()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (reconnecting) MaterialIcons.bluetooth_searching else MaterialIcons.headset_off,
                                        contentDescription = null,
                                        modifier = Modifier.size(84.dp),
                                        tint = if (reconnecting) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }

                                Spacer(Modifier.height(40.dp))

                                Text(
                                    text = if (reconnecting) stringResource(R.string.reconnecting) else stringResource(R.string.tap_to_reconnect),
                                    style = MaterialTheme.typography.labelSmallEmphasized,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            } else {
                                Text(
                                    text = stringResource(R.string.airpods_not_connected),
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                if (sharedPreferences.getBoolean("bypass_device_check.v2", false)) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = stringResource(R.string.compatibility_check_bypassed),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }


                    }
                }

                DesignSystem.Apple -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = {
                                            val now = System.currentTimeMillis()

                                            if (now - lastTapTime.longValue > 400) {
                                                tapCount.intValue = 0
                                            }

                                            tapCount.intValue++
                                            lastTapTime.longValue = now

                                            if (tapCount.intValue >= 5) {
                                                tapCount.intValue = 0
                                                activateDemoMode()
                                            }
                                        })
                                }) {
                            Text(
                                text = stringResource(R.string.airpods_not_connected),
                                style = MaterialTheme.typography.displaySmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(24.dp))
                        }

                        if (state.connectionSuccessful) {
                            OutlinedButton(
                                onClick = { reconnectFromSavedMac(); reconnecting = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                enabled = !reconnecting
                            ) {
                                Text(
                                    text = stringResource(R.string.reconnect_to_last_device),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        OutlinedButton(
                            onClick = navigateToAppSettings,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.app_settings),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }


                }
            }
        }
    }
}

@Preview(name = "Apple")
@Composable
fun AirPodsSettingsScreenPreviewApple() {
    PodsTheme(
        m3eEnabled = false
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            AirPodsSettingsScreen(
                state = demoState,

                setControlCommandInt = { _, _ -> },
                setControlCommandBoolean = { _, _ -> },
                setControlCommandByte = { _, _ -> },
                setATTCharacteristicValue = { _, _ -> },

                onAutomaticEarDetectionChanged = {},
                onAutomaticConnectionChanged = {},
                setDynamicEndOfCharge = {},
                setOffListeningMode = {},
                disconnect = {},

                navigateToRename = {},
                navigateToHearingProtection = {},
                navigateToHearingAid = {},
                navigateToLeftLongPress = {},
                navigateToRightLongPress = {},
                                navigateToAdaptiveStrength = {},
                        navigateToHeadTracking = {},
                navigateToAccessibility = {},
                navigateToVersion = {},
                navigateToCallControlScreen = {},
                navigateToMicrophoneSettings = {},
                navigateToEqualizer = {},

                activateDemoMode = {},
                reconnectFromSavedMac = {}
            )
        }
    }
}


@Preview(name = "Material")
@Composable
fun AirPodsSettingsScreenPreviewMaterial() {
    PodsTheme(
        m3eEnabled = true
    ) {
        Box (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            AirPodsSettingsScreen(
                state = demoState,

                setControlCommandInt = { _, _ -> },
                setControlCommandBoolean = { _, _ -> },
                setControlCommandByte = { _, _ -> },
                setATTCharacteristicValue = { _, _ -> },

                onAutomaticEarDetectionChanged = {},
                onAutomaticConnectionChanged = {},
                setDynamicEndOfCharge = {},
                setOffListeningMode = {},
                disconnect = {},

                navigateToRename = {},
                navigateToHearingProtection = {},
                navigateToHearingAid = {},
                navigateToLeftLongPress = {},
                navigateToRightLongPress = {},
                                navigateToAdaptiveStrength = {},
                        navigateToHeadTracking = {},
                navigateToAccessibility = {},
                navigateToVersion = {},
                navigateToCallControlScreen = {},
                navigateToMicrophoneSettings = {},
                navigateToEqualizer = {},

                activateDemoMode = {},
                reconnectFromSavedMac = {}
            )
        }
    }
}
