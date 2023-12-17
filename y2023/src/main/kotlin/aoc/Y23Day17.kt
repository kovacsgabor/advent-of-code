package aoc

object Y23Day17 : Day() {

    data class State(val pos: Pos, val dir: Dir4, val straightSteps: Int)

    @JvmStatic
    fun main(args: Array<String>) {
        val map = lines.splitByPositions { it.toLong() }
        val box = map.toPosBox()
        val goal = map.maxPos()

        fun solve(minStraightSteps: Int, maxStraightSteps: Int): Long =
            Graph<State> { (pos, dir, straightSteps) ->
                val nextStates = mutableListOf<State>()
                if (straightSteps >= minStraightSteps || straightSteps == 0) { // we can turn
                    fun turn(d: Dir4) = State(pos + d, d, 1)
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
                        // The starting direction is not specified, so let us choose a direction that allows us to go
                        // both downwards and rightwards from here.
                        Dir4.DOWN,
                        // Set this to 0 instead of 1, so the downward steps are not limited
                        0,
                    )
                )
                .filter { it.node.pos == goal && it.node.straightSteps >= minStraightSteps }
                .first()
                .cost

        part1(solve(0, 3))
        part2(solve(4, 10))
    }

}
