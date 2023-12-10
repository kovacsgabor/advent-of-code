package aoc

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import kotlin.test.*

class ParseTest {

    enum class OnOff {
        ON
    }

    @Test
    fun testParseError() {
        assertThrows<IllegalArgumentException> {
            "hello".parse {}
        }

        assertThrows<IllegalArgumentException> {
            "hello".parse {
                "abc" {}
                "edf" {}
            }
        }
    }

    @Test
    fun testAny() {
        val input =
            listOf("-17", "0", "123", "+82", "apple", "abc_123", "0.72", "0,4", "abc edf", "abc, edf", "abc-edf")
        val parsed = mutableListOf<String>()
        input.parseEach {
            anyText { (s: String) -> parsed += s }
        }

        assertEquals(input, parsed)
    }

    @Test
    fun testInt() {
        listOf("-17", "0", "123", "+82").parseEach {
            int {}
        }

        var matched = false
        listOf("apple", "abc_123", "0.72", "0,4", "abc edf", "abc, edf", "abc-edf").parseEach {
            int { matched = true }
            anyText {}
        }
        assertFalse(matched)
    }

    @Test
    fun testLong() {
        listOf("-17", "0", "123", "+82").parseEach {
            long {}
        }

        var matched = false
        listOf("apple", "abc_123", "0.72", "0,4", "abc edf", "abc, edf", "abc-edf").parseEach {
            long { matched = true }
            anyText {}
        }
        assertFalse(matched)
    }

    @Test
    fun testWord() {
        listOf("-17", "0", "123", "+82", "apple", "abc_123", "0.72", "abc-edf").parseEach {
            word {}
        }

        var matched = false
        listOf("0,4", "abc edf", "abc, edf").parseEach {
            word { matched = true }
            anyText {}
        }
        assertFalse(matched)
    }

    @Test
    fun testWordNoHyphen() {
        listOf("0", "123", "+82", "apple", "abc_123", "0.72").parseEach {
            wordNoHyphen {}
        }

        var matched = false
        listOf("-17", "0,4", "abc edf", "abc, edf", "abc-edf").parseEach {
            wordNoHyphen { matched = true }
            anyText {}
        }
        assertFalse(matched)
    }

    @Test
    fun testCombined() {
        var called = false
        val (onOff: OnOff, x1: Int, x2: Long, y1: Int, y2: Long, z1: Int, z2: Long) = "on x=11..13,y=8..-6,z=-8..-10".parse {
            "(on|off) $int" { (_: String) -> fail() }
            "(on|off) x=$int..$int,y=$int..$int,z=$int..$int" { (s: String) ->
                assertEquals("on", s)
                called = true
            }
            ".*" { fail() }
        }

        assertEquals(OnOff.ON, onOff)
        assertEquals(11, x1)
        assertEquals(13, x2)
        assertEquals(8, y1)
        assertEquals(-6, y2)
        assertEquals(-8, z1)
        assertEquals(-10, z2)
        assertTrue(called)
    }


    @Test
    fun testSingleMatchers() {
        assertThrows<IllegalArgumentException> {
            "hello".parse("abc")
        }

        assertNull("hello".parseOrNull("abc"))

        val (i : Int) = "123".parse(int)
        assertEquals(123, i)

        val (j : Int?) = "123".parseOrNull(int)
        assertEquals(123, j)

        val (k : Int?) = "hello".parseOrNull(int)
        assertNull(k)
    }

}
