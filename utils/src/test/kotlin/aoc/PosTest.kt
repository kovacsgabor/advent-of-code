package aoc

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class PosTest {
    @Test
    fun times() {
        assertEquals(-3, Pos(1, 5) * Pos(2, -1))
        assertEquals(Pos(3, 15), Pos(1, 5) * 3)
    }

    @Test
    fun sumOfCoordinates() {
        assertEquals(-1L, Pos(1, -2).sumOfCoordinates())
    }

    @Test
    fun absSumOfCoordinates() {
        assertEquals(3, Pos(1, -2).absSumOfCoordinates())
    }

    @Test
    fun map() {
        assertEquals(Pos(2, 14), Pos(1, 7).map { it * 2 })
    }

    @Test
    fun combine() {
        assertEquals(Pos(15, 77), Pos(3, 7).mapWith(Pos(5, 11)) { a, b -> a * b })
    }

    @Test
    fun div() {
        assertEquals(1, Pos(2, 2) / Pos(2, 2))
        assertEquals(2, Pos(2, 2) / Pos(1, 1))
        assertEquals(-2, Pos(-2, -2) / Pos(1, 1))
        assertEquals(-2, Pos(4, -2) / Pos(-2, 1))
        assertEquals(null, Pos(4, -2) / Pos(2, 1))
        assertEquals(null, Pos(5, -2) / Pos(2, 0))
        assertEquals(2, Pos(10, 0) / Pos(5, 0))
        assertEquals(10, Pos(100, 0) / Pos(10, 0))
    }

    @Test
    fun neighbors4() {
        assertEquals(
            listOf(
                Pos(1, -3), Pos(0, -2), Pos(1, -1), Pos(2, -2)
            ),
            Pos(1, -2).neighbors4()
        )
    }

    @Test
    fun neighbors8() {
        assertEquals(
            listOf(
                Pos(1, -3), Pos(0, -2), Pos(1, -1), Pos(2, -2),
                Pos(0, -3), Pos(2, -3), Pos(0, -1), Pos(2, -1),
            ),
            Pos(1, -2).neighbors8()
        )
    }

    @Test
    fun plus() {
        assertEquals(Pos(3, 4), Pos(1, 5) + Pos(2, -1))
    }

    @Test
    fun components() {
        val (x, y) = Pos(1, 2)
        assertEquals(1, x)
        assertEquals(2, y)
    }

    @Test
    fun testEquals() {
        assertEquals(Pos(1, 2), Pos(1, 2))
        assertNotEquals(Pos(2, 2), Pos(1, 2))
        assertNotEquals(Pos(1, 2), Pos(1, 1))
    }

    @Test
    fun testHashCode() {
        assertEquals(Pos(1, 2).hashCode(), Pos(1, 2).hashCode())
    }

    @Test
    fun testToString() {
        assertEquals("Pos(1,2)", Pos(1, 2).toString())
    }

}

