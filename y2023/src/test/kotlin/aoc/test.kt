package aoc

import aoc.test.test
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class Y2023Test {

    @TestFactory
    fun test() = listOf(
        test(Y23Day1, 54667, 54203),
        test(Y23Day2, 2237, 66681),
        test(Y23Day3, 535078, 75312571),
        test(Y23Day4, 20407, 23806951),
        test(Y23Day5, 388071289, 84206669),
        test(Y23Day6, 281600, 33875953),
        test(Y23Day7, 248113761, 246285222),
        test(Y23Day8, 15989, 13830919117339),
        test(Y23Day9, 1762065988, 1066),
        test(Y23Day10, 6882, 491),
        test(Y23Day11, 10494813, 840988812853),
    )

    private fun test(d: Day, part1: Any, part2: Any): DynamicTest =
        DynamicTest.dynamicTest(d.toString()) { d.test(part1, part2) }

}
