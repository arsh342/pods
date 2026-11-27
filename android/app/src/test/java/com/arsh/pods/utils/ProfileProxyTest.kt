package com.arsh.pods.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class ProfileProxyTest {
    @Test
    fun closesAfterSuccessfulUse() {
        val events = mutableListOf<String>()

        useProfileProxy(close = { events.add("close") }) { events.add("use") }

        assertEquals(listOf("use", "close"), events)
    }

    @Test
    fun closesAndReportsErrorWhenUseThrows() {
        var closed = 0
        var reported: Exception? = null
        val failure = SecurityException("connect not allowed")

        useProfileProxy(close = { closed++ }, onError = { reported = it }) { throw failure }

        assertEquals(1, closed)
        assertEquals(failure, reported)
    }
}
