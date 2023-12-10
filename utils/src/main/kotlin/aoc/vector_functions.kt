package aoc

import kotlin.math.abs

/** Returns true of all coordinates of this vector are zero. */
fun Vector<*>.isZero(): Boolean {
    forEach { if (it != 0L) return false }
    return true
}

/** The coordinates of this vector in a list. */
val Vector<*>.coordinates: List<Long>
    get() = object : AbstractList<Long>(), RandomAccess {
        override val size: Int get() = dimensions
        override fun get(index: Int): Long = invoke(index)
    }

fun Vector<*>.sumOfCoordinates(): Long = sumOf { it }
fun Vector<*>.absSumOfCoordinates(): Long = sumOf { abs(it) }

// Basic math

/** Applies the [abs] function to all coordinates. */
fun <V : Vector<V>> V.abs(): V = map { abs(it) }

/** Applies the [Long.sign] function to all coordinates. */
fun <V : Vector<V>> V.sign(): V = map { it.sign() }

/** Multiplies this vector with -1. */
operator fun <V : Vector<V>> V.unaryMinus(): V = map { -it }

/** Adds the other vector to this one. */
operator fun <V : Vector<V>> V.plus(other: V): V = mapWith(other) { a, b -> a + b }

/** Subtracts the other vector from this one. */
operator fun <V : Vector<V>> V.minus(other: V): V = mapWith(other) { a, b -> a - b }


// Sum of multiple vectors

/** Returns the sum of these vectors. */
fun Iterable<Pos>.sum(): Pos = sumOrNull() ?: Pos.zero

/** Returns the sum of these vectors. */
fun Iterable<Xyz>.sum(): Xyz = sumOrNull() ?: Xyz.zero

/** Returns the sum of these vectors or null of the iterable is empty. */
fun <V : Vector<V>> Iterable<V>.sumOrNull(): V? = reduceOrNull { a, b -> a + b }

/** Returns the sum of these vectors. */
fun Sequence<Pos>.sum(): Pos = sumOrNull() ?: Pos.zero

/** Returns the sum of these vectors. */
fun Sequence<Xyz>.sum(): Xyz = sumOrNull() ?: Xyz.zero

/** Returns the sum of these vectors or null of the sequence is empty. */
fun <V : Vector<V>> Sequence<V>.sumOrNull(): V? = reduceOrNull { a, b -> a + b }


// Multiplication

/** Calculates the scalar product of this vector and [other]. */
operator fun <V : Vector<V>> V.times(other: V): Long = sumOfWith(other) { a, b -> a * b }

/** Multiplies each coordinate with [num]. */
operator fun <V : Vector<V>> V.times(num: Long): V = map { it * num }

/** Multiplies each coordinate with [num]. */
operator fun <V : Vector<V>> V.times(num: Int): V = this * num.toLong()

/** Multiplies each coordinate of [vector] with this number. */
operator fun <V : Vector<V>> Long.times(vector: V): V = vector * this

/** Multiplies each coordinate of [vector] with this number. */
operator fun <V : Vector<V>> Int.times(vector: V): V = vector * this.toLong()


// Distance

/** Returns the Manhattan distance of this vector and [other]. */
infix fun <V : Vector<V>> V.mdist(other: V): Long = sumOfWith(other) { a, b -> abs(a - b) }


// Division

/**
 * Returns how many times the other vector should be multiplied to get this one,
 * or null if there is not a single such multiplier.
 */
operator fun <V : Vector<V>> V.div(other: V): Long? {
    var result: Long? = null
    forEachWith(other) { a, b ->
        if (b == 0L) {
            if (a == 0L) return@forEachWith // any multiplier is OK
            else return null // cannot succeed
        }
        val d = a / b
        when (result) {
            null -> result = d // no expected result yet, we update it
            d -> {} // we found the same multiplier, we are good
            else -> return null // at least 2 different multipliers => we cannot succeed
        }
    }
    return result ?: 1 // result can only be null here when (0, 0, 0) / (0, 0, 0)
}


// Higher-order functions on coordinates

/** Transforms each coordinate of this vector using the given function. */
inline fun <V : Vector<V>> V.map(f: (Long) -> Long): V = copyWith({ 0 }, { a, _ -> f(a) })

/** Transforms each coordinate of this vector using the given function, then returns the sum of the results. */
inline fun Vector<*>.sumOf(f: (Long) -> Long): Long = reduce(f) { a, b -> a + b }

/** Runs the given function for each coordinate. */
inline fun Vector<*>.forEach(f: (Long) -> Unit) = reduce(f) { _, _ -> }

/** Performs a reduction operation on the coordinates of this vector. */
inline fun <T> Vector<*>.reduce(f: (Long) -> T, g: (T, T) -> T): T = doReduceWith({ 0 }, { a, _ -> f(a) }, g)


// Higher-order functions on coordinates with another vector

/** Combines each coordinate of this vector and [o] using the given function. */
inline fun <V : Vector<V>> V.mapWith(o: V, f: (Long, Long) -> Long): V = copyWith(o, f)

/** Combines each coordinate of this vector and [o] using the given function, then returns the sum of the results. */
inline fun <V : Vector<V>> V.sumOfWith(o: V, f: (Long, Long) -> Long): Long = reduceWith(o, f) { a, b -> a + b }

/** Runs the given function for each coordinate in this vector and [o]. */
inline fun <V : Vector<V>> V.forEachWith(o: V, f: (Long, Long) -> Unit) = reduceWith(o, f) { _, _ -> }

/** Performs a reduction operation on the coordinates of this vector and [o]. */
inline fun <V : Vector<V>, T> V.reduceWith(o: V, f: (Long, Long) -> T, g: (T, T) -> T): T = doReduceWith(o, f, g)


// ===== Internal helper methods =====

@PublishedApi
internal inline fun <T> Vector<*>.doReduceWith(
    other: (Int) -> Long,
    f: (Long, Long) -> T,
    g: (T, T) -> T,
): T = when (this) {
    is Pos ->
        g(
            f(x, other(0)),
            f(y, other(1))
        )

    is Xyz ->
        g(
            g(
                f(x, other(0)),
                f(y, other(1))
            ),
            f(z, other(2))
        )
}

@PublishedApi
@Suppress("UNCHECKED_CAST")
internal inline fun <V : Vector<V>> V.copyWith(other: (Int) -> Long, f: (Long, Long) -> Long): V =
    when (val self = this as Vector<*>) {
        is Xyz -> Xyz(f(self.x, other(0)), f(self.y, other(1)), f(self.z, other(2)))
        is Pos -> Pos(f(self.x, other(0)), f(self.y, other(1)))
    } as V
