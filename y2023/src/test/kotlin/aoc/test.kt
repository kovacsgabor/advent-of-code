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
        test(Y23Day12, 7939, 850504257483930),
        test(Y23Day13, 41859, 30842),
        test(Y23Day14, 105784, 91286),
        test(Y23Day15, 513214, 258826),
        test(Y23Day16, 7608, 8221),
        test(Y23Day17, 861, 1037),
        test(Y23Day18, 67891, 94116351948493),
        test(Y23Day19, 377025, 135506683246673),
        test(Y23Day20, 737679780, 227411378431763),
        test(Y23Day21, 3709, 617361073602319),
        test(Y23Day22, 497, 67468),
        test(Y23Day23, 2298, 6602),
        test(Y23Day24, 16812, 880547248556435),
        test(Y23Day25, 554064, null),
    )

    private fun test(d: Day, part1: Any, part2: Any?): DynamicTest =
        DynamicTest.dynamicTest(d.toString()) { d.test(part1, part2) }

}
