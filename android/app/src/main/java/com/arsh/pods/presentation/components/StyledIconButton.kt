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

import androidx.compose.animation.core.animateFloatAsState
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.layer.CompositingStrategy
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCoerceAtMost
import androidx.compose.ui.util.fastCoerceIn
import androidx.compose.ui.util.lerp
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.highlight.Highlight
import com.kyant.backdrop.shadow.InnerShadow
import kotlinx.coroutines.launch
import com.arsh.pods.R
import com.arsh.pods.presentation.theme.DesignSystem
import com.arsh.pods.presentation.theme.LocalDesignSystem
import com.arsh.pods.utils.inspectDragGestures
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tanh

@Composable
fun StyledIconButton(
    modifier: Modifier = Modifier,
    icon: String,
    iconTint: Color = Color.Unspecified,
    surfaceColor: Color = Color.Unspecified,
    backdrop: LayerBackdrop = rememberLayerBackdrop(),
    onClick: () -> Unit,
    enabled: Boolean = true,
    materialButtonStyle: MaterialButtonStyle = MaterialButtonStyle.Normal,
    // False draws the bare icon; the circle behind it fades in when true.
    raised: Boolean = true
) {
    when (LocalDesignSystem.current) {
        DesignSystem.OneUI -> {
            when (materialButtonStyle) {
                MaterialButtonStyle.Tonal -> {
                    FilledTonalIconButton(
                        onClick = onClick,
                        enabled = enabled,
                        modifier = Modifier.size(52.dp)
                    ) {
                        Text(
                            text = icon,
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                fontFamily = FontFamily(Font(R.font.sf_pro))
                            )
                        )
                    }
                }
                MaterialButtonStyle.Filled -> {
                    FilledIconButton(
                        onClick = onClick,
                        enabled = enabled,
                        modifier = Modifier.size(52.dp)
                    ) {
                        Text(
                            text = icon,
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                fontFamily = FontFamily(Font(R.font.sf_pro))
                            )
                        )
                    }
                }
                MaterialButtonStyle.Outlined -> {
                    OutlinedIconButton(
                        onClick = onClick,
                        enabled = enabled,
                        modifier = Modifier.size(52.dp)
                    ) {
                        Text(
                            text = icon,
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                fontFamily = FontFamily(Font(R.font.sf_pro))
                            )
                        )
                    }
                }
                MaterialButtonStyle.Normal -> {
                    IconButton(
                        onClick = onClick,
                        enabled = enabled,
                        modifier = Modifier.size(52.dp)
                    ) {
                        Text(
                            text = icon,
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                fontFamily = FontFamily(Font(R.font.sf_pro))
                            )
                        )
                    }
                }
            }
        }
        DesignSystem.Apple -> {
            val haptics = LocalHapticFeedback.current
            val isDarkTheme = isSystemInDarkTheme()
            val backgroundColor = if (surfaceColor.isSpecified) {
                surfaceColor
            } else if (isDarkTheme) {
                Color(0xFF252527)
            } else {
                Color(0xFFE8E8EC)
            }
            val foregroundColor = if (iconTint.isSpecified) {
                iconTint
            } else {
                androidx.compose.material3.MaterialTheme.colorScheme.onSurface
            }

            val circle by animateFloatAsState(if (raised) 1f else 0f, label = "button circle")

            Box(
                modifier = modifier
                    .padding(horizontal = 12.dp)
                    .size(48.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(backgroundColor.copy(alpha = backgroundColor.alpha * circle))
                    .clickable(enabled = enabled, role = Role.Button) {
                        haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
                        onClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    color = foregroundColor,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontFamily = FontFamily(Font(R.font.sf_pro))
                    )
                )
            }
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO, name = "Light")
@Preview(uiMode = UI_MODE_NIGHT_YES, name = "Dark")
@Composable
fun StyledIconButtonPreview() {
    Box(modifier = Modifier
        .height(120.dp)
        .width(200.dp)
        .background(
            if (isSystemInDarkTheme()) Color(0xFF000000) else Color(0xFFF2F2F7),
            RoundedCornerShape(28.dp)
        ), contentAlignment = Alignment.Center) {
        StyledIconButton(
            icon = "􀍟",
            onClick = { }
        )
    }
}
