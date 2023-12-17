package aoc

import kotlin.time.measureTime

/**
 * Creates a line from this to [other].
 * The result is never empty: this vector does not have to be less than [other].
 */
operator fun Pos.rangeTo(other: Pos): Line<Pos> =
    PosLine(UpOrDownRange(x, other.x), UpOrDownRange(y, other.y))

/**
 * Creates a line from this to [other].
 * The result is never empty: this vector does not have to be less than [other].
 */
operator fun Xyz.rangeTo(other: Xyz): Line<Xyz> =
    XyzLine(UpOrDownRange(x, other.x), UpOrDownRange(y, other.y), UpOrDownRange(z, other.z))

sealed interface Line<V : PosOrXyz> : Iterable<V> {
    val first: V
    val last: V
    val size: Long
}

private data class PosLine(val x: UpOrDownRange, val y: UpOrDownRange) : Line<Pos> {
    override val first: Pos get() = Pos(x.first, y.first)
    override val last: Pos get() = Pos(x.last, y.last)
    override val size: Long = max(x.size, y.size)

    override fun iterator(): Iterator<Pos> {
        val xx = x.supplier(size)
        val yy = y.supplier(size)
        return object : Iterator<Pos> {
            var index = 0L
            override fun hasNext(): Boolean = index < size
            override fun next(): Pos {
                index++
                return Pos(xx(), yy())
            }
        }
    }
}

private data class XyzLine(val x: UpOrDownRange, val y: UpOrDownRange, val z: UpOrDownRange) : Line<Xyz> {
    override val first: Xyz get() = Xyz(x.first, y.first, z.first)
    override val last: Xyz get() = Xyz(x.last, y.last, z.last)
    override val size: Long = max(max(x.size, y.size), z.size)

    override fun iterator(): Iterator<Xyz> {
        val xx = x.supplier(size)
        val yy = y.supplier(size)
        val zz = z.supplier(size)
        return object : Iterator<Xyz> {
            var index = 0L
            override fun hasNext(): Boolean = index < size
            override fun next(): Xyz {
                index++
                return Xyz(xx(), yy(), zz())
            }
        }
    }
}

/** Iterates from [first] to [last], both inclusive. */
private data class UpOrDownRange(val first: Long, val last: Long) {
    val size = abs(first - last) + 1
    val sign = (last - first).sign()

    fun supplier(targetSize: Long): () -> Long =
        when (size) {
            // Special case: we never increment
            1L -> {
                { first }
            }

            // Special case: we increment on every element
            targetSize -> {
                var value = first
                {
                    val result = value
                    value += sign
                    result
                }
            }

            // General case: we increment at specific intervals
            else -> {
                val incrementEvery = targetSize ceilDiv size
                var nextIncrement = incrementEvery
                var value = first
                {
                    val result = value
                    if (--nextIncrement == 0L) {
                        nextIncrement = incrementEvery
                        value += sign
                    }
                    result
                }
            }
        }

}
