package aoc

import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Function

/**
 * "Unsafe" map whose [get] method returns a non-nullable value, so when it is known that the key is in the map
 * (which is the usual case when solving aoc puzzles), !! can be omitted.
 * Also supports an optional default value for cases when that is practical.
 */
data class UMap<K, V>(val default: V?, val m: MMap<K, V>) : MMap<K, V> by m {
    override fun get(key: K): V = if (default == null) m[key]!! else m.getOrDefault(key, default)

    // Default methods of java.util.Map must be overridden here, otherwise they would call the get method above
    // instead of the method of m (which can lead to NullPointerException if the default is null)

    override fun getOrDefault(key: K, defaultValue: V): V = m.getOrDefault(key, defaultValue)
    override fun putIfAbsent(key: K, value: V): V? = m.putIfAbsent(key, value)
    override fun forEach(action: BiConsumer<in K, in V>) = m.forEach(action)
    override fun replaceAll(function: BiFunction<in K, in V, out V>) = m.replaceAll(function)
    override fun replace(key: K, value: V): V? = m.replace(key, value)
    override fun replace(key: K, oldValue: V, newValue: V): Boolean = m.replace(key, oldValue, newValue)
    override fun remove(key: K, value: V): Boolean = m.remove(key, value)
    override fun compute(key: K, f: BiFunction<in K, in V?, out V?>): V? = m.compute(key, f)
    override fun computeIfAbsent(key: K, f: Function<in K, out V>): V = m.computeIfAbsent(key, f)
    override fun computeIfPresent(key: K, f: BiFunction<in K, in V & Any, out V?>): V? = m.computeIfPresent(key, f)
    override fun merge(key: K, v: V & Any, f: BiFunction<in V & Any, in V & Any, out V?>): V? = m.merge(key, v, f)

}

/** Converts this map to a [UMap]. */
fun <K, V> Map<K, V>.toUMap(default: V? = null) =
    UMap(if (default == null && this is UMap<K, V>) this.default else default, toMMap())

/** Converts this list of paris to a [UMap]. */
fun <K, V> Iterable<Pair<K, V>>.toUMap(default: V? = null): UMap<K, V> = UMap(default, toMap(mutableMapOf()))

/** Converts this list of paris to a [UMap]. */
fun <K, V> Sequence<Pair<K, V>>.toUMap(default: V? = null): UMap<K, V> = UMap(default, toMap(mutableMapOf()))

/** Creates an empty [UMap] with the given default value. */
fun <K, V> umapOf(default: V? = null): UMap<K, V> = UMap(default, mutableMapOf())

/** Creates a [UMap] with no default value, prefilled with the given data. */
fun <K, V> umapOf(vararg pairs: Pair<K, V>): UMap<K, V> = UMap(null, mutableMapOf(*pairs))

/** Creates a [UMap] with the given default value, prefilled with the given data. */
fun <K, V> umapOf(default: V? = null, vararg pairs: Pair<K, V>): UMap<K, V> = UMap(default, mutableMapOf(*pairs))
