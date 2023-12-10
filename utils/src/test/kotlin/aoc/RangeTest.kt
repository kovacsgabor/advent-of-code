package aoc

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RangeTest {
    @Test
    fun intersection() {
        assertEquals(3..4, 3..6 intersectRange 2..4)
        assertEquals(3..4, 3..4 intersectRange 2..8)

        @Suppress("EmptyRange")
        assertEquals(3..-2, 3..4 intersectRange 2..-2)
    }

    @Test
    fun minus() {
        // No intersection
        assertEquals(listOf(3..6), (3..6) - (0..2))
        assertEquals(listOf(3..6), (3..6) - (7..10))
        assertEquals(listOf(3..6), (3..6) - (0..1))
        assertEquals(listOf(3..6), (3..6) - (8..10))

        // Has intersection
        assertEquals(listOf(4..6), (3..6) - (0..3))
        assertEquals(listOf(3..5), (3..6) - (6..10))
        assertEquals(listOf(5..6), (3..6) - (0..4))
        assertEquals(listOf(3..4), (3..6) - (5..10))

        // Contained range
        assertEquals(listOf(3..3, 6..6), (3..6) - (4..5))

        // Containing range
        assertEquals(listOf(), (3..6) - (0..10))

        // Equal range
        assertEquals(listOf(), (3..6) - (3..6))

        // Empty ranges
        @Suppress("EmptyRange")
        assertEquals(listOf(3..6), (3..6) - (4..-3))
        @Suppress("EmptyRange")
        assertEquals(listOf(), (3..-5) - (4..5))
        @Suppress("EmptyRange")
        assertEquals(listOf(), (3..-5) - (4..-3))
    }

    @Test
    fun contains() {
        assertTrue(3..5 in 3..6)
        assertTrue(3..6 in 3..6)
        assertTrue(4..6 in 3..6)
        assertTrue(4..5 in 3..6)

        assertFalse(2..5 in 3..6)
        assertFalse(3..7 in 3..6)
        assertFalse(2..8 in 3..6)
        assertFalse(9..10 in 3..6)
    }
}