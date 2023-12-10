package aoc

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class XyzTest {

    @Test
    fun times() {
        assertEquals(-9, Xyz(1, 5, 2) * Xyz(2, -1, -3))
        assertEquals(Xyz(3, 15, 6), Xyz(1, 5, 2) * 3)
    }

    @Test
    fun sumOfCoordinates() {
        assertEquals(6L, Xyz(1, -2, 7).sumOfCoordinates())
    }

    @Test
    fun absSumOfCoordinates() {
        assertEquals(8, Xyz(-5, 1, -2).absSumOfCoordinates())
    }

    @Test
    fun map() {
        assertEquals(Xyz(2, 14, 10), Xyz(1, 7, 5).map { it * 2 })
    }

    @Test
    fun combine() {
        assertEquals(Xyz(15, 77, 26), Xyz(3, 7, 13).mapWith(Xyz(5, 11, 2)) { a, b -> a * b })
    }

    @Test
    fun neighbors6() {
        assertEquals(
            listOf(
                Xyz(19, 30, 40), Xyz(21, 30, 40),
                Xyz(20, 29, 40), Xyz(20, 31, 40),
                Xyz(20, 30, 39), Xyz(20, 30, 41),
            ), Xyz(20, 30, 40).neighbors6()
        )
    }

    @Test
    fun neighbors26() {
        assertEquals(
            listOf(
                Xyz(19, 30, 40), Xyz(21, 30, 40), // +-x
                Xyz(20, 29, 40), Xyz(20, 31, 40), // +-y
                Xyz(20, 30, 39), Xyz(20, 30, 41), // +-z

                Xyz(19, 29, 40), Xyz(19, 31, 40), Xyz(19, 30, 39), Xyz(19, 30, 41), // -x, +-y || +-z
                Xyz(21, 29, 40), Xyz(21, 31, 40), Xyz(21, 30, 39), Xyz(21, 30, 41), // +x, +-y || +-z
                Xyz(20, 29, 39), Xyz(20, 29, 41), Xyz(20, 31, 39), Xyz(20, 31, 41), // +-y, +-z

                Xyz(19, 29, 39), Xyz(19, 29, 41), Xyz(19, 31, 39), Xyz(19, 31, 41), // -x, +-y, +-z
                Xyz(21, 29, 39), Xyz(21, 29, 41), Xyz(21, 31, 39), Xyz(21, 31, 41), // +x, +-y, +-z
            ), Xyz(20, 30, 40).neighbors26()
        )
    }

    @Test
    fun plus() {
        assertEquals(Xyz(3, 4, -7), Xyz(1, 5, -4) + Xyz(2, -1, -3))
    }

    @Test
    fun components() {
        val (x, y, z) = Xyz(1, 2, -7)
        assertEquals(1, x)
        assertEquals(2, y)
        assertEquals(-7, z)
    }


    @Test
    fun crossProduct() {
        assertEquals(Xyz(0, 0, 1), Xyz(1, 0, 0) crossProduct Xyz(0, 1, 0))
    }

    @Test
    fun testEquals() {
        assertEquals(Xyz(1, 2, 3), Xyz(1, 2, 3))
        assertNotEquals(Xyz(2, 2, 3), Xyz(1, 2, 3))
        assertNotEquals(Xyz(1, 2, 3), Xyz(1, 1, 3))
        assertNotEquals(Xyz(1, 2, 3), Xyz(1, 2, 4))
    }

    @Test
    fun testHashCode() {
        assertEquals(Xyz(1, 2, 3).hashCode(), Xyz(1, 2, 3).hashCode())
    }

    @Test
    fun testToString() {
        assertEquals("Xyz(1,2,4)", Xyz(1, 2, 4).toString())
    }

}