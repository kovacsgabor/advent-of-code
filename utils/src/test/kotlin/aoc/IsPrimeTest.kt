package aoc

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Timeout
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IsPrimeTest {

    @BeforeEach
    fun clear() {
        clearKnownPrimeLists()
    }

    @Test
    fun testSpecialCases() {
        assertFalse(isPrime(0))
        assertFalse(isPrime(1))
        assertTrue(isPrime(2))
    }

    @Test
    fun testEvenInBulk() {
        (4..<10_000L).step(2).forEach { even ->
            assertFalse(isPrime(even), "$even")
        }
    }

    @Test
    fun testOddInBulk() {
        (3..<10_000L).step(2).forEach { odd ->
            val isPrimeNaive = (3..(sqrt(odd.toDouble()).toLong() + 1)).step(2).none { odd % it == 0L }
            assertEquals(isPrimeNaive, isPrime(odd), "$odd")
        }
    }

    @Test
    @Timeout(1)
    fun testBigPrime() {
        repeat(10) {
            assertTrue(isPrime(735_434_647_980_607))
        }
    }

}
