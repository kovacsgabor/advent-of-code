package aoc

object Y23Day18 : Day() {

    data class Line(val dir: Dir4, val count: Int)

    private fun solve(lines: List<Line>): Long {
        // The true corners of the loop
        val trueCornerNodesOfLoop = lines.runningFold(Pos.zero) { pos, (dir, count) -> pos + dir * count }.toSet()

        val xCoordinates = trueCornerNodesOfLoop.x.toSet()
        val yCoordinates = trueCornerNodesOfLoop.y.toSet()

        val xCoordinatesSorted = xCoordinates.sorted()
        val yCoordinatesSorted = yCoordinates.sorted()

        // From here, we introduce an alternative representation where empty rows and columns between the loop nodes
        // are compressed into 1 each.

        fun xFromCompressed(v: Long): Long = xCoordinatesSorted[v / 2]
        fun yFromCompressed(v: Long): Long = yCoordinatesSorted[v / 2]

        fun Pos.toCompressed(): Pos = Pos(
            xCoordinatesSorted.binarySearch(x) * 2L,
            yCoordinatesSorted.binarySearch(y) * 2L,
        )

        var truePos = Pos.zero
        var compressedPos = truePos.toCompressed()
        val compressedNodesOfLoop = lines.flatMap { (dir, count) ->
            val from = compressedPos
            truePos += dir * count
            compressedPos = truePos.toCompressed()
            from..compressedPos
        }.toSet()

        val box = compressedNodesOfLoop.toPosBox()
        val greaterBox = box.widen(1) // so we can go around the loop

        val graph = Dir4.filterNodes { it in greaterBox && it !in compressedNodesOfLoop }

        val notEnclosedPositions = graph.bfs(greaterBox.minPos()).nodes.toSet()

        return box
            .xThenY()
            .filter { it !in notEnclosedPositions }
            .sumOf { (x, y) ->
                // Even coordinates are mapped to 1 true coordinate, so they are worth 1
                // Odd coordinates are worth the amount we skipped between their neighbors

                val xSize = if (x % 2 == 0L) 1 else xFromCompressed(x + 1) - xFromCompressed(x - 1) - 1
                val ySize = if (y % 2 == 0L) 1 else yFromCompressed(y + 1) - yFromCompressed(y - 1) - 1
                xSize * ySize
            }

    }

    @JvmStatic
    fun main(args: Array<String>) {
        val linesForPart1 = mutableListOf<Line>()
        val linesForPart2 = mutableListOf<Line>()

        lines.parseEach {
            "$word $int \\(#(.....)(.)\\)" { (d: Dir4.OneChar, amount: Int, amount2: String, d2: String) ->
                linesForPart1 += Line(d.toPos(), amount)
                linesForPart2 += Line(
                    when (d2) {
                        "0" -> Dir4.RIGHT
                        "1" -> Dir4.DOWN
                        "2" -> Dir4.LEFT
                        "3" -> Dir4.UP
                        else -> throw IllegalArgumentException()
                    },
                    amount2.toInt(16)
                )
            }
        }

        part1(solve(linesForPart1))
        part2(solve(linesForPart2))
    }

}
