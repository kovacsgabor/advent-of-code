package aoc

object Y23Day12 : Day() {

    enum class Tile {
        ON, OFF, UNKNOWN
    }

    private fun solve(repetitionCount: Int): Long {
        var solutions = 0L
        lines.forEach { line ->
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

            fun getGroupSize(i: Int) = if (i < groups.size) groups[i] else 0

            val solver = memoize {
                    tileIndex: Int, // the current index in "tiles"
                    groupIndex: Int, // the current index in "groups"
                    currentRunLength: Int, // the number of consecutive ONs we have already found
                ->

                /** Treat the current tile as if it was [Tile.ON]. */
                fun handleOn(): Long {
                    // Skip consecutive ON tiles
                    var i = tileIndex + 1
                    while (i != tiles.size && tiles[i] == Tile.ON) i++

                    val newRunLength = currentRunLength + (i - tileIndex)

                    // If the current run is already too big, exit now.
                    // This is important as the next tile might be UNKNOWN where we attempt both choices eagerly, and
                    // we should not do that for already dead branches
                    return if (newRunLength > getGroupSize(groupIndex)) 0

                    // Otherwise, continue with the next index
                    else this(i, groupIndex, newRunLength)
                }

                /** Treat the current tile as if it was [Tile.OFF]. */
                fun handleOff(): Long {
                    // Skip consecutive OFF tiles
                    var i = tileIndex + 1
//                    while (i != tiles.size && tiles[i] == Tile.OFF) i++

                    // We are currently closing a group, so check its size
                    return when (currentRunLength) {
                        // The group has not yet started -> continue with the same group
                        // (this can happen when an UNKNOWN is treated as OFF after another OFF)
                        0 -> this(i, groupIndex, 0)

                        // The group is of the correct size -> continue with the next group
                        getGroupSize(groupIndex) -> this(i, groupIndex + 1, 0)

                        // The size of the currently closed group is incorrect, so finish now
                        else -> 0
                    }
                }

                if (tileIndex == tiles.size) {
                    // We have reached the end, we are OK if all groups have been matched
                    if (groupIndex == groups.size) 1 else 0
                } else when (tiles[tileIndex]) {
                    Tile.ON -> handleOn()
                    Tile.OFF -> handleOff()
                    Tile.UNKNOWN -> handleOn() + handleOff()
                }
            }

            solutions += solver(0, 0, 0)
        }
        return solutions
    }

    @JvmStatic
    fun main(args: Array<String>) {
        part1(solve(0))
        part2(solve(4))
    }

}
