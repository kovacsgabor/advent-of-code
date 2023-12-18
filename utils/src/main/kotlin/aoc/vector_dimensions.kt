package aoc

// Pattern matching support
operator fun PosOrXyz.component1(): Long = x
operator fun PosOrXyz.component2(): Long = y
operator fun Xyz.component3(): Long = z


/** Returns the lazily calculated projection of this iterable to the [x] axis. */
val Iterable<PosOrXyz>.x: Iterable<Long> get() = mapLazily { x }

/** Returns the lazily calculated projection of this iterable to the [y] axis. */
val Iterable<PosOrXyz>.y: Iterable<Long> get() = mapLazily { y }

/** Returns the lazily calculated projection of this iterable to the [z] axis. */
val Iterable<Xyz>.z: Iterable<Long> get() = mapLazily { z }


/** Returns the lazily calculated projection of the keys of this map to the [x] axis. */
val <V : PosOrXyz> Map<V, *>.x: Iterable<Long> get() = keys.x

/** Returns the lazily calculated projection of the keys of this map to the [y] axis. */
val <V : PosOrXyz> Map<V, *>.y: Iterable<Long> get() = keys.y

/** Returns the lazily calculated projection of the keys of this map to the [z] axis. */
val <V : Xyz> Map<V, *>.z: Iterable<Long> get() = keys.z


/** Calculates the lower bound of this iterable: a vector with minimal [x] and [y] values. */
fun Iterable<Pos>.minPos() = Pos(x.min(), y.min())

/** Calculates the lower bound of this iterable: a vector with minimal [x], [y] and [z] values. */
fun Iterable<Xyz>.minXyz() = Xyz(x.min(), y.min(), z.min())

/** Calculates the lower bound of the keys: a vector with minimal [x] and [y] values. */
fun Map<Pos, *>.minPos() = keys.minPos()

/** Calculates the lower bound of the keys: a vector with minimal [x], [y] and [z] values. */
fun Map<Xyz, *>.minXyz() = keys.minXyz()


/** Calculates the upper bound of this iterable: a vector with maximal [x] and [y] values. */
fun Iterable<Pos>.maxPos() = Pos(x.max(), y.max())

/** Calculates the upper bound of this iterable: a vector with maximal [x], [y] and [z] values. */
fun Iterable<Xyz>.maxXyz() = Xyz(x.max(), y.max(), z.max())

/** Calculates the upper bound of the keys: a vector with maximal [x] and [y] values. */
fun <V> Map<Pos, V>.maxPos() = keys.maxPos()

/** Calculates the upper bound of the keys: a vector with maximal [x], [y] and [z] values. */
fun <V> Map<Xyz, V>.maxXyz() = keys.maxXyz()
