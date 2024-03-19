package cn.enaium

import cn.enaium.utility.toUUID
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class UtilityTest {
    @Test
    fun toUUID() {
        assertEquals(
            "00000000-0000-0000-0000-000000000000".toUUID(),
            UUID.fromString("00000000-0000-0000-0000-000000000000")
        )
    }
}