package com.arsh.pods.presentation

import com.arsh.pods.presentation.components.bandOffsetLabel
import com.arsh.pods.presentation.components.bandValueAt
import org.junit.Assert.assertEquals
import org.junit.Test

class EqGraphTest {
    // A graph drawn between y = 20 (top, 100) and y = 220 (bottom, 0).
    private fun at(y: Float) = bandValueAt(y, top = 20f, bottom = 220f)

    @Test
    fun theTopIsMostAndTheBottomLeast() {
        assertEquals(100, at(20f))
        assertEquals(0, at(220f))
        assertEquals(50, at(120f))
    }

    @Test
    fun dragsPastTheEdgesStayInRange() {
        assertEquals(100, at(-50f))
        assertEquals(0, at(400f))
    }

    @Test
    fun valuesNearTheMiddleSettleOnIt() {
        assertEquals(50, at(116f))
        assertEquals(50, at(124f))
    }

    @Test
    fun labelsShowTheChangeFromFlat() {
        assertEquals("+12", bandOffsetLabel(62))
        assertEquals("−8", bandOffsetLabel(42))
        assertEquals("0", bandOffsetLabel(50))
    }
}
