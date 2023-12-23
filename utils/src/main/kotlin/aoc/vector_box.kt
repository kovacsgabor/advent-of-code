package aoc

/** A bounding box of 2D vectors. */
data class PosBox(val x: LongRange, val y: LongRange)

/** A bounding box of 3D vectors. */
data class XyzBox(val x: LongRange, val y: LongRange, val z: LongRange)


operator fun PosBox.contains(p: Pos): Boolean = p.x in x && p.y in y
operator fun XyzBox.contains(p: Xyz): Boolean = p.x in x && p.y in y && p.z in z


// Iteration
fun PosBox.xThenY(): Iterable<Pos> =
    Iterable {
        object : Iterator<Pos> {
            var xx = x.first
            var yy = y.first

            override fun hasNext(): Boolean = yy <= y.last

            override fun next(): Pos {
                val p = Pos(xx, yy)
                xx++
                if (xx > x.last) {
                    xx = x.first
                    yy++
                }
                return p
            }
        }
    }

fun XyzBox.xThenYThenZ(): Iterable<Xyz> =
    Iterable {
        object : Iterator<Xyz> {
            var xx = x.first
            var yy = y.first
            var zz = z.first

            override fun hasNext(): Boolean = zz <= z.last

            override fun next(): Xyz {
                val p = Xyz(xx, yy, zz)
                xx++
                if (xx > x.last) {
                    xx = x.first
                    yy++
                    if (yy > y.last) {
                        yy = y.first
                        zz++
                    }
                }
                return p
            }
        }
    }

/** Returns the minimum value of this box. */
fun PosBox.minPos(): Pos = Pos(x.first, y.first)

/** Returns the maximum value of this box. */
fun PosBox.maxPos(): Pos = Pos(x.last, y.last)

/** Returns the minimum value of this box. */
fun XyzBox.minXyz(): Xyz = Xyz(x.first, y.first, z.first)

/** Returns the maximum value of this box. */
fun XyzBox.maxXyz(): Xyz = Xyz(x.last, y.last, z.last)


/** Calculates the bounding box of these vectors. */
fun Iterable<Pos>.toPosBox(): PosBox = PosBox(x.range(), y.range())

/** Calculates the bounding box of these vectors. */
fun Iterable<Xyz>.toXyzBox(): XyzBox = XyzBox(x.range(), y.range(), z.range())

/** Calculates the bounding box of the keys. */
fun Map<Pos, *>.toPosBox(): PosBox = PosBox(x.range(), y.range())

/** Calculates the bounding box of the keys. */
fun Map<Xyz, *>.toXyzBox(): XyzBox = XyzBox(x.range(), y.range(), z.range())

/**
 * Increases the ranges of this box in both directions with the given amount.
 */
fun PosBox.widen(amount: Long) = PosBox(x.widen(amount), y.widen(amount))

/**
 * Increases the ranges of this box in both directions with the given amount.
 */
fun XyzBox.widen(amount: Long) = XyzBox(x.widen(amount), y.widen(amount), z.widen(amount))
