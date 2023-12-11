package aoc

object Y23Day11 : Day() {

    @JvmStatic
    fun main(args: Array<String>) {
        val galaxies = lines.splitByPositions().filter { it.value == "#" }.keys

        // Bounding box of coordinates
        val box = galaxies.toPosBox()

        // Empty rows and columns
        val emptyX = box.x - galaxies.map { it.x }.toSet()
        val emptyY = box.y - galaxies.map { it.y }.toSet()

        // Galaxies with increased distances
        val galaxiesTwo = mutableListOf<Pos>()
        val galaxiesMillion = mutableListOf<Pos>()
        galaxies.forEach { pos ->
            val increment = Pos(emptyX.count { it < pos.x }, emptyY.count { it < pos.y })
            galaxiesTwo += pos + increment
            galaxiesMillion += pos + increment * 999_999
        }

        fun solve(galaxies: List<Pos>): Long {
            var sum = 0L
            for (i in galaxies.indices) {
                for (j in (i + 1)..<galaxies.size) {
                    sum += galaxies[i] mdist galaxies[j]
                }
            }
            return sum
        }

        part1(solve(galaxiesTwo))
        part2(solve(galaxiesMillion))
    }

}
