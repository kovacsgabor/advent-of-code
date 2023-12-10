package aoc

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SplitTest {
    @Test
    fun splitWhen() {
        assertEquals(
            listOf(
                listOf("a", "b", "c"),
                listOf("d"),
                listOf("e", "f")
            ),
            listOf("", "", "a", "b", "c", "", "d", "", "", "e", "f", "").splitWhen { it.isEmpty() }
        )
    }
}

