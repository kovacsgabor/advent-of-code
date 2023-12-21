package aoc

object Y23Day21 : Day() {

    private const val GOAL1 = 64L
    private const val GOAL2 = 26501365L

    enum class Tile {
        EMPTY, ROCK, START
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val map = lines.splitByPositions {
            when (it) {
                "." -> Tile.EMPTY
                "#" -> Tile.ROCK
                "S" -> Tile.START
                else -> throw IllegalArgumentException()
            }
        }

        val source = map.filter { it.value == Tile.START }.keys.first()
        val box = map.toPosBox()
        val size = box.x.size
        val middle = source.x

        // Assert that the map is a square
        assert(box.y.size == size)

        // Assert that S sits exactly in the middle of the square
        assert(source.y == middle)
        assert(middle * 2 + 1 == size)

        // Assert that the edges and the row and column of S do not contain any rocks
        assert(map
            .filterKeys { it.x == middle || it.y == middle || it.x == 0L || it.y == 0L || it.x == size || it.y == size }
            .filterValues { it == Tile.ROCK }
            .isEmpty())

        // Assert that the required number of steps is exactly a multiple of the size + half of the size
        assert((GOAL2 - middle) % size == 0L)
        val times = (GOAL2 - middle) / size

        // Assert that the multiplier is even
        // (we could solve the problem if this was not true, but some modifications would have to be applied) 
        assert(times % 2 == 0L)

        // Run BFS to analyze the pattern on a few nearby boxes
        fun isRock(p: Pos): Boolean = map[Pos(p.x mod size, p.y mod size)] == Tile.ROCK
        val list = Dir4
            .filterNodes { !isRock(it) }
            .bfs(source)
            .takeWhile { it.cost <= middle + size * 2 }
            .toList()

        val boxes = (-2..2).map { x ->
            val xRange = x * size..<(x + 1) * size
            x to (-2..2).map { y ->
                val yRange = y * size..<(y + 1) * size
                val b = PosBox(xRange, yRange)
                y to list.count { it.node in b && it.cost % 2 == GOAL2 % 2 }
            }.toUMap()
        }.toUMap()

        part1(list.count { it.cost <= GOAL1 && it.cost % 2 == GOAL1 % 2 })

        // Complete even and odd repetitions based on the original square 
        val even = boxes[0][0]
        val odd = boxes[1][0]

        val evenInTriangles = (1..<times - 1 step 2).sum()
        val oddInTriangles = (2..<times - 1 step 2).sum()
        part2(
            // middle
            even

                    // the four straight paths 
                    + ((times - 1) ceilDiv 2) * odd * 4
                    + ((times - 1) / 2) * even * 4

                    // ends of these straight paths
                    + boxes[-2][0] + boxes[0][-2] + boxes[2][0] + boxes[0][2]

                    // triangles
                    + evenInTriangles * even * 4
                    + oddInTriangles * odd * 4

                    // edge of triangles
                    + (times - 1) * (boxes[-1][-1] + boxes[-1][1] + boxes[1][-1] + boxes[1][1])
                    + times * (boxes[-2][-1] + boxes[-2][1] + boxes[1][-2] + boxes[2][1])
        )
    }

}
