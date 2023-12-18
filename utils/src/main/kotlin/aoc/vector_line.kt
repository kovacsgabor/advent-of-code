package aoc

/**
 * Creates a line from this to [other].
 * The result is never empty: this vector does not have to be less than [other].
 */
operator fun Pos.rangeTo(other: Pos): PosLine =
    PosLine(x upOrDownTo other.x, y upOrDownTo other.y)

/**
 * Creates a line from this to [other].
 * The result is never empty: this vector does not have to be less than [other].
 */
operator fun Xyz.rangeTo(other: Xyz): XyzLine =
    XyzLine(x upOrDownTo other.x, y upOrDownTo other.y, z upOrDownTo other.z)

sealed interface Line<V : PosOrXyz> : Iterable<V> {
    val first: V
    val last: V
    val size: Long
    operator fun contains(v: V): Boolean
}

/** A line between [first] and [last] on the 2D plane. */
data class PosLine(val x: LongProgression, val y: LongProgression) : Line<Pos> {
    override val first: Pos get() = Pos(x.first, y.first)
    override val last: Pos get() = Pos(x.last, y.last)
    override val size: Long = max(x.size, y.size)

    private val xx = EnlargedLongProgression(x, size)
    private val yy = EnlargedLongProgression(y, size)

    override fun contains(v: Pos): Boolean =
        (xx.indexOf(v.x) intersectRange yy.indexOf(v.y)).isNotEmpty()

    override fun iterator(): Iterator<Pos> = iteratorFromFunction(size) { Pos(xx[it], yy[it]) }
}

/** A line between [first] and [last] on the 3D plane. */
data class XyzLine(val x: LongProgression, val y: LongProgression, val z: LongProgression) : Line<Xyz> {
    override val first: Xyz get() = Xyz(x.first, y.first, z.first)
    override val last: Xyz get() = Xyz(x.last, y.last, z.last)
    override val size: Long = max(max(x.size, y.size), z.size)

    private val xx = EnlargedLongProgression(x, size)
    private val yy = EnlargedLongProgression(y, size)
    private val zz = EnlargedLongProgression(z, size)

    override fun contains(v: Xyz): Boolean =
        (xx.indexOf(v.x) intersectRange yy.indexOf(v.y) intersectRange zz.indexOf(v.z)).isNotEmpty()

    override fun iterator(): Iterator<Xyz> = iteratorFromFunction(size) { Xyz(xx[it], yy[it], zz[it]) }

}

private inline fun <T> iteratorFromFunction(size: Long, crossinline function: (Long) -> T): Iterator<T> =
    object : Iterator<T> {
        private var index = 0L
        override fun hasNext(): Boolean = index < size
        override fun next(): T = function(index++)
    }


private class EnlargedLongProgression(val progression: LongProgression, val targetSize: Long) {
    private val progressionSize = progression.size
    private val multiplier = progressionSize.toDouble() / targetSize * progression.step
    private fun toTargetIndex(index: Long) = index * targetSize ceilDiv progressionSize

    operator fun get(index: Long): Long = progression.first + (index * multiplier).toLong()

    fun indexOf(value: Long): LongRange {
        if (value !in progression) return LongRange.EMPTY
        val trueIndex = (value - progression.first) / progression.step
        return toTargetIndex(trueIndex)..<toTargetIndex(trueIndex + 1)
    }

}
