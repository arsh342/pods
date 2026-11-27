package com.arsh.pods.presentation

import com.arsh.pods.presentation.widgets.WidgetTheme
import com.arsh.pods.presentation.widgets.backgroundAlpha
import com.arsh.pods.presentation.widgets.usesDarkColours
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WidgetAppearanceTest {
    @Test
    fun systemFollowsThePhone() {
        assertTrue(WidgetTheme.SYSTEM.usesDarkColours(phoneIsDark = true))
        assertFalse(WidgetTheme.SYSTEM.usesDarkColours(phoneIsDark = false))
    }

    @Test
    fun darkAndLightIgnoreThePhone() {
        assertTrue(WidgetTheme.DARK.usesDarkColours(phoneIsDark = false))
        assertFalse(WidgetTheme.LIGHT.usesDarkColours(phoneIsDark = true))
    }

    @Test
    fun transparencyBecomesBackgroundOpacity() {
        assertEquals(255, backgroundAlpha(transparencyPercent = 0))
        assertEquals(0, backgroundAlpha(transparencyPercent = 100))
        // The default matches the Galaxy battery widget.
        assertEquals(166, backgroundAlpha(transparencyPercent = 35))
    }

    @Test
    fun transparencyOutsideTheSliderIsClamped() {
        assertEquals(255, backgroundAlpha(-20))
        assertEquals(0, backgroundAlpha(140))
    }
}
