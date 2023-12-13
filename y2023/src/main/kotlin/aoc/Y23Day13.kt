package aoc

object Y23Day13 : Day() {

    @JvmStatic
    fun main(args: Array<String>) {
        var part1 = 0L
        var part2 = 0L
        lines
            .splitWhen { it.isEmpty() }
            .forEach { pattern ->
                val map = pattern.splitByPositions { if (it == ".") 0L else 1L }

                val box = map.toPosBox()
                val rows = box.y.map { 0L }.toMList()
                val columns = box.x.map { 0L }.toMList()
                for (x in box.x.reversed()) {
                    for (y in box.y.reversed()) {
                        val value = map[Pos(x, y)]
                        columns[x] = columns[x] * 10L + value
                        rows[y] = rows[y] * 10L + value
                    }
                }

                fun findReflectionX(toIgnore: Long?): Long {
                    for (x in 0..<box.x.max()) {
                        if (
                            x != toIgnore
                            && (1..min(box.x.max() - x, x + 1)).all { columns[x + it] == columns[x + 1 - it] }
                        ) {
                            return x + 1
                        }
                    }
                    return 0
                }

                fun findReflectionY(toIgnore: Long?): Long {
                    for (y in 0..<box.y.max()) {
                        if (
                            y != toIgnore
                            && (1..min(box.y.max() - y, y + 1)).all { rows[y + it] == rows[y + 1 - it] }
                        ) {
                            return y + 1
                        }
                    }
                    return 0
                }

                val originalRows = findReflectionY(null)
                val originalColumns = if (originalRows == 0L) findReflectionX(null) else 0
                part1 += originalColumns + originalRows * 100

                for (modifiedX in box.x) {
                    for (modifiedY in box.y) {
                        // Whether to increase or decrease the bit at (modifiedX, modifiedY)
                        val bitDirection = if (map[Pos(modifiedX, modifiedY)] == 1L) -10 else 10
                        columns[modifiedX] += bitDirection pow modifiedY
                        rows[modifiedY] += bitDirection pow modifiedX

                        val newRows = findReflectionY(originalRows - 1)
                        if (newRows != 0L) {
                            part2 += newRows * 100
                            return@forEach
                        }
                        val newColumns = findReflectionX(originalColumns - 1)
                        if (newColumns != 0L) {
                            part2 += newColumns
                            return@forEach
                        }

                        // Reset the changed bit
                        columns[modifiedX] -= bitDirection pow modifiedY
                        rows[modifiedY] -= bitDirection pow modifiedX
                    }
                }
            }
        part1(part1)
        part2(part2)
    }

}
