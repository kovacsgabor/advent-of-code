package aoc

object Y23Day14 : Day() {

    enum class Tile { ROUND, SOLID, EMPTY }

    @JvmStatic
    fun main(args: Array<String>) {
        val map = lines
            .splitByPositions {
                when (it) {
                    "O" -> Tile.ROUND
                    "#" -> Tile.SOLID
                    else -> Tile.EMPTY
                }
            }
            .toMMap()

        val box = map.toPosBox()

        fun tilt(dir: Dir4) {
            // Make sure that we always iterate in the opposite direction as we tilt in, so not yet moved rocks do
            // not block others
            for (x in if (dir == Dir4.RIGHT) box.x.reversed() else box.x) {
                for (y in if (dir == Dir4.DOWN) box.y.reversed() else box.y) {
                    var pos = Pos(x, y)
                    if (map[pos] == Tile.ROUND && map[pos + dir] == Tile.EMPTY) {
                        map[pos] = Tile.EMPTY
                        pos += dir * 2
                        while (map[pos] == Tile.EMPTY) {
                            pos += dir
                        }
                        map[pos - dir] = Tile.ROUND
                    }
                }
            }
        }

        val maxY = map.maxPos().y
        fun calculateLoad() = map.filter { it.value == Tile.ROUND }.keys.sumOf { maxY - it.y + 1 }

        // First cycle and part 1
        tilt(Dir4.UP)
        part1(calculateLoad())
        tilt(Dir4.LEFT)
        tilt(Dir4.DOWN)
        tilt(Dir4.RIGHT)

        // Remaining cycles
        val history = umapOf<Map<Pos, Tile>, Long>()
        val maxCycles = 1_000_000_000L
        var jumpedForward = false
        var cycle = 1L // the first cycle is calculated above
        while (cycle < maxCycles) {
            // Detect repetitions and jump forward
            if (!jumpedForward) {
                val copy = map.toMap()
                if (copy in history) {
                    val repetitionLength = cycle - history[copy]
                    cycle += ((maxCycles - cycle) / repetitionLength) * repetitionLength
                    jumpedForward = true
                } else {
                    history[copy] = cycle
                }
            }
            tilt(Dir4.UP)
            tilt(Dir4.LEFT)
            tilt(Dir4.DOWN)
            tilt(Dir4.RIGHT)
            cycle++
        }

        part2(calculateLoad())
    }

}
