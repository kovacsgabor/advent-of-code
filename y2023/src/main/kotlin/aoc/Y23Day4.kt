package aoc

object Y23Day4 : Day() {

    @JvmStatic
    fun main(args: Array<String>) {
        var id = 0
        var score = 0
        val multipliers = countMapOf<Int>()
        lines.forEach { line ->
            id++
            multipliers[id]++
            val numbers = line.split(": ")[1].split(" | ")

            val winning = numbers[0].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
            val owned = numbers[1].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }.toSet()

            val matchingCount = winning.count { it in owned }

            score += 2 pow (matchingCount - 1)

            for (i in id + 1..id + matchingCount) {
                multipliers[i] += multipliers[id]
            }
        }

        // Remove non-existent cards
        multipliers.keys.filter { it > id }.forEach { multipliers.remove(it) }

        part1(score)
        part2(multipliers.values.sum())
    }

}
