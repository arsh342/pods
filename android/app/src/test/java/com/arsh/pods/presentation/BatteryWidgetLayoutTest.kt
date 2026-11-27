package com.arsh.pods.presentation

import com.arsh.pods.presentation.widgets.WidgetArrangement
import com.arsh.pods.presentation.widgets.WidgetShape.COLUMN
import com.arsh.pods.presentation.widgets.WidgetShape.GRID
import com.arsh.pods.presentation.widgets.WidgetShape.ROW
import com.arsh.pods.presentation.widgets.fittedSize
import com.arsh.pods.presentation.widgets.WidgetSlot.CASE
import com.arsh.pods.presentation.widgets.WidgetSlot.EMPTY
import com.arsh.pods.presentation.widgets.WidgetSlot.HEADSET
import com.arsh.pods.presentation.widgets.WidgetSlot.LEFT
import com.arsh.pods.presentation.widgets.WidgetSlot.LOWEST_BUD
import com.arsh.pods.presentation.widgets.WidgetSlot.PHONE
import com.arsh.pods.presentation.widgets.WidgetSlot.RIGHT
import com.arsh.pods.presentation.widgets.isLowBattery
import com.arsh.pods.presentation.widgets.widgetArrangement
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Sizes are what a Galaxy S24 home screen gives each number of cells, in dp on screen. */
class BatteryWidgetLayoutTest {
    private val everything = listOf(LEFT, RIGHT, CASE, PHONE)

    @Test
    fun twoCellsWideShowsOnlyTheEarbuds() {
        assertEquals(WidgetArrangement(ROW, listOf(LEFT, RIGHT)), widgetArrangement(147f, 73f))
    }

    @Test
    fun threeCellsWideAddsTheCase() {
        assertEquals(WidgetArrangement(ROW, listOf(LEFT, RIGHT, CASE)), widgetArrangement(231f, 75f))
    }

    @Test
    fun fourCellsWideAddsThePhone() {
        assertEquals(WidgetArrangement(ROW, everything), widgetArrangement(314f, 75f))
    }

    @Test
    fun twoByTwoShowsEverythingIncludingThePhone() {
        assertEquals(WidgetArrangement(GRID, everything, columns = 2), widgetArrangement(148f, 168f))
    }

    @Test
    fun roomForMoreRingsIsFilledWithEmptyOnes() {
        fun filled(count: Int) = everything + List(count - everything.size) { EMPTY }

        // 3x2, 2x3, 4x2 and 2x4 cells.
        assertEquals(WidgetArrangement(GRID, filled(6), columns = 3), widgetArrangement(231f, 168f))
        assertEquals(WidgetArrangement(GRID, filled(6), columns = 2), widgetArrangement(148f, 261f))
        assertEquals(WidgetArrangement(GRID, filled(8), columns = 4), widgetArrangement(314f, 168f))
        assertEquals(WidgetArrangement(GRID, filled(8), columns = 2), widgetArrangement(148f, 354f))
    }

    @Test
    fun largerThanFourByTwoShowsPictures() {
        val large = WidgetArrangement(GRID, everything, columns = 2, pictures = true)

        // 3x3, 4x3, 3x4 and 4x4 cells.
        listOf(231f to 261f, 314f to 261f, 231f to 354f, 314f to 354f)
            .forEach { assertEquals(large, widgetArrangement(it.first, it.second)) }
    }

    @Test
    fun oneCellWideStacksAsManyAsFit() {
        assertEquals(WidgetArrangement(COLUMN, listOf(LEFT, RIGHT)), widgetArrangement(65f, 168f))
        assertEquals(WidgetArrangement(COLUMN, listOf(LEFT, RIGHT, CASE)), widgetArrangement(65f, 261f))
        assertEquals(WidgetArrangement(COLUMN, everything), widgetArrangement(65f, 354f))
    }

    @Test
    fun aSingleCellShowsTheEarbudWithLessCharge() {
        assertEquals(WidgetArrangement(ROW, listOf(LOWEST_BUD)), widgetArrangement(65f, 75f))
    }

    @Test
    fun missingSizeFallsBackToTheDefaultThree() {
        assertEquals(WidgetArrangement(ROW, listOf(LEFT, RIGHT, CASE)), widgetArrangement(0f, 0f))
    }

    @Test
    fun ringsTurnRedAtTenPercentOrBelow() {
        assertTrue(isLowBattery(10))
        assertTrue(isLowBattery(1))
        assertFalse(isLowBattery(11))
        assertFalse(isLowBattery(0))
    }

    @Test
    fun iconsKeepTheirProportionsInsideTheirBox() {
        // The earbud drawing is tall and the case drawing is wide.
        val (budWidth, budHeight) = fittedSize(94f, 123f, 20f)
        assertEquals(20f, budHeight, 0.01f)
        assertEquals(15.28f, budWidth, 0.01f)

        val (caseWidth, caseHeight) = fittedSize(141f, 109f, 20f)
        assertEquals(20f, caseWidth, 0.01f)
        assertEquals(15.46f, caseHeight, 0.01f)
    }

    @Test
    fun anIconWithoutASizeFillsTheBox() {
        assertEquals(20f to 20f, fittedSize(-1f, -1f, 20f))
    }

    @Test
    fun headphonesTakeThePlaceOfEarbudsAndCase() {
        fun max(width: Float, height: Float) = widgetArrangement(width, height, headphones = true)

        assertEquals(WidgetArrangement(ROW, listOf(HEADSET, PHONE)), max(147f, 73f))
        assertEquals(
            WidgetArrangement(GRID, listOf(HEADSET, PHONE, EMPTY, EMPTY), columns = 2), max(148f, 168f)
        )
        assertEquals(
            WidgetArrangement(GRID, listOf(HEADSET, PHONE), columns = 2, pictures = true), max(314f, 354f)
        )
        assertEquals(WidgetArrangement(COLUMN, listOf(HEADSET, PHONE)), max(65f, 261f))
        assertEquals(WidgetArrangement(ROW, listOf(HEADSET)), max(65f, 75f))
    }
}
