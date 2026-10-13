/*
    Pods - AirPods liberated from Apple's ecosystem
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

import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.arsh.pods.R
import com.arsh.pods.presentation.components.StyledButton
import com.arsh.pods.presentation.components.StyledIconButton
import com.arsh.pods.presentation.components.StyledList
import com.arsh.pods.presentation.components.StyledSlider
import com.arsh.pods.presentation.components.StyledToggle
import com.arsh.pods.presentation.MaterialIcons
import com.arsh.pods.presentation.theme.OneUITypography
import com.arsh.pods.presentation.theme.DesignSystem
import com.arsh.pods.presentation.theme.LocalDesignSystem
import com.arsh.pods.presentation.viewmodel.AppSettingsViewModel
import com.arsh.pods.utils.XposedState
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettingsScreen(
    viewModel: AppSettingsViewModel = viewModel(),
    navigateToOpenSourceLicenses: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val state by viewModel.uiState.collectAsState()

    val backdrop = rememberLayerBackdrop()

    val isAppleDesign = LocalDesignSystem.current == DesignSystem.Apple
    val topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() +
        if (isAppleDesign) 60.dp else 84.dp
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 12.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .layerBackdrop(backdrop)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(topPadding))

        val isDarkTheme = isSystemInDarkTheme()

        if (state.connectionSuccessful) {
            StyledList (title = stringResource(R.string.conversational_awareness)) {
                StyledToggle(
                    label = stringResource(R.string.conversational_awareness_pause_music),
                    description = stringResource(R.string.conversational_awareness_pause_music_description),
                    checked = state.conversationalAwarenessPauseMusicEnabled,
                    onCheckedChange = viewModel::setConversationalAwarenessPauseMusicEnabled,
                )

                StyledToggle(
                    label = stringResource(R.string.relative_conversational_awareness_volume),
                    description = stringResource(R.string.relative_conversational_awareness_volume_description),
                    checked = state.relativeConversationalAwarenessVolumeEnabled,
                    onCheckedChange = viewModel::setRelativeConversationalAwarenessVolumeEnabled,
                )

                item { index, count ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.conversational_awareness_volume),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StyledSlider(
                                modifier = Modifier.weight(1f),
                                value = state.conversationalAwarenessVolume,
                                valueRange = 10f..85f,
                                snapPoints = listOf(44f),
                                onValueChange = viewModel::setConversationalAwarenessVolume,
                                            index = index,
                                count = count
                            )
                            Icon(
                                imageVector = MaterialIcons.volume_up,
                                contentDescription = stringResource(R.string.conversational_awareness_volume),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val conversationalAwarenessVolume = state.conversationalAwarenessVolume
            LaunchedEffect(conversationalAwarenessVolume) {
                viewModel.setConversationalAwarenessVolume(conversationalAwarenessVolume)
            }
            if (context.checkSelfPermission("android.permission.BLUETOOTH_PRIVILEGED") == PackageManager.PERMISSION_GRANTED) {
                StyledToggle(
                    title = stringResource(R.string.ear_detection),
                    label = stringResource(R.string.disconnect_when_not_wearing),
                    description = stringResource(R.string.disconnect_when_not_wearing_description),
                    checked = state.disconnectWhenNotWearing,
                    onCheckedChange = viewModel::setDisconnectWhenNotWearing,
                )
            }

            StyledList(title = stringResource(R.string.takeover_airpods_state)) {
                StyledToggle(
                    label = stringResource(R.string.takeover_disconnected),
                    description = stringResource(R.string.takeover_disconnected_desc),
                    checked = state.takeoverWhenDisconnected,
                    onCheckedChange = viewModel::setTakeoverWhenDisconnected,
                )
                StyledToggle(
                    label = stringResource(R.string.takeover_idle),
                    description = stringResource(R.string.takeover_idle_desc),
                    checked = state.takeoverWhenIdle,
                    onCheckedChange = viewModel::setTakeoverWhenIdle,
                )
                StyledToggle(
                    label = stringResource(R.string.takeover_music),
                    description = stringResource(R.string.takeover_music_desc),
                    checked = state.takeoverWhenMusic,
                    onCheckedChange = viewModel::setTakeoverWhenMusic,
                )

                StyledToggle(
                    label = stringResource(R.string.takeover_call),
                    description = stringResource(R.string.takeover_call_desc),
                    checked = state.takeoverWhenCall,
                    onCheckedChange = viewModel::setTakeoverWhenCall,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            StyledList(title = stringResource(R.string.takeover_phone_state)) {
                StyledToggle(
                    label = stringResource(R.string.takeover_ringing_call),
                    description = stringResource(R.string.takeover_ringing_call_desc),
                    checked = state.takeoverWhenRingingCall,
                    onCheckedChange = viewModel::setTakeoverWhenRingingCall,
                )
                StyledToggle(
                    label = stringResource(R.string.takeover_media_start),
                    description = stringResource(R.string.takeover_media_start_desc),
                    checked = state.takeoverWhenMediaStart,
                    onCheckedChange = viewModel::setTakeoverWhenMediaStart,
                )
            }

            StyledToggle(
                title = stringResource(R.string.advanced_options),
                label = stringResource(R.string.use_alternate_head_tracking_packets),
                description = stringResource(R.string.use_alternate_head_tracking_packets_description),
                checked = state.useAlternateHeadTrackingPackets,
                onCheckedChange = viewModel::setUseAlternateHeadTrackingPackets,
            )
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp, bottom = 2.dp)
            ) {
                Text(
                    text = stringResource(R.string.customizations_unavailable),
                    style = OneUITypography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                )
            }
        }

        if (XposedState.isAvailable && XposedState.bluetoothScopeEnabled) {
            val restartBluetoothText = stringResource(R.string.found_offset_restart_bluetooth)
            StyledToggle(
                label = stringResource(R.string.act_as_an_apple_device) + " (${
                    stringResource(
                        R.string.requires_xposed
                    )
                })",
                description = stringResource(R.string.act_as_an_apple_device_description),
                checked = state.vendorIdHook,
                onCheckedChange = { enabled ->
                    Toast.makeText(context, restartBluetoothText, Toast.LENGTH_SHORT).show()
                    viewModel.setVendorIdHook(enabled)
                }
            )
        }

        Spacer(modifier = Modifier.height(bottomPadding))

        if (state.showCameraDialog) {
            AlertDialog(onDismissRequest = { viewModel.setShowCameraDialog(false) }, title = {
                Text(
                    stringResource(R.string.set_custom_camera_package),
                    fontFamily = FontFamily(Font(R.font.sf_pro)),
                    fontWeight = FontWeight.Medium
                )
            }, text = {
                Column {
                    Text(
                        stringResource(R.string.enter_custom_camera_package),
                        fontFamily = FontFamily(Font(R.font.sf_pro)),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = state.cameraPackageValue,
                        onValueChange = {
                            viewModel.setCameraPackageValue(it)
                            viewModel.setCameraPackageError(null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.cameraPackageError != null,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Ascii,
                            capitalization = KeyboardCapitalization.None
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (isDarkTheme) Color(0xFF007AFF) else Color(
                                0xFF3C6DF5
                            ),
                            unfocusedBorderColor = if (isDarkTheme) Color.Gray else Color.LightGray
                        ),
                        supportingText = {
                            if (state.cameraPackageError != null) {
                                Text(
                                    state.cameraPackageError ?: "",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        label = { Text(stringResource(R.string.custom_camera_package)) })
                }
            }, confirmButton = {
                val successText = stringResource(R.string.custom_camera_package_set_success)
                TextButton(
                    onClick = {
                        viewModel.saveCameraPackage()
                        Toast.makeText(context, successText, Toast.LENGTH_SHORT).show()
                    }) {
                    Text(
                        "Save",
                        fontFamily = FontFamily(Font(R.font.sf_pro)),
                        fontWeight = FontWeight.Medium
                    )
                }
            }, dismissButton = {
                TextButton(
                    onClick = { viewModel.setShowCameraDialog(false) }) {
                    Text(
                        "Cancel",
                        fontFamily = FontFamily(Font(R.font.sf_pro)),
                        fontWeight = FontWeight.Medium
                    )
                }
            })
        }
    }
}
