package aoc

object Y23Day12 : Day() {

    enum class Tile {
        ON, OFF, UNKNOWN
    }

    private fun solve(repetitionCount: Int): Long = lines.sumOf { line ->
        val (tileString, groupString) = line.split(" ")

        val initialTiles = tileString.toChars().map {
            when (it) {
                "." -> Tile.OFF
                "#" -> Tile.ON
                "?" -> Tile.UNKNOWN
                else -> throw IllegalArgumentException()
            }
        }

        val initialGroups = groupString.split(",").map { it.toInt() }

        val tiles = initialTiles.toMList()
        val groups = initialGroups.toMList()

        // In part 2, the input is bigger
        repeat(repetitionCount) {
            tiles += Tile.UNKNOWN
            tiles += initialTiles
            groups += initialGroups
        }

        tiles += Tile.OFF // so we do not have to handle the case when the last tile is ON or UNKNOWN

        val solver = memoize<Int, Int, Long> { i, groupIndex ->
            // Skip consecutive OFF tiles
            var tileIndex = i
            while (tileIndex != tiles.size && tiles[tileIndex] == Tile.OFF) tileIndex++

            if (tileIndex == tiles.size) {
                return@memoize 0L // we have reached the end, but there are still groups left
            }

            var result = 0L

            // At this point, we know that the tile is either ON or UNKNOWN, so we can assume it to be ON.
            // "nextOff" is the index of the next OFF tile: all tiles before it must be ON, and it should OFF.
            // (We know that there is always an OFF after each run of ON because we added an OFF to the end.)
            val nextOff = tileIndex + groups[groupIndex]
            if (
                nextOff < tiles.size
                && tiles[nextOff] != Tile.ON
                && ((tileIndex + 1)..<nextOff).all { tiles[it] != Tile.OFF }
            ) {
                result += if (groupIndex + 1 < groups.size) {
                    // If there are more groups, call the function recursively
                    this(nextOff + 1, groupIndex + 1)
                } else {
                    // Otherwise, simply check that all subsequent tiles can be OFF
                    if (((nextOff + 1)..<tiles.size).all { tiles[it] != Tile.ON }) 1 else 0
                }
            }

            // If it is UNKNOWN, add the case where we assume it to be OFF
            if (tiles[tileIndex] == Tile.UNKNOWN) {
                result += this(tileIndex + 1, groupIndex)
            }

            result
        }

        solver(0, 0)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        part1(solve(0))
        part2(solve(4))
    }

}
