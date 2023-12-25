package aoc

import java.math.BigInteger

object Y23Day24 : Day() {

    /** Hailstone in part 1. */
    data class Hailstone2D(val from: Pos, val velocity: Pos)

    /** Hailstone in part 2. */
    data class Hailstone3D(val from: Xyz, val velocity: Xyz)

    @JvmStatic
    fun main(args: Array<String>) = measured {
        val hailstones2D = mutableListOf<Hailstone2D>()
        val hailstones3D = mutableListOf<Hailstone3D>()

        lines.forEach { line ->
            val components = line.split(",", "@").map { it.trim().toLong() }
            hailstones2D += Hailstone2D(
                Pos(components[0], components[1]),
                Pos(components[3], components[4]),
            )
            hailstones3D += Hailstone3D(
                Xyz(components[0], components[1], components[2]),
                Xyz(components[3], components[4], components[5]),
            )
        }

        solvePart1(hailstones2D)
        solvePart2(hailstones3D)
    }

    private fun solvePart1(hailstones: List<Hailstone2D>) {
        val range = if (test) 7L..27 else 200000000000000L..400000000000000
        val box = PosBox(range, range)

        fun Hailstone2D.isInFuture(pos: Pos): Boolean =
            (pos.x - from.x).sign() == velocity.x.sign()
                    && (pos.y - from.y).sign() == velocity.y.sign()

        fun isIntersectionInArea(a: Hailstone2D, b: Hailstone2D): Boolean {
            // Find the intersection point of the lines the hailstones travel on
            val (x1, y1) = a.from.toList().map { it.toBig() }
            val (x2, y2) = (a.from + a.velocity).toList().map { it.toBig() }
            val (x3, y3) = b.from.toList().map { it.toBig() }
            val (x4, y4) = (b.from + b.velocity).toList().map { it.toBig() }

            val denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)

            if (denominator == BigInteger.ZERO) return false

            val numeratorX = (x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)
            val numeratorY = (x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)

            val (x, remainderX) = numeratorX.divideAndRemainder(denominator)
            val (y, remainderY) = numeratorY.divideAndRemainder(denominator)

            val intersectionFloor = Pos(x.toLong(), y.toLong())

            // If there were remainders, then the actual intersection is between grid points, so also check the ceiling
            // to be in the box
            val intersectionCeiling = Pos(
                if (remainderX == BigInteger.ZERO) x.toLong() else x.toLong() + 1,
                if (remainderY == BigInteger.ZERO) y.toLong() else y.toLong() + 1,
            )

            return intersectionFloor in box
                    && intersectionCeiling in box
                    && a.isInFuture(intersectionFloor)
                    && b.isInFuture(intersectionFloor)
        }

        part1(
            hailstones.indices.sumOf { i ->
                (0..<i).count { j -> isIntersectionInArea(hailstones[i], hailstones[j]) }
            }
        )
    }

    private fun solvePart2(hailstones: List<Hailstone3D>) {
        // The algorithm for part 2 makes use of the fact that the components of the velocity are small, and iterates
        // over the possible values of the x any y components, as these are enough to calculate the times when the rock
        // has to reach each hailstone.
        //
        // This might not be the most elegant solution, but it is correct and fast.

        /**
         * Calculates the time when the rock destroys the ith hailstone based on the x and y component of the rock velocity.
         * Returns -1 if these velocities are definitely incorrect.
         */
        fun calculateTime(velocityX: Long, velocityY: Long, i: Int): Long {
            // We can choose any other hailstone here
            val j = if (i != 0) 0 else 1

            // The following calculation is based on a number of equations.
            // We know that the rock and the ith hailstone exactly meet at a specific time, so for each of their
            // components:
            //
            // F.a + t[i] * V.a == f[i].a + t[i] * v[i].a
            //
            // where:
            //   a is one of x, y or z
            //   t[i] is the time when the ith rock is destroyed
            //   F is initial position of the rock
            //   V is the velocity of the rock
            //   f[i] is the initial position of the ith hailstone
            //   v[i] is velocity of the ith hailstone
            //
            // With simplified notation:
            //
            // Fa + ti * Va == fia + ti * via
            //
            // Based on this, we can say:
            //
            // Fa == fia + ti * via - ti * Va
            // Fa == fia + ti * (via - Va)
            //
            // Let us replace (via - Va) with dia, the difference between the velocity of the ith
            // hailstone and the rock:
            //
            // Fa == fia + ti * dia
            //
            // The same is true for hailstone j, so based on the two equations using Fa:
            //
            // fia + ti * dia == fja + tj * dja
            //       ti * dia == fja - fia + tj * dja
            //             ti == (fja - fia + tj * dja) / dia
            //
            // Let us replace fja - fia with gjia, the difference between the initial positions
            // of these hailstones:
            //
            // ti == (gjia + tj * dja) / dia
            //
            // But due to the symmetric nature of our steps, also (for any component "b"):
            //
            // tj == (gijb + ti * dib) / djb
            //
            // If we replace "a" with "x" and "b" with "y":
            //
            // ti == (gjix + tj * djx) / dix
            // and
            // tj == (gijy + ti * diy) / djy
            //
            // Now, using the second equation to replace tj in the first one:
            //
            //                              ti == (gjix + ((gijy + ti * diy) / djy) * djx) / dix
            //                        ti * dix == gjix + ((gijy + ti * diy) / djy) * djx
            //                  ti * dix * djy == gjix * djy + (gijy + ti * diy) * djx
            //                  ti * dix * djy == gjix * djy + gijy * djx + ti * diy * djx
            // ti * dix * djy - ti * diy * djx == gjix * djy + gijy * djx
            //    ti * (dix * djy - diy * djx) == gjix * djy + gijy * djx
            //                              ti == (gjix * djy + gijy * djx) / (dix * djy - diy * djx)

            val dix = hailstones[i].velocity.x - velocityX
            val diy = hailstones[i].velocity.y - velocityY

            val djx = hailstones[j].velocity.x - velocityX
            val djy = hailstones[j].velocity.y - velocityY

            val denominator = dix * djy - diy * djx

            if (denominator == 0L) return -1

            val gijy = (hailstones[i].from.y - hailstones[j].from.y)
            val gjix = (hailstones[j].from.x - hailstones[i].from.x)

            val numerator = gijy * djx + gjix * djy

            if (numerator % denominator != 0L) return -1

            return numerator / denominator
        }

        var found = false
        fun tryWithVelocity(velocityX: Long, velocityY: Long) {
            // The individual times when these hailstones are eliminated based on the velocity
            val t0 = calculateTime(velocityX, velocityY, 0)
            if (t0 < 0) return

            val t1 = calculateTime(velocityX, velocityY, 1)
            if (t1 < 0 || t1 == t0) return

            /** Returns where this hailstone will be at the specified time. */
            fun Hailstone3D.locationAt(time: Long): Xyz = from + velocity * time

            // The points where the hailstones are eliminated
            val p0 = hailstones[0].locationAt(t0)
            val p1 = hailstones[1].locationAt(t1)

            val velocityZ = (p0.z - p1.z) / (t0 - t1)
            val velocity = Xyz(velocityX, velocityY, velocityZ)

            // The initial position of the rock
            val from = p0 - t0 * velocity
            if (p1 - t1 * velocity != from) return

            // Check a third hailstone to validate that these are indeed the correct parameters.
            // Checking a third one is enough because with three hailstones, we have the same amount of equations as
            // we have variables.
            val t2 = calculateTime(velocityX, velocityY, 2)
            val p2 = hailstones[2].locationAt(t2)
            if (t2 < 0 || p2 - t2 * velocity != from) return

            found = true
            part2(from.toList().sum())
        }

        var size = 1L
        while (!found) {
            // Search in ever increasing ranges, but only the cases when at least one of x or y is -size or size,
            // as the other cases are already checked
            for (velocityX in -size..size) {
                tryWithVelocity(velocityX, -size)
                tryWithVelocity(velocityX, size)
            }
            for (velocityY in -size + 1..<size) {
                tryWithVelocity(-size, velocityY)
                tryWithVelocity(size, velocityY)
            }
            size++
        }
    }

}



