package aoc

object Y23Day23 : Day() {

    @JvmStatic
    fun main(args: Array<String>) = measured {
        val map = lines
            .splitByPositions()
            .filterValues { it != "#" }
            .toUMap(default = "#")

        val box = map.toPosBox()

        // The start and goal are in the leftmost and rightmost columns as the original leftmost and rightmost columns
        // are removed by the filter above
        val start = box.minPos()
        val goal = box.maxPos()

        // The start, goal and nodes with more than 2 (non-forest) neighbors.
        // These are the places where we have multiple choices.
        val junctions = map
            .keys
            .filter { pos -> pos == start || pos == goal || Dir4.values.count { it + pos in map } > 2 }
            .toSet()

        fun solve(slopesMatter: Boolean): Long {
            var graph = Dir4.filterNodes { it in map }

            if (slopesMatter) {
                graph = graph.filterEdges { pos, pos2 ->
                    val d = Dir4.of(map[pos])
                    d == null || pos + d == pos2
                }
            }

            val edges = junctions
                .associateWith { pos ->
                    // Find all paths to other junctions
                    graph
                        .filterEdges { from, _ -> from == pos || from !in junctions }
                        .bfs(pos)
                        .filter { it.node != pos && it.node in junctions }
                        .map { Edge(it.node, it.cost) }
                        .toList()
                }
                .toUMap()

            return WeightedGraph<Pos> { edges[it] }
                .bfs(start) { node, prev, _ -> prev.iterateBackwards.none { it.node == node } }
                .filter { it.node == goal }
                .map { it.cost }
                .max()
        }

        part1(solve(true))
        part2(solve(false))
    }

}
