package aoc

import kotlin.math.abs
import kotlin.time.measureTime


// Basic math

/** Returns true of all coordinates of this vector are zero. */
fun Pos.isZero(): Boolean = x == 0L && y == 0L

/** Returns true of all coordinates of this vector are zero. */
fun Xyz.isZero(): Boolean = x == 0L && y == 0L && z == 0L

/** Applies the [abs] function to all coordinates. */
fun Pos.abs(): Pos = map { abs(it) }

/** Applies the [abs] function to all coordinates. */
fun Xyz.abs(): Xyz = map { abs(it) }

/** Applies the [Long.sign] function to all coordinates. */
fun Pos.sign(): Pos = map { it.sign() }

/** Applies the [Long.sign] function to all coordinates. */
fun Xyz.sign(): Xyz = map { it.sign() }

/** Multiplies this vector with -1. */
operator fun Pos.unaryMinus(): Pos = map(Long::unaryMinus)

/** Multiplies this vector with -1. */
operator fun Xyz.unaryMinus(): Xyz = map(Long::unaryMinus)

/** Adds the other vector to this one. */
operator fun Pos.plus(other: Pos): Pos = mapWith(other, Long::plus)

/** Adds the other vector to this one. */
operator fun Xyz.plus(other: Xyz): Xyz = mapWith(other, Long::plus)

/** Subtracts the other vector from this one. */
operator fun Pos.minus(other: Pos): Pos = mapWith(other, Long::minus)

/** Subtracts the other vector from this one. */
operator fun Xyz.minus(other: Xyz): Xyz = mapWith(other, Long::minus)


// Sum of multiple vectors

/** Returns the sum of these vectors. */
fun Iterable<Pos>.sum(): Pos = reduceOrNull(Pos::plus) ?: Pos.zero

/** Returns the sum of these vectors. */
fun Iterable<Xyz>.sum(): Xyz = reduceOrNull(Xyz::plus) ?: Xyz.zero

/** Returns the sum of these vectors. */
fun Sequence<Pos>.sum(): Pos = reduceOrNull(Pos::plus) ?: Pos.zero

/** Returns the sum of these vectors. */
fun Sequence<Xyz>.sum(): Xyz = reduceOrNull(Xyz::plus) ?: Xyz.zero


// Sum of coordinates

fun Pos.sumOfCoordinates(): Long = sumOf { it }
fun Xyz.sumOfCoordinates(): Long = sumOf { it }

fun Pos.absSumOfCoordinates(): Long = sumOf { abs(it) }
fun Xyz.absSumOfCoordinates(): Long = sumOf { abs(it) }


// Multiplication

/** Calculates the cross product of this vector and [other]. */
infix fun Xyz.crossProduct(other: Xyz): Xyz =
    Xyz(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x,
    )

/** Calculates the scalar product of this vector and [other]. */
operator fun Pos.times(other: Pos): Long = reduceWith(other, Long::times, Long::plus)

/** Calculates the scalar product of this vector and [other]. */
operator fun Xyz.times(other: Xyz): Long = reduceWith(other, Long::times, Long::plus)

/** Multiplies each coordinate with [num]. */
operator fun Pos.times(num: Long): Pos = map { it * num }

/** Multiplies each coordinate with [num]. */
operator fun Xyz.times(num: Long): Xyz = map { it * num }

/** Multiplies each coordinate with [num]. */
operator fun Pos.times(num: Int): Pos = this * num.toLong()

/** Multiplies each coordinate with [num]. */
operator fun Xyz.times(num: Int): Xyz = this * num.toLong()

/** Multiplies each coordinate of [vector] with this number. */
operator fun Long.times(vector: Pos): Pos = vector * this

/** Multiplies each coordinate of [vector] with this number. */
operator fun Long.times(vector: Xyz): Xyz = vector * this

/** Multiplies each coordinate of [vector] with this number. */
operator fun Int.times(vector: Pos): Pos = vector * toLong()

/** Multiplies each coordinate of [vector] with this number. */
operator fun Int.times(vector: Xyz): Xyz = vector * toLong()


// Distance

/** Returns the Manhattan distance of this vector and [other]. */
infix fun Pos.mdist(other: Pos): Long = abs(x - other.x) + abs(y - other.y)

/** Returns the Manhattan distance of this vector and [other]. */
infix fun Xyz.mdist(other: Xyz): Long = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)


// Division

/**
 * Returns how many times the other vector should be multiplied to get this one,
 * or null if there is not a single such multiplier.
 */
operator fun Pos.div(o: Pos): Long? = Divisor.AnyCanDivide.update(x, o.x).update(y, o.y).toLong()

/**
 * Returns how many times the other vector should be multiplied to get this one,
 * or null if there is not a single such multiplier.
 */
operator fun Xyz.div(o: Xyz): Long? = Divisor.AnyCanDivide.update(x, o.x).update(y, o.y).update(z, o.z).toLong()

private fun Divisor.update(a: Long, b: Long): Divisor {
    if (b == 0L) return if (a == 0L) this else Divisor.NoneCanDivide
    val newDivisor = Divisor.CanDivideWith(a / b)
    return when (this) {
        Divisor.AnyCanDivide, newDivisor -> newDivisor // no expected divisor yet, or the same expected divisor
        else -> Divisor.NoneCanDivide
    }
}

private fun Divisor.toLong(): Long? = when (this) {
    is Divisor.CanDivideWith -> v
    Divisor.AnyCanDivide -> 1
    Divisor.NoneCanDivide -> null
}

private sealed interface Divisor {
    data class CanDivideWith(val v: Long) : Divisor
    data object AnyCanDivide : Divisor
    data object NoneCanDivide : Divisor
}


// Higher-order functions on coordinates

/** Transforms each coordinate of this vector using the given function. */
inline fun Pos.map(f: (Long) -> Long): Pos = Pos(f(x), f(y))

/** Transforms each coordinate of this vector using the given function. */
inline fun Xyz.map(f: (Long) -> Long): Xyz = Xyz(f(x), f(y), f(z))

/** Combines each coordinate of this vector and [o] using the given function. */
inline fun Pos.mapWith(o: Pos, f: (Long, Long) -> Long): Pos = Pos(f(x, o.x), f(y, o.y))

/** Combines each coordinate of this vector and [o] using the given function. */
inline fun Xyz.mapWith(o: Xyz, f: (Long, Long) -> Long): Xyz = Xyz(f(x, o.x), f(y, o.y), f(z, o.z))


/** Runs the given function for each coordinate. */
inline fun Pos.forEach(f: (Long) -> Unit) = reduce(f) { _, _ -> }

/** Runs the given function for each coordinate. */
inline fun Xyz.forEach(f: (Long) -> Unit) = reduce(f) { _, _ -> }

/** Runs the given function for each coordinate in this vector and [o]. */
inline fun Pos.forEachWith(o: Pos, f: (Long, Long) -> Unit) = reduceWith(o, f) { _, _ -> }

/** Runs the given function for each coordinate in this vector and [o]. */
inline fun Xyz.forEachWith(o: Xyz, f: (Long, Long) -> Unit) = reduceWith(o, f) { _, _ -> }


/** Performs a reduction operation on the coordinates of this vector. */
inline fun <T> Pos.reduce(f: (Long) -> T, g: (T, T) -> T): T = g(f(x), f(y))

/** Performs a reduction operation on the coordinates of this vector. */
inline fun <T> Xyz.reduce(f: (Long) -> T, g: (T, T) -> T): T = g(g(f(x), f(y)), f(z))

/** Performs a reduction operation on the coordinates of this vector and [o]. */
inline fun <T> Pos.reduceWith(o: Pos, f: (Long, Long) -> T, g: (T, T) -> T): T = g(f(x, o.x), f(y, o.y))

/** Performs a reduction operation on the coordinates of this vector and [o]. */
inline fun <T> Xyz.reduceWith(o: Xyz, f: (Long, Long) -> T, g: (T, T) -> T): T = g(g(f(x, o.x), f(y, o.y)), f(z, o.z))


/** Transforms each coordinate of this vector using the given function, then returns the sum of the results. */
inline fun Pos.sumOf(f: (Long) -> Long): Long = reduce(f, Long::plus)

/** Transforms each coordinate of this vector using the given function, then returns the sum of the results. */
inline fun Xyz.sumOf(f: (Long) -> Long): Long = reduce(f, Long::plus)
