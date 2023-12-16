package aoc

object Y23Day16 : Day() {

    data class State(val pos: Pos, val dir: Dir4)

    @JvmStatic
    fun main(args: Array<String>) {
        val map = lines.splitByPositions()
        val box = map.toPosBox()

        val graph = Graph<State> { (pos, dir) ->
            val newDirs = when (map[pos]) {
                "." -> listOf(dir)

                "\\" -> when (dir) {
                    Dir4.DOWN -> listOf(Dir4.RIGHT)
                    Dir4.LEFT -> listOf(Dir4.UP)
                    Dir4.RIGHT -> listOf(Dir4.DOWN)
                    Dir4.UP -> listOf(Dir4.LEFT)
                }

                "/" -> when (dir) {
                    Dir4.DOWN -> listOf(Dir4.LEFT)
                    Dir4.LEFT -> listOf(Dir4.DOWN)
                    Dir4.RIGHT -> listOf(Dir4.UP)
                    Dir4.UP -> listOf(Dir4.RIGHT)
                }

                "-" -> when (dir) {
                    Dir4.DOWN, Dir4.UP -> listOf(Dir4.LEFT, Dir4.RIGHT)
                    Dir4.LEFT, Dir4.RIGHT -> listOf(dir)
                }

                "|" -> when (dir) {
                    Dir4.DOWN, Dir4.UP -> listOf(dir)
                    Dir4.LEFT, Dir4.RIGHT -> listOf(Dir4.DOWN, Dir4.UP)
                }

                else -> throw IllegalArgumentException()
            }

            newDirs.map { State(it + pos, it) }
        }
            .filterNodes { it.pos in box }

        fun count(x: Long, y: Long, dir: Dir4): Int =
            graph
                .bfs(State(Pos(x, y), dir))
                .nodes
                .distinctBy { it.pos }
                .count()

        part1(count(0, 0, Dir4.RIGHT))
        part2(
            listOf(
                box.y.maxOf { count(0, it, Dir4.RIGHT) },
                box.y.maxOf { count(box.x.last(), it, Dir4.LEFT) },
                box.x.maxOf { count(it, 0, Dir4.DOWN) },
                box.x.maxOf { count(it, box.y.last(), Dir4.UP) },
            ).max()
        )
    }

}
