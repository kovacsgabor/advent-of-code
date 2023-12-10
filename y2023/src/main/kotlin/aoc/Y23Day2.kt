package aoc

object Y23Day2 : Day() {
    private const val MAX_RED = 12
    private const val MAX_GREEN = 13
    private const val MAX_BLUE = 14

    @JvmStatic
    fun main(args: Array<String>) {
        var part1 = 0
        var part2 = 0
        var id = 0
        lines.forEach { line ->
            id++

            var red = 0
            var green = 0
            var blue = 0
            line
                .split(":", ";", ",")
                .drop(1)
                .map { it.trim() }
                .map { it.split(" ") }
                .map { it[0].toInt() to it[1] }
                .forEach { (count, type) ->
                    when (type) {
                        "red" -> red = max(red, count)
                        "green" -> green = max(green, count)
                        "blue" -> blue = max(blue, count)
                        else -> throw IllegalArgumentException()
                    }
                }

            if (red <= MAX_RED && green <= MAX_GREEN && blue <= MAX_BLUE) {
                part1 += id
            }
            part2 += red * blue * green
        }
        part1(part1)
        part2(part2)
    }

}
