package com.arsh.pods.data

import com.arsh.pods.bluetooth.AACPManager

enum class CustomEqBand { LOW, MID, HIGH }

data class CustomEq(val state: Int, val low: Int, val mid: Int, val high: Int) {

    fun isEnabled(): Boolean {
        return state == 2
    }

    fun withBands(low: Int, mid: Int, high: Int): CustomEq =
        copy(low = low.coerceIn(0, 100), mid = mid.coerceIn(0, 100), high = high.coerceIn(0, 100))

    fun withEnabled(enabled: Boolean): CustomEq = copy(state = if (enabled) 2 else 1)

    fun toPacket(): ByteArray {
        return byteArrayOf(
            AACPManager.Companion.Opcodes.CUSTOM_EQ, 0x00,
            0x05, 0x00, // length (LE)
            0x01, state.toByte(),
            low.toByte(), mid.toByte(), high.toByte()
        )
    }

    init {
        require(low in 0..100) { "low must be between 0 and 100, was $low" }
        require(mid in 0..100) { "mid must be between 0 and 100, was $mid" }
        require(high in 0..100) { "high must be between 0 and 100, was $high" }
    }
}
