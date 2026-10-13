package com.arsh.pods.data.updates

import androidx.compose.runtime.Composable
import com.arsh.pods.R
import com.arsh.pods.presentation.screens.AirPodsSettingsScreenPreviewMaterial

val update1_0_0 = listOf(
        UpdateItem(
            titleRes = R.string.material3e,
            descriptionRes = R.string.update_m3e_description,
            demoComposeable = @Composable {
                AirPodsSettingsScreenPreviewMaterial()
            }
        ),
    )

val updates = update1_0_0
