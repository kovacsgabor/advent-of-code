package aoc

object Y23Day22 : Day() {

    private val down = Xyz(0, 0, -1)

    data class Brick(val from: Xyz, val to: Xyz) {
        // Cache the locations of this brick
        val positions = (from..to).toSet()
    }

    /**
     * Simulates the falls of all given bricks.
     *
     * @param removedBrick if not null, only those bricks that are directly above this one need checking,
     *      otherwise all bricks have to be checked
     */
    private fun simulateFalls(all: List<Brick>, removedBrick: Brick?): List<Brick> {
        // Maps to each occupied position the index of the brick that is in that location
        val allPositions = all.flatMapIndexed { index, b -> b.positions.map { it to index } }.toUMap()

        /**
         * Returns whether this brick can fall to that location,
         * i.e. if that location is empty not considering the brick itself.
         */
        fun Brick.canFallTo(from: Xyz, to: Xyz) =
            from.z != 0L && to.z != 0L && (from..to).all { it !in allPositions || it in this.positions }

        /** Returns the new brick if this brick can fall, null otherwise. */
        fun Brick.simulateFall(): Brick? {
            var (from, to) = this
            from += down
            to += down

            // Cannot fall at all
            if (!canFallTo(from, to)) return null

            // Try to fall more
            var nextFrom = from + down
            var nextTo = to + down
            while (canFallTo(nextFrom, nextTo)) {
                from = nextFrom
                to = nextTo
                nextFrom += down
                nextTo += down
            }

            return Brick(from, to)
        }

        /** Returns the indices of bricks exactly above this one. */
        fun Brick.indicesAbove(): Set<Int> =
            (from - down..to - down).mapNotNull { allPositions.getOrDefault(it, null) }.toSet()

        val bricks = all.toMList()

        // The index of those bricks which might fall
        var indicesToCheck = removedBrick?.indicesAbove() ?: bricks.indices.toList()

        while (indicesToCheck.isNotEmpty()) {
            indicesToCheck = indicesToCheck.flatMap { index ->
                val brick = bricks[index]
                // Simulate the fall of this brick
                val newBrick = brick.simulateFall()
                if (newBrick != null) { // it can fall
                    bricks[index] = newBrick

                    // Update the locations in the map
                    allPositions -= brick.positions
                    newBrick.positions.forEach { allPositions[it] = index }

                    // Return the indices of those other bricks above this one for ones that need checking in the next
                    // iteration
                    brick.indicesAbove()
                } else {
                    listOf()
                }
            }
        }
        return bricks
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val bricks = simulateFalls(
            lines.map { line ->
                val a = line.split("~", ",").map { it.toLong() }
                val brick = Brick(Xyz(a[0], a[1], a[2]), Xyz(a[3], a[4], a[5]))
                brick
            },
            null,
        )

        var part1 = 0L
        var part2 = 0L
        for (brick in bricks) {
            val others = bricks - brick
            val afterFall = simulateFalls(others, brick)
            val count = others.indices.count { others[it] != afterFall[it] }
            if (count == 0) part1++
            part2 += count
        }

        part1(part1)
        part2(part2)
    }

}
