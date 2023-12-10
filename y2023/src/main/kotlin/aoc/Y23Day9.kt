package aoc

object Y23Day9 : Day() {

    @JvmStatic
    fun main(args: Array<String>) = measured {
        var part1 = 0
        var part2 = 0
        lines.forEach { line ->
            val values = mutableListOf<MList<Int>>()

            values.add(line.split(" ").map { it.toInt() }.toMList())

            while (values.last().any { it != 0 }) {
                values += values.last()
                    .windowed(2)
                    .map { it[1] - it[0] }
                    .toMList()
            }

            for (i in values.size - 2 downTo 0) {
                values[i].add(0, values[i].first() - values[i + 1].first())
                values[i].add(values[i].last() + values[i + 1].last())
            }
            part2 += values[0].first()
            part1 += values[0].last()
        }
        part1(part1)
        part2(part2)
    }

}
