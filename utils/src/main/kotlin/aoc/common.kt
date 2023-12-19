package aoc

import java.util.Collections

// Add aliases to some basic types and functions to shorten their names and/or bring them in scope
typealias MList<T> = MutableList<T>
typealias MSet<T> = MutableSet<T>
typealias MMap<K, V> = MutableMap<K, V>
typealias BitSet = java.util.BitSet

fun <T> Iterable<T>.toMList(): MList<T> = toMutableList()
fun <T> Iterable<T>.toMSet(): MSet<T> = toMutableSet()
fun <K, V> Map<K, V>.toMMap(): MMap<K, V> = toMutableMap()

fun <T> mutableDequeOf(vararg values: T): ArrayDeque<T> = ArrayDeque(values.asList())
fun <T> nCopies(n : Int, value: T): List<T> = Collections.nCopies(n, value)


// Getter and setter for lists using a long index
operator fun <T> List<T>.get(index: Long): T = get(index.toInt())
operator fun <T> MList<T>.set(index: Long, value: T): T = set(index.toInt(), value)


/** Creates a copy of the given list with the value at [index] replaced with [newValue] */
fun <T> List<T>.with(index: Int, newValue: T): List<T> {
    val copy = toMList()
    copy[index] = newValue
    return copy
}

/** Creates a copy of the given list with the value at [index] replaced with [newValue] */
fun <T> List<T>.with(index: Long, newValue: T): List<T> = with(index.toInt(), newValue)


// Character-related functions on strings that treat each character as a String.
// (I do not like working with the Character type, so I always use String instead.)
fun String.charAt(index: Int): String = "" + this[index]
fun String.charAt(index: Long): String = "" + this[index.toInt()]

fun String.toChars(): MList<String> = this.indices.map { charAt(it) }.toMList()
fun String.toCharSet(): MSet<String> = this.indices.map { charAt(it) }.toMSet()


// min and max of iterables and sequences
fun <T : Comparable<T>> Iterable<T>.min(): T = minOf { it }
fun <T : Comparable<T>> Iterable<T>.max(): T = maxOf { it }
fun <T : Comparable<T>> Iterable<T>.bounds(): Pair<T, T> = min() to max()

fun <T : Comparable<T>> Sequence<T>.min() = minOf { it }
fun <T : Comparable<T>> Sequence<T>.max() = maxOf { it }
fun <T : Comparable<T>> Sequence<T>.bounds(): Pair<T, T> = min() to max()
