package aoc

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LineTest {
    @Test
    fun singleElementLine1() {
        testPos(
            Pos(0, 7),
        )
    }

    @Test
    fun singleElementLine2() {
        testXyz(
            Xyz(0, 7, -2),
        )
    }

    @Test
    fun oneDimensionLine1() {
        testPos(
            Pos(0, 1),
            Pos(0, 2),
            Pos(0, 3),
            Pos(0, 4),
            Pos(0, 5),
        )
    }

    @Test
    fun oneDimensionLine2() {
        testPos(
            Pos(-1, 0),
            Pos(-2, 0),
            Pos(-3, 0),
            Pos(-4, 0),
            Pos(-5, 0),
        )
    }

    @Test
    fun oneDimensionLine3() {
        testXyz(
            Xyz(0, 0, 0),
            Xyz(0, 0, 1),
            Xyz(0, 0, 2),
        )
    }

    @Test
    fun oneDimensionLine4() {
        testXyz(
            Xyz(0, 0, 0),
            Xyz(0, -1, 0),
            Xyz(0, -2, 0),
        )
    }

    @Test
    fun oneDimensionLine5() {
        testXyz(
            Xyz(10, 0, 0),
            Xyz(9, 0, 0),
        )
    }

    @Test
    fun sameIncrementLine1() {
        testPos(
            Pos(0, 0),
            Pos(1, 1),
            Pos(2, 2),
        )
    }

    @Test
    fun sameIncrementLine2() {
        testXyz(
            Xyz(5, 25, 11),
            Xyz(4, 24, 12),
            Xyz(3, 23, 13),
            Xyz(2, 22, 14),
            Xyz(1, 21, 15),
        )
    }

    @Test
    fun generalLine1() {
        testPos(
            Pos(1, 1),
            Pos(1, 2),
            Pos(1, 3),
            Pos(2, 4),
            Pos(2, 5),
            Pos(2, 6),
            Pos(3, 7),
            Pos(3, 8),
        )
    }

    @Test
    fun generalLine2() {
        testXyz(
            Xyz(5, 25, 11),
            Xyz(5, 25, 12),
            Xyz(4, 25, 13),
            Xyz(4, 25, 14),
            Xyz(3, 24, 15),
            Xyz(3, 24, 16),
            Xyz(2, 24, 17),
            Xyz(2, 23, 18),
            Xyz(1, 23, 19),
            Xyz(1, 23, 20),
        )
    }

    @Test
    fun generalLine3() {
        testXyz(
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
        )
    }

    private fun testPos(vararg expectedIteration: Pos) {
        val first = expectedIteration.first()
        val last = expectedIteration.last()

        val line = first..last
        assertEquals(first, line.first)
        assertEquals(last, line.last)
        assertEquals(expectedIteration.size, line.size.toInt())
        assertEquals(expectedIteration.toList(), line.toList())

        for (e in listOf(first, last).toPosBox().widen(1).xThenY()) {
            assertEquals(e in expectedIteration, e in line, e.toString())
        }
    }

    private fun testXyz(vararg expectedIteration: Xyz) {
        val first = expectedIteration.first()
        val last = expectedIteration.last()

        val line = first..last
        assertEquals(first, line.first)
        assertEquals(last, line.last)
        assertEquals(expectedIteration.size, line.size.toInt())
        assertEquals(expectedIteration.toList(), line.toList())

        for (e in listOf(first, last).toXyzBox().widen(1).xThenYThenZ()) {
            assertEquals(e in expectedIteration, e in line, e.toString())
        }
    }

}
