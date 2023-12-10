package aoc

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DirTest {

    @Test
    fun dir4Values() {
        assertEquals(4, Dir4.values.size)
        assertEquals(4, Dir4.values.distinct().size)

        // Ensure that no element is null. It could happen due to initialization error
        assertEquals(4, Dir4.values.map { it.toString() }.size)
    }

    @Test
    fun dir8Values() {
        assertEquals(8, Dir8.values.size)
        assertEquals(8, Dir8.values.distinct().size)

        // Ensure that no element is null. It could happen due to initialization error
        assertEquals(8, Dir8.values.map { it.toString() }.size)
    }

    @Test
    fun dir6Values() {
        assertEquals(6, Dir6.values.size)
        assertEquals(6, Dir6.values.distinct().size)

        // Ensure that no element is null. It could happen due to initialization error
        assertEquals(6, Dir6.values.map { it.toString() }.size)
    }

    @Test
    fun dir26Values() {
        assertEquals(26, Dir26.values.size)
        assertEquals(26, Dir26.values.distinct().size)

        // Ensure that no element is null. It could happen due to initialization error
        assertEquals(26, Dir26.values.map { it.toString() }.size)
    }

}