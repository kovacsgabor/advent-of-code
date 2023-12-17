package aoc

object Y23Day17 : Day() {

    data class State(val pos: Pos, val dir: Dir4?, val straightSteps: Int)

    @JvmStatic
    fun main(args: Array<String>) {
        val map = lines.splitByPositions { it.toLong() }
        val box = map.toPosBox()
        val goal = map.maxPos()

        fun solve(minStraightSteps: Int, maxStraightSteps: Int): Long =
            Graph<State> { (pos, dir, straightSteps) ->
                fun turn(d: Dir4) = State(pos + d, d, 1)

                // initial state
                if (dir == null) return@Graph listOf(turn(Dir4.RIGHT), turn(Dir4.DOWN))

                val nextStates = mutableListOf<State>()
                if (straightSteps >= minStraightSteps) { // we can turn
                    nextStates += turn(dir.right())
                    nextStates += turn(dir.left())
                }
                if (straightSteps < maxStraightSteps) { // we can go straight forward
                    nextStates += State(pos + dir, dir, straightSteps + 1)
                }
                nextStates
            }
                .filterNodes { it.pos in box } // do not leave the map
                .weighted { _, nextState -> map[nextState.pos] } // apply weights based on the target location
                .dijkstra(
                    State(
                        Pos(0, 0),
                        null, // the start node is a special case
                        0 // does not matter, we handle this state in a special way
                    )
                )
                .filter { it.node.pos == goal && it.node.straightSteps >= minStraightSteps }
                .first()
                .cost

        part1(solve(1, 3))
        part2(solve(4, 10))
    }

}
