package aoc

object Y23Day6 : Day() {

    @JvmStatic
    fun main(args: Array<String>) {
        fun solve(time: Long, distance: Long): Long {
            var counts = 0L
            for (t in 1..<time) {
                val travelled = (time - t) * t
                if (travelled > distance) {
                    counts++
                }
            }
            return counts
        }

        // Part 1
        fun split(line: String): List<Long> = line.split(":")[1]
            .split(" ")
            .filterNot { it.isEmpty() }
            .map { it.toLong() }

        val times = split(lines[0])
        val distances = split(lines[1])

        part1(times.indices.map { solve(times[it], distances[it]) }.product())

        // Part 2
        fun combine(list: List<Long>): Long = list.joinToString(separator = "").toLong()

        part2(solve(combine(times), combine(distances)))
    }

}
