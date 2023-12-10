package aoc

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CachedIterableTest {
    @Test
    fun testIterator() {
        var phase = 0
        val seq = sequence {
            phase += 1
            yield(1)
            phase += 1
            yield(2)
            phase += 1
        }

        // Creation should not advance the sequence
        val cs = CachedIterable(seq)
        assertEquals(0, phase)

        // Access should advance the sequence only as necessary
        assertEquals(1, cs.first { it == 1 })
        assertEquals(1, phase)

        // Repeated access should not advance the sequence
        assertEquals(1, cs.first { it == 1 })
        assertEquals(1, phase)

        // Access should advance the sequence only as necessary
        assertEquals(2, cs.first { it == 2 })
        assertEquals(2, phase)

        // We reach the end of the sequence
        assertEquals(null, cs.firstOrNull { it == 3 })
        assertEquals(3, phase)
    }

    @Test
    fun testHasSizeAtLeast() {
        var phase = 0
        val seq = sequence {
            phase += 1
            yield(1)
            phase += 1
            yield(2)
            phase += 1
        }

        // Creation should not advance the sequence
        val cs = CachedIterable(seq)
        assertEquals(0, phase)

        // Querying 0 size should not advance the sequence
        assertTrue(cs.hasSizeAtLeast(0))
        assertEquals(0, phase)

        // Access should advance the sequence only as necessary
        assertTrue(cs.hasSizeAtLeast(1))
        assertEquals(1, phase)

        // Repeated access should not advance the sequence
        assertTrue(cs.hasSizeAtLeast(1))
        assertEquals(1, phase)

        // Access should advance the sequence only as necessary
        assertTrue(cs.hasSizeAtLeast(2))
        assertEquals(2, phase)

        // We reach the end of the sequence
        assertFalse(cs.hasSizeAtLeast(3))
        assertEquals(3, phase)

        // Asking for a bigger size is OK
        assertFalse(cs.hasSizeAtLeast(10))
        assertEquals(3, phase)
    }

    @Test
    fun testGet() {
        var phase = 0
        val seq = sequence {
            phase += 1
            yield(1)
            phase += 1
            yield(2)
            phase += 1
        }

        // Creation should not advance the sequence
        val cs = CachedIterable(seq)
        assertEquals(0, phase)

        // Access should advance the sequence only as necessary
        assertEquals(1, cs[0])
        assertEquals(1, phase)

        // Repeated access should not advance the sequence
        assertEquals(1, cs[0])
        assertEquals(1, phase)

        // Access should advance the sequence only as necessary
        assertEquals(2, cs[1])
        assertEquals(2, phase)

        // We reach the end of the sequence
        assertThrows<NoSuchElementException> { cs[2] }
        assertEquals(3, phase)
    }
}
