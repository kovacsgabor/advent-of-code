package aoc

object Y23Day3 : Day() {

    @JvmStatic
    fun main(args: Array<String>) {
        val digits = umapOf<Pos, Int>()
        val symbols = mutableSetOf<Pos>()
        val stars = mutableSetOf<Pos>()

        lines
            .splitByPositions()
            .forEach { (pos, value) ->
                if (value != ".") {
                    if (Character.isDigit(value[0])) {
                        digits[pos] = value.toInt()
                    } else {
                        symbols += pos
                        if (value == "*") stars += pos
                    }
                }
            }

        val maxDigitPos = digits.maxPos()

        var sumOfPartNumbers = 0
        var sumOfStarNumbers = 0
        val firstNumberForEachStar = umapOf<Pos, Int>()

        // Go through each row and parse consecutive digits as numbers
        // In the meantime, also check for stars around them
        for (y in 0..maxDigitPos.y) {
            var number = 0
            var partNumber = false
            val starsAround = mutableSetOf<Pos>()
            for (x in 0..maxDigitPos.x + 1) {
                val pos = Pos(x, y)
                if (pos in digits) {
                    number *= 10
                    number += digits[Pos(x, y)]

                    val neighbors = Dir8.values.map { it + pos }

                    // Part 1: mark it as a part number if a symbol are around
                    partNumber = partNumber || neighbors.any { it in symbols }

                    // Part 2: collect neighboring stars
                    starsAround += neighbors.filter { it in stars }
                } else {
                    // Part 1
                    if (partNumber) {
                        sumOfPartNumbers += number
                    }

                    // Part 2
                    for (star in starsAround) {
                        if (star in firstNumberForEachStar) {
                            sumOfStarNumbers += number * firstNumberForEachStar[star]
                        } else {
                            firstNumberForEachStar[star] = number
                        }
                    }

                    // Clear variables
                    number = 0
                    partNumber = false
                    starsAround.clear()
                }
            }
        }

        part1(sumOfPartNumbers)
        part2(sumOfStarNumbers)
    }

}
