package aoc.test

import aoc.Day
import kotlin.reflect.jvm.kotlinFunction

/** Runs the given day and compares its results to the given objects. Compares results using [Any.toString]. */
fun Day.test(expectedPart1: Any, expectedPart2: Any) {
    javaClass
        .getDeclaredMethod("main", Array<String>::class.java)
        .kotlinFunction!!
        .call(this, emptyArray<String>())

    if (expectedPart1.toString() != part1.toString()) {
        throw AssertionError(
            "Part 1 of $this is wrong.\n" +
                    "Expected: $expectedPart1\n" +
                    "Actual:   $part1"
        )
    }
    if (expectedPart2.toString() != part2.toString()) {
        throw AssertionError(
            "Part 2 of $this is wrong.\n" +
                    "Expected: $expectedPart2\n" +
                    "Actual:   $part2"
        )
    }
}
