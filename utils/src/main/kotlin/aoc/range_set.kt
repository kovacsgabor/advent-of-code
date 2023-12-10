package aoc

/**
 * A set of [LongRange]s.
 * The ranges in this set are always sorted and overlapping ranges are merged, so each set has a unique representation.
 */
@JvmInline
value class LongRangeSet private constructor(val ranges: List<LongRange>) {

    companion object {
        private val EMPTY = LongRangeSet(listOf())
        private val COMPARATOR = Comparator.comparingLong<LongRange> { it.first }.thenComparingLong { it.last }

        operator fun invoke(): LongRangeSet = EMPTY
        operator fun invoke(range: LongRange): LongRangeSet =
            if (range.isEmpty()) EMPTY
            else LongRangeSet(listOf(range))

        operator fun invoke(ranges: Iterable<LongRange>): LongRangeSet {
            val filtered = ranges.filterNotTo(mutableListOf()) { it.isEmpty() }
            if (filtered.isEmpty()) {
                return EMPTY
            }
            if (filtered.size == 1) {
                return LongRangeSet(filtered.first())
            }

            filtered.sortWith(COMPARATOR) // sort ranges

            // Merge ranges if possible
            val result = mutableListOf<LongRange>()
            val iterator = filtered.iterator()
            var prev = iterator.next()
            iterator.forEachRemaining {
                if (prev.last < it.first - 1) { // cannot merge
                    result += prev
                    prev = it
                } else {
                    prev = prev.first..it.last
                }
            }
            result += prev
            return LongRangeSet(result)
        }
    }

}


fun rangeSetClosed(from: Int, to: Int): LongRangeSet = rangeSetClosed(from.toLong(), to.toLong())
fun rangeSetClosed(from: Long, to: Long): LongRangeSet = LongRangeSet(from..to)

fun rangeSetOpen(from: Int, to: Int): LongRangeSet = rangeSetClosed(from.toLong(), to.toLong())
fun rangeSetOpen(from: Long, to: Long): LongRangeSet = LongRangeSet(from..<to)

fun emptyRangeSet(): LongRangeSet = LongRangeSet()

fun rangeSetOf(vararg longs: Long): LongRangeSet = LongRangeSet(longs.map { it..it })
fun rangeSetOf(vararg ranges: LongRange): LongRangeSet = LongRangeSet(ranges.asIterable())

fun Long.toRangeSet(): LongRangeSet = LongRangeSet(this..this)
fun IntRange.toRangeSet(): LongRangeSet = LongRangeSet(toLong())
fun LongRange.toRangeSet(): LongRangeSet = LongRangeSet(this)
fun Iterable<LongRange>.toRangeSet(): LongRangeSet = LongRangeSet(this)

fun LongRangeSet.min() = ranges.first().first
fun LongRangeSet.max() = ranges.last().last

fun LongRangeSet.isEmpty() = ranges.isEmpty()
fun LongRangeSet.isNotEmpty() = ranges.isNotEmpty()

/** Calculates the union of this and [other]. */
operator fun LongRangeSet.plus(other: LongRangeSet): LongRangeSet =
    when {
        isEmpty() -> other
        other.isEmpty() -> this
        else -> LongRangeSet(ranges + other.ranges)
    }

/** Calculates the difference of this and [other]. */
operator fun LongRangeSet.minus(other: LongRangeSet): LongRangeSet {
    if (isEmpty() || other.isEmpty()) return this
    var ranges = this.ranges
    for (r in other.ranges) ranges = ranges.flatMap { it - r }
    return LongRangeSet(ranges)
}

/** Calculates the intersection of this and [other]. */
infix fun LongRangeSet.intersect(other: LongRangeSet): LongRangeSet =
    if (isEmpty() || other.isEmpty()) emptyRangeSet()
    else LongRangeSet(ranges.flatMap { a -> other.ranges.map { b -> a intersectRange b } })

fun LongRangeSet.shift(value: Int): LongRangeSet = shift(value.toLong())
fun LongRangeSet.shift(value: Long): LongRangeSet = LongRangeSet(ranges.map { it.shift(value) })

