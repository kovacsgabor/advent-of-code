package aoc

object Y23Day10 : Day() {

    @JvmStatic
    fun main(args: Array<String>) = measured {
        val map = lines.splitByPositions {
            when (it) {
                "-" -> listOf(Dir4.LEFT, Dir4.RIGHT)
                "|" -> listOf(Dir4.UP, Dir4.DOWN)
                "L" -> listOf(Dir4.UP, Dir4.RIGHT)
                "J" -> listOf(Dir4.UP, Dir4.LEFT)
                "7" -> listOf(Dir4.LEFT, Dir4.DOWN)
                "F" -> listOf(Dir4.DOWN, Dir4.RIGHT)
                "." -> listOf()
                "S" -> Dir4.values
                else -> throw IllegalArgumentException()
            }
        }

        val start = map.keys.first { map[it].size == 4 }
        map[start] = Dir4.values.filter { it.opposite() in map[it + start] }

        val graph = Graph<Pos> { pos -> map[pos].map { it + pos } }.filterNodes { it in map.keys }
        part1(graph.bfs(start).map { it.cost }.max())

        val neighborsOfStart = Dir4.values.map { it + start }
        val loop = graph.dfs(start)
            .filter { it.node in neighborsOfStart }
            .maxBy { it.cost }
            .pathForwards
            .map { it.node }
            .toSet()

        // Convert each node into 3x3 and mark the loop positions in this enlarged grid
        val enlargedLoop = loop
            .flatMap { pos -> (map[pos] + Pos.zero).map { pos * 3 + Pos.one + it } }
            .toSet()

        val box = listOf(Pos(-1, -1), (map.maxPos() + Pos.one) * 3).toPosBox()

        // Find all nodes that are outside the loop
        val outside = Dir4
            .filterNodes { it !in enlargedLoop && it in box }
            .bfs(Pos(-1, -1))
            .nodes
            .toSet()

        part2(map
            .keys
            .filter { it !in loop }
            .map { it * 3 }
            .count { it !in outside })
    }

}
