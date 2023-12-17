package aoc

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LineTest {
    @Test
    fun singleElementLine1() {
        val line = Pos(0, 7)..Pos(0, 7)

        assertEquals(Pos(0, 7), line.first)
        assertEquals(Pos(0, 7), line.last)
        assertEquals(1, line.size)
        assertEquals(
            listOf(
                Pos(0, 7),
            ), line.toList()
        )
    }

    @Test
    fun singleElementLine2() {
        val line = Xyz(0, 7, -2)..Xyz(0, 7, -2)

        assertEquals(Xyz(0, 7, -2), line.first)
        assertEquals(Xyz(0, 7, -2), line.last)
        assertEquals(1, line.size)
        assertEquals(
            listOf(
                Xyz(0, 7, -2),
            ), line.toList()
        )
    }

    @Test
    fun oneDimensionLine1() {
        val line = Pos(0, 1)..Pos(0, 5)

        assertEquals(Pos(0, 1), line.first)
        assertEquals(Pos(0, 5), line.last)
        assertEquals(5, line.size)
        assertEquals(
            listOf(
                Pos(0, 1),
                Pos(0, 2),
                Pos(0, 3),
                Pos(0, 4),
                Pos(0, 5),
            ), line.toList()
        )
    }

    @Test
    fun oneDimensionLine2() {
        val line = Pos(-1, 0)..Pos(-5, 0)

        assertEquals(Pos(-1, 0), line.first)
        assertEquals(Pos(-5, 0), line.last)
        assertEquals(5, line.size)
        assertEquals(
            listOf(
                Pos(-1, 0),
                Pos(-2, 0),
                Pos(-3, 0),
                Pos(-4, 0),
                Pos(-5, 0),
            ), line.toList()
        )
    }

    @Test
    fun oneDimensionLine3() {
        val line = Xyz(0, 0, 0)..Xyz(0, 0, 2)

        assertEquals(Xyz(0, 0, 0), line.first)
        assertEquals(Xyz(0, 0, 2), line.last)
        assertEquals(3, line.size)
        assertEquals(
            listOf(
                Xyz(0, 0, 0),
                Xyz(0, 0, 1),
                Xyz(0, 0, 2),
            ), line.toList()
        )
    }

    @Test
    fun oneDimensionLine4() {
        val line = Xyz(0, 0, 0)..Xyz(0, -2, 0)

        assertEquals(Xyz(0, 0, 0), line.first)
        assertEquals(Xyz(0, -2, 0), line.last)
        assertEquals(3, line.size)
        assertEquals(
            listOf(
                Xyz(0, 0, 0),
                Xyz(0, -1, 0),
                Xyz(0, -2, 0),
            ), line.toList()
        )
    }

    @Test
    fun oneDimensionLine5() {
        val line = Xyz(10, 0, 0)..Xyz(9, 0, 0)

        assertEquals(Xyz(10, 0, 0), line.first)
        assertEquals(Xyz(9, 0, 0), line.last)
        assertEquals(2, line.size)
        assertEquals(
            listOf(
                Xyz(10, 0, 0),
                Xyz(9, 0, 0),
            ), line.toList()
        )
    }

    @Test
    fun sameIncrementLine1() {
        val line = Pos(0, 0)..Pos(2, 2)

        assertEquals(Pos(0, 0), line.first)
        assertEquals(Pos(2, 2), line.last)
        assertEquals(3, line.size)
        assertEquals(
            listOf(
                Pos(0, 0),
                Pos(1, 1),
                Pos(2, 2),
            ), line.toList()
        )
    }

    @Test
    fun sameIncrementLine2() {
        val line = Xyz(5, 25, 11)..Xyz(1, 21, 15)

        assertEquals(Xyz(5, 25, 11), line.first)
        assertEquals(Xyz(1, 21, 15), line.last)
        assertEquals(5, line.size)
        assertEquals(
            listOf(
                Xyz(5, 25, 11),
                Xyz(4, 24, 12),
                Xyz(3, 23, 13),
                Xyz(2, 22, 14),
                Xyz(1, 21, 15),
            ), line.toList()
        )
    }

    @Test
    fun generalLine1() {
        val line = Pos(1, 1)..Pos(3, 8)

        assertEquals(Pos(1, 1), line.first)
        assertEquals(Pos(3, 8), line.last)
        assertEquals(8, line.size)
        assertEquals(
            listOf(
                Pos(1, 1),
                Pos(1, 2),
                Pos(1, 3),
                Pos(2, 4),
                Pos(2, 5),
                Pos(2, 6),
                Pos(3, 7),
                Pos(3, 8),
            ), line.toList()
        )
    }

    @Test
    fun generalLine2() {
        val line = Xyz(5, 25, 11)..Xyz(1, 23, 20)

        assertEquals(Xyz(5, 25, 11), line.first)
        assertEquals(Xyz(1, 23, 20), line.last)
        assertEquals(10, line.size)
        assertEquals(
            listOf(
                Xyz(5, 25, 11),
                Xyz(5, 25, 12),
                Xyz(4, 25, 13),
                Xyz(4, 25, 14),
                Xyz(3, 24, 15),
                Xyz(3, 24, 16),
                Xyz(2, 24, 17),
                Xyz(2, 24, 18),
                Xyz(1, 23, 19),
                Xyz(1, 23, 20),
            ), line.toList()
        )
    }

    @Test
    fun generalLine3() {
        val line = Xyz(5, 21, 11)..Xyz(1, 25, 20)

        assertEquals(Xyz(5, 21, 11), line.first)
        assertEquals(Xyz(1, 25, 20), line.last)
        assertEquals(10, line.size)
        assertEquals(
            listOf(
                Xyz(5, 21, 11),
                Xyz(5, 21, 12),
                Xyz(4, 22, 13),
                Xyz(4, 22, 14),
                Xyz(3, 23, 15),
                Xyz(3, 23, 16),
                Xyz(2, 24, 17),
                Xyz(2, 24, 18),
                Xyz(1, 25, 19),
                Xyz(1, 25, 20),
            ), line.toList()
        )
    }

}
