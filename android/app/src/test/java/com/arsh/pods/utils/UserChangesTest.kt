package com.arsh.pods.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

class UserChangesTest {
    private val quiet = 20.milliseconds

    @Test
    fun theValueAControlOpensWithIsNotAChange() = runBlocking {
        val shown = flow { emit(75f) }

        assertEquals(emptyList<Float>(), shown.userChanges(quiet).toList())
    }

    @Test
    fun laterChangesAreReportedOnceTheySettle() = runBlocking {
        val slider = flow {
            emit(75f)
            delay(100)
            emit(60f)
            emit(50f)
        }

        assertEquals(listOf(50f), slider.userChanges(quiet).toList())
    }

    @Test
    fun aChangeMadeRightAfterOpeningIsStillReported() = runBlocking {
        val slider = flow {
            emit(75f)
            emit(40f)
        }

        assertEquals(listOf(40f), slider.userChanges(quiet).toList())
    }
}
