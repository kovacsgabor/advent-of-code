package aoc

object Y23Day1 : Day() {

    @JvmStatic
    fun main(args: Array<String>) {
        fun valueOf(s: String): Int {
            val digits = s.toChars()
                .filter { Character.isDigit(it[0]) }
                .map { it.toInt() }
            return digits.first() * 10 + digits.last()
        }

        part1(lines.sumOf { valueOf(it) })
        part2(lines
            .map {
                it
                    .replace("one", "one1one")
                    .replace("two", "two2two")
                    .replace("three", "three3three")
                    .replace("four", "four4four")
                    .replace("five", "five5five")
                    .replace("six", "six6six")
                    .replace("seven", "seven7seven")
                    .replace("eight", "eight8eight")
                    .replace("nine", "nine9nine")
            }
            .sumOf { valueOf(it) })
    }
}
