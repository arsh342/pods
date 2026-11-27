package com.arsh.pods.presentation

import com.arsh.pods.presentation.widgets.WidgetShape
import com.arsh.pods.presentation.widgets.isLowBattery
import com.arsh.pods.presentation.widgets.widgetShape
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BatteryWidgetLayoutTest {
    @Test
    fun wideWidgetsUseTheRow() {
        assertEquals(WidgetShape.ROW, widgetShape(270f, 90f))
        assertEquals(WidgetShape.ROW, widgetShape(360f, 200f))
    }

    @Test
    fun tallWidgetsUseTheColumn() {
        assertEquals(WidgetShape.COLUMN, widgetShape(80f, 280f))
        assertEquals(WidgetShape.COLUMN, widgetShape(150f, 380f))
    }

    @Test
    fun squarishWidgetsUseTheGrid() {
        assertEquals(WidgetShape.GRID, widgetShape(180f, 180f))
        assertEquals(WidgetShape.GRID, widgetShape(70f, 90f))
    }

    @Test
    fun missingSizeFallsBackToTheDefaultRow() {
        assertEquals(WidgetShape.ROW, widgetShape(0f, 0f))
    }

    @Test
    fun ringsTurnRedAtTenPercentOrBelow() {
        assertTrue(isLowBattery(10))
        assertTrue(isLowBattery(1))
        assertFalse(isLowBattery(11))
        assertFalse(isLowBattery(0))
    }
}
