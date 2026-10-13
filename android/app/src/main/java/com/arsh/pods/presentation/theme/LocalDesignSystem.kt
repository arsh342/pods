/*
    Pods - AirPods on Android
    Copyright (C) 2025 Pods contributors

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

package com.arsh.pods.presentation.theme

import androidx.compose.runtime.compositionLocalOf

enum class DesignSystem {
    Apple,
    OneUI
}

val LocalDesignSystem = compositionLocalOf { DesignSystem.Apple }
