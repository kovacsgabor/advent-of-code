@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package aoc

/** A vector with at least 2 dimensions. */
sealed interface Vector2OrMore {
    val x: Long
    val y: Long
}

/** A vector with at least 3 dimensions. */
sealed interface Vector3OrMore : Vector2OrMore {
    val z: Long
}

// Pattern matching support
operator fun Vector2OrMore.component1(): Long = x
operator fun Vector2OrMore.component2(): Long = y
operator fun Vector3OrMore.component3(): Long = z


/** Returns the lazily calculated projection of this iterable to the [x] axis. */
val Iterable<Vector2OrMore>.x: Iterable<Long> get() = mapLazily { x }

/** Returns the lazily calculated projection of this iterable to the [y] axis. */
val Iterable<Vector2OrMore>.y: Iterable<Long> get() = mapLazily { y }

/** Returns the lazily calculated projection of this iterable to the [z] axis. */
val Iterable<Vector3OrMore>.z: Iterable<Long> get() = mapLazily { z }


/** Returns the lazily calculated projection of the keys of this map to the [x] axis. */
val <V : Vector2OrMore, T> Map<V, T>.x: Iterable<Long> get() = keys.x

/** Returns the lazily calculated projection of the keys of this map to the [y] axis. */
val <V : Vector2OrMore, T> Map<V, T>.y: Iterable<Long> get() = keys.y

/** Returns the lazily calculated projection of the keys of this map to the [z] axis. */
val <V : Vector3OrMore, T> Map<V, T>.z: Iterable<Long> get() = keys.z


/** Calculates the lower bound of this iterable: a vector with minimal [x] and [y] values. */
fun Iterable<Pos>.minPos() = Pos(x.min(), y.min())

/** Calculates the lower bound of this iterable: a vector with minimal [x], [y] and [z] values. */
fun Iterable<Xyz>.minXyz() = Xyz(x.min(), y.min(), z.min())

/** Calculates the lower bound of the keys: a vector with minimal [x] and [y] values. */
fun <V> Map<Pos, V>.minPos() = keys.minPos()

/** Calculates the lower bound of the keys: a vector with minimal [x], [y] and [z] values. */
fun <V> Map<Xyz, V>.minXyz() = keys.minXyz()


/** Calculates the upper bound of this iterable: a vector with maximal [x] and [y] values. */
fun Iterable<Pos>.maxPos() = Pos(x.max(), y.max())

/** Calculates the upper bound of this iterable: a vector with maximal [x], [y] and [z] values. */
fun Iterable<Xyz>.maxXyz() = Xyz(x.max(), y.max(), z.max())

/** Calculates the upper bound of the keys: a vector with maximal [x] and [y] values. */
fun <V> Map<Pos, V>.maxPos() = keys.maxPos()

/** Calculates the upper bound of the keys: a vector with maximal [x], [y] and [z] values. */
fun <V> Map<Xyz, V>.maxXyz() = keys.maxXyz()


/**
 * Calculates the ranges of [x] and [y] that cover all elements in this iterable,
 * then increases these ranges in both directions with the given amount,
 * and lists all vectors in these increased ranges.
 */
@kotlin.jvm.JvmName("widenPos")
fun Iterable<Pos>.widen(amount: Long) =
    x.range().widen(amount).flatMap { x ->
        y.range().widen(amount).map { y ->
            Pos(x, y)
        }
    }

/**
 * Calculates the ranges of [x], [y] and [z] that cover all elements in this iterable,
 * then increases these ranges in both directions with the given amount,
 * and lists all vectors in these increased ranges.
 */
@kotlin.jvm.JvmName("widenXyz")
fun Iterable<Xyz>.widen(amount: Long): Iterable<Xyz> =
    x.range().widen(amount).flatMap { x ->
        y.range().widen(amount).flatMap { y ->
            z.range().widen(amount).map { z ->
                Xyz(x, y, z)
            }
        }
    }

