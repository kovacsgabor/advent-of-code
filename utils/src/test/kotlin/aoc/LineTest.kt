package aoc

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LineTest {
    @Test
    fun singleElementLine1() {
        val line = Pos(0, 7)..Pos(0, 7)

        assertEquals(listOf(), line.dirs)
        assertEquals(listOf(), line.incrementEvery)
        assertEquals(
            listOf(
                Pos(0, 7),
            ), line.toList()
        )
    }

    @Test
    fun singleElementLine2() {
        val line = Xyz(0, 7, -2)..Xyz(0, 7, -2)

        assertEquals(listOf(), line.dirs)
        assertEquals(listOf(), line.incrementEvery)
        assertEquals(
            listOf(
                Xyz(0, 7, -2),
            ), line.toList()
        )
    }

    @Test
    fun oneDimensionLine1() {
        val line = Pos(0, 1)..Pos(0, 5)

        assertEquals(Pos(0, 1), line.dir)
        assertEquals(listOf(1L), line.incrementEvery)
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
    fun oneDimensionLine3() {
        val line = Xyz(0, 0, 0)..Xyz(0, 0, 2)

        assertEquals(Xyz(0, 0, 1), line.dir)
        assertEquals(listOf(1L), line.incrementEvery)
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

        assertEquals(Xyz(0, -1, 0), line.dir)
        assertEquals(listOf(1L), line.incrementEvery)
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

        assertEquals(Xyz(-1, 0, 0), line.dir)
        assertEquals(listOf(1L), line.incrementEvery)
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

        assertEquals(listOf(Pos(1, 1)), line.dirs)
        assertEquals(listOf(1L), line.incrementEvery)
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

        assertEquals(listOf(Xyz(-1, -1, 1)), line.dirs)
        assertEquals(listOf(1L), line.incrementEvery)
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

        assertEquals(listOf(Pos(1, 0), Pos(0, 1)), line.dirs)
        assertEquals(listOf(3L, 1L), line.incrementEvery)
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

        assertEquals(listOf(Xyz(-1, 0, 0), Xyz(0, -1, 0), Xyz(0, 0, 1)), line.dirs)
        assertEquals(listOf(2L, 4L, 1L), line.incrementEvery)
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

        assertEquals(listOf(Xyz(-1, 1, 0), Xyz(0, 0, 1)), line.dirs)
        assertEquals(listOf(2L, 1L), line.incrementEvery)
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