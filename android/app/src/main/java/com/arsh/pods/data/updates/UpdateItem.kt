package com.arsh.pods.data.updates

import androidx.compose.runtime.Composable

data class UpdateItem(
    val titleRes: Int,
    val descriptionRes: Int,
    val demoComposeable: @Composable () -> Unit
)
