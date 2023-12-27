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
            .toList()

        check(junctions.size < 64)

        val junctionToIndexMap = junctions.mapIndexed { index, pos -> pos to index }.toUMap()

        val startIndex = junctionToIndexMap[start]
        val goalIndex = junctionToIndexMap[goal]
        val all = junctions.indices.map { 1L shl it }.reduce(Long::or)

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
                        .filterEdges { from, _ -> from == pos || from !in junctionToIndexMap }
                        .bfs(pos)
                        .filter { it.node in junctionToIndexMap && it.node != pos }
                        .map { it.node to it.cost }
                        .toMap()
                }

            val edgeCostsByIndex = junctions.map { from ->
                val m = edges[from]!!
                junctions.map { to -> m.getOrDefault(to, 0L) }
            }

            val edgeBitSet = edgeCostsByIndex.map { costs ->
                costs
                    .mapIndexedNotNull { index, cost -> if (cost > 0) 1L shl index else null }
                    .reduceOrNull(Long::or) ?: 0L
            }

            var maxCost = 0L

            fun search(index: Int, notVisited: Long, cost: Long) {
                if (index == goalIndex) {
                    maxCost = max(maxCost, cost)
                    return
                }

                val nv = notVisited xor (1L shl index)
                var targets = edgeBitSet[index] and nv
                val costs = edgeCostsByIndex[index]

                while (targets != 0L) {
                    val lowest = targets.takeLowestOneBit()
                    val nextIndex = lowest.countTrailingZeroBits()
                    search(nextIndex, nv, cost + costs[nextIndex])
                    targets = targets xor lowest
                }
            }

            search(startIndex, all, 0L)
            return maxCost
        }

        part1(solve(true))
        part2(solve(false))
    }


}
