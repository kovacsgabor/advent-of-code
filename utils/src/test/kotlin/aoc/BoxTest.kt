package aoc

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class BoxTest {

    @Test
    fun createPosBox() {
        assertEquals(
            PosBox(-5L..7, 2L..10),
            listOf(
                Pos(-5, 10), Pos(7, 2)
            ).toPosBox()
        )
    }

    @Test
    fun createXyzBox() {
        assertEquals(
            XyzBox(-5L..7, 2L..10, 6L..6),
            listOf(
                Xyz(-5, 10, 6), Xyz(7, 2, 6)
            ).toXyzBox()
        )
    }

    @Test
    fun posBoxContains() {
        assertTrue(Pos(-5, 2) in PosBox(-5L..7, 2L..10))
        assertTrue(Pos(-5, 6) in PosBox(-5L..7, 2L..10))
        assertTrue(Pos(-10, 2) !in PosBox(-5L..7, 2L..10))
        assertTrue(Pos(-5, 20) !in PosBox(-5L..7, 2L..10))
    }

    @Test
    fun xyzBoxContains() {
        assertTrue(Xyz(-5, 2, 6) in XyzBox(-5L..7, 2L..10, 6L..6))
        assertTrue(Xyz(-5, 6, 6) in XyzBox(-5L..7, 2L..10, 6L..6))
        assertTrue(Xyz(-10, 2, 6) !in XyzBox(-5L..7, 2L..10, 6L..6))
        assertTrue(Xyz(-5, 20, 6) !in XyzBox(-5L..7, 2L..10, 6L..6))
        assertTrue(Xyz(-5, 2, 7) !in XyzBox(-5L..7, 2L..10, 6L..6))
    }

    @Test
    @Timeout(1)
    fun posBoxIterate() {
        assertEquals(
            listOf(
                Pos(0, 0),
                Pos(1, 0),
                Pos(2, 0),
                Pos(0, 1),
                Pos(1, 1),
                Pos(2, 1),
            ),
            PosBox(0..2L, 0..1L).xThenY().toList()
        )
    }

    @Test
    @Timeout(1)
    fun xyzBoxIterate() {
        assertEquals(
            listOf(
                Xyz(0, 0, 0),
                Xyz(1, 0, 0),
                Xyz(2, 0, 0),
                Xyz(3, 0, 0),

                Xyz(0, 1, 0),
                Xyz(1, 1, 0),
                Xyz(2, 1, 0),
                Xyz(3, 1, 0),

                Xyz(0, 2, 0),
                Xyz(1, 2, 0),
                Xyz(2, 2, 0),
                Xyz(3, 2, 0),

                Xyz(0, 0, 1),
                Xyz(1, 0, 1),
                Xyz(2, 0, 1),
                Xyz(3, 0, 1),

                Xyz(0, 1, 1),
                Xyz(1, 1, 1),
                Xyz(2, 1, 1),
                Xyz(3, 1, 1),

                Xyz(0, 2, 1),
                Xyz(1, 2, 1),
                Xyz(2, 2, 1),
                Xyz(3, 2, 1),
            ),
            XyzBox(0L..3, 0L..2, 0L..1).xThenYThenZ().toList()
        )
    }
}