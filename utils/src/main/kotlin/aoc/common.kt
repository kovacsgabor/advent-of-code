package aoc

import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayDeque

// Add aliases to some basic types and functions to shorten their names and/or bring them in scope
typealias MList<T> = MutableList<T>
typealias MSet<T> = MutableSet<T>
typealias MMap<K, V> = MutableMap<K, V>
typealias BitSet = java.util.BitSet

fun Long.toBig(): BigInteger = toBigInteger()

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

fun <T : Comparable<T>> Sequence<T>.min(): T = minOf { it }
fun <T : Comparable<T>> Sequence<T>.max(): T = maxOf { it }
fun <T : Comparable<T>> Sequence<T>.bounds(): Pair<T, T> = min() to max()


/** Returns true if this iterable only contains different elements. */
fun <T : Any?> Iterable<T>.isDistinct(): Boolean = isDistinctBy { it }

/** Returns true if this iterable only contains different elements. */
fun <T : Any?> Sequence<T>.isDistinct(): Boolean = isDistinctBy { it }

/** Returns true if the results produced by [function] on the elements of this iterable are all different. */
fun <T : Any?, U : Any?> Iterable<T>.isDistinctBy(function: (T) -> U): Boolean = iterator().isDistinctBy(function)

/** Returns true if the results produced by [function] on the elements of this sequence are all different. */
fun <T : Any?, U : Any?> Sequence<T>.isDistinctBy(function: (T) -> U): Boolean = iterator().isDistinctBy(function)

/** Returns true if this iterator only contains different elements. Consumes the iterator. */
private fun <T, U : Any?> Iterator<T>.isDistinctBy(function: (T) -> U): Boolean {
    val set = mutableSetOf<U>()
    for (e in this) {
        if (!set.add(function(e))) return false
    }
    return true
}
