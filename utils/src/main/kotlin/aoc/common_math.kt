package aoc

import kotlin.math.pow
import kotlin.math.sign as ktsign

// Non-negative modulo
infix fun Int.mod(n: Int): Int = (this % n).let { if (it >= 0) it else it + n }
infix fun Long.mod(n: Int): Int = (this % n).let { if (it >= 0) it else it + n }.toInt()
infix fun Long.mod(n: Long): Long = (this % n).let { if (it >= 0) it else it + n }


// Add alias for math functions to have them in scope
fun min(a: Int, b: Int): Int = kotlin.math.min(a, b)
fun min(a: Long, b: Long): Long = kotlin.math.min(a, b)
fun max(a: Int, b: Int): Int = kotlin.math.max(a, b)
fun max(a: Long, b: Long): Long = kotlin.math.max(a, b)

fun abs(a: Int): Int = kotlin.math.abs(a)
fun abs(a: Long): Long = kotlin.math.abs(a)

fun Int.sign(): Int = ktsign
fun Long.sign(): Long = ktsign.toLong()


// Power function on ints and longs
infix fun Int.pow(a: Int): Int = toDouble().pow(a.toDouble()).toInt()
infix fun Int.pow(a: Long): Long = toDouble().pow(a.toDouble()).toLong()
infix fun Long.pow(a: Int): Long = toDouble().pow(a.toDouble()).toLong()
infix fun Long.pow(a: Long): Long = toDouble().pow(a.toDouble()).toLong()


// Power function on ints and longs
infix fun Int.ceilDiv(a: Int): Int = if (this == 0) 0 else (this - 1) / a + 1
infix fun Long.ceilDiv(a: Long): Long = if (this == 0L) 0 else (this - 1) / a + 1

// product of iterables and sequences
@kotlin.jvm.JvmName("productOfInt")
fun Iterable<Int>.product(): Long = fold(1L) { a, b -> a * b }

@kotlin.jvm.JvmName("productOfLong")
fun Iterable<Long>.product(): Long = reduce { a, b -> a * b }

@kotlin.jvm.JvmName("productOfInt")
fun Sequence<Int>.product(): Long = fold(1L) { a, b -> a * b }

@kotlin.jvm.JvmName("productOfLong")
fun Sequence<Long>.product(): Long = reduce { a, b -> a * b }


/** Returns the greatest common divisor of the inputs. */
fun gcd(a: Long, b: Long): Long {
    var num1 = a
    var num2 = b
    while (num2 != 0L) {
        val temp = num2
        num2 = num1 % num2
        num1 = temp
    }
    return num1
}

fun Iterable<Long>.gcd(): Long = reduce(::gcd)


/** Returns the least common multiplier of the inputs. */
fun lcm(a: Long, b: Long): Long {
    val larger = max(a, b)
    val smaller = min(a, b)
    var lcm = larger
    while (lcm % smaller != 0L) {
        lcm += larger
    }
    return lcm
}

fun Iterable<Long>.lcm(): Long = reduce(::lcm)
