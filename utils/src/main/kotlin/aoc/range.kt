package aoc

import kotlin.math.max
import kotlin.math.min

infix fun Int.upToOrFrom(other: Int): IntRange = if (this <= other) this..other else other..this
infix fun Long.upToOrFrom(other: Long): LongRange = if (this <= other) this..other else other..this

fun IntRange.isNotEmpty() = !isEmpty()
fun LongRange.isNotEmpty() = !isEmpty()

val IntRange.size get() = last - first + 1
val LongRange.size get() = last - first + 1

fun IntRange.toLong() = first.toLong()..last.toLong()
fun LongRange.toInt() = first.toInt()..last.toInt()


/** Returns the (possibly empty) range that is in both this and [other]. */
infix fun IntRange.intersectRange(other: IntRange) = max(first, other.first)..min(last, other.last)

/** Returns the (possibly empty) range that is in both this and [other]. */
infix fun LongRange.intersectRange(other: LongRange) = max(first, other.first)..min(last, other.last)


/** Returns the non-empty sub-ranges obtained after removing [other] from this range. */
operator fun IntRange.minus(other: IntRange): List<IntRange> =
    (toLong() - other.toLong()).map { it.toInt() }

/** Returns the non-empty sub-ranges obtained after removing [other] from this range. */
operator fun LongRange.minus(other: LongRange): List<LongRange> {
    val intersection = this intersectRange other
    if (intersection.isEmpty()) {
        return if (isEmpty()) listOf() else listOf(this)
    }
    return listOf(first..<intersection.first, (intersection.last + 1)..last).filterNot { it.isEmpty() }
}


/** Shifts this range by [value]. */
fun IntRange.shift(value: Int): IntRange = (first + value)..(last + value)

/** Shifts this range by [value] towards positive infinity. */
fun LongRange.shift(value: Long): LongRange = (first + value)..(last + value)


/** Returns whether this range covers [other]. */
operator fun IntRange.contains(other: IntRange) = other.isEmpty() || (other.first >= first && other.last <= last)

/** Returns whether this range covers [other]. */
operator fun LongRange.contains(other: LongRange) = other.isEmpty() || (other.first >= first && other.last <= last)


/** Increases the size of the range in both directions by [amount]. */
fun IntRange.widen(amount: Int) = first - amount..last + amount

/** Increases the size of the range in both directions by [amount]. */
fun LongRange.widen(amount: Int) = first - amount..last + amount

/** Increases the size of the range in both directions by [amount]. */
fun LongRange.widen(amount: Long) = first - amount..last + amount


/** Creates a range that covers all elements. */
fun Iterable<Int>.range(): IntRange = min()..max()

/** Creates a range that covers all elements. */
fun Iterable<Long>.range(): LongRange = min()..max()
