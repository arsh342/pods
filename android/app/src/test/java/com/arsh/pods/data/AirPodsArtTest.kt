package com.arsh.pods.data

import com.arsh.pods.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AirPodsArtTest {
    private fun model(number: String) = AirPodsModels.getModelByModelNumber(number)!!

    @Test
    fun everyModelShowsItsOwnPictures() {
        val expected = mapOf(
            "A1523" to (R.drawable.airpods_1_buds to R.drawable.airpods_1_case),
            "A2032" to (R.drawable.airpods_2_buds to R.drawable.airpods_2_case),
            "A2565" to (R.drawable.airpods_3_buds to R.drawable.airpods_3_case),
            "A3053" to (R.drawable.airpods_4_buds to R.drawable.airpods_4_case),
            "A3055" to (R.drawable.airpods_4_anc_buds to R.drawable.airpods_4_anc_case),
            "A3531" to (R.drawable.airpods_5_buds to R.drawable.airpods_5_case),
            "A3439" to (R.drawable.airpods_5_wireless_buds to R.drawable.airpods_5_wireless_case),
            "A2084" to (R.drawable.airpods_pro_1_buds to R.drawable.airpods_pro_1_case),
            "A2931" to (R.drawable.airpods_pro_2_buds to R.drawable.airpods_pro_2_case),
            "A3047" to (R.drawable.airpods_pro_2_buds to R.drawable.airpods_pro_2_case),
            "A3063" to (R.drawable.airpods_pro_3_buds to R.drawable.airpods_pro_3_case),
            "A2096" to (R.drawable.airpods_max to R.drawable.airpods_max),
            "A3184" to (R.drawable.airpods_max to R.drawable.airpods_max)
        )

        expected.forEach { (number, pictures) ->
            assertEquals(number, pictures.first, model(number).budsRes)
            assertEquals(number, pictures.second, model(number).caseRes)
        }
        // Nothing registered is left unchecked.
        AirPodsModels.models.forEach { registered ->
            assertTrue(registered.name, registered.modelNumber.any { it in expected })
        }
    }

    private fun icons(number: String) = model(number).let {
        listOf(it.leftIconRes, it.rightIconRes, it.caseIconRes, it.statusIconRes)
    }

    @Test
    fun theFirstAirPodsHaveLongStemmedIcons() {
        val classic = listOf(
            R.drawable.airpods_classic_left_notification, R.drawable.airpods_classic_right_notification,
            R.drawable.airpods_classic_case_notification, R.drawable.ic_status_airpods_classic
        )
        listOf("A1523", "A2032").forEach { assertEquals(it, classic, icons(it)) }
    }

    @Test
    fun openFitModelsShareTheirIcons() {
        val openFit = listOf(
            R.drawable.airpods_4_left_notification, R.drawable.airpods_4_right_notification,
            R.drawable.airpods_4_case_notification, R.drawable.ic_status_airpods_open
        )
        listOf("A2565", "A3053", "A3055", "A3531", "A3439").forEach { assertEquals(it, openFit, icons(it)) }
    }

    @Test
    fun proModelsKeepTheProIcons() {
        val pro = listOf(
            R.drawable.airpods_pro_left_notification, R.drawable.airpods_pro_right_notification,
            R.drawable.airpods_pro_case_notification, R.drawable.ic_status_airpods_pro
        )
        listOf("A2084", "A2931", "A3047", "A3063").forEach { assertEquals(it, pro, icons(it)) }
    }

    @Test
    fun airPodsMaxAreHeadphones() {
        listOf("A2096", "A3184").forEach {
            assertTrue(model(it).isHeadphones)
            assertEquals(R.drawable.ic_status_airpods_max, model(it).statusIconRes)
        }
        assertEquals(false, model("A3055").isHeadphones)
    }
}
