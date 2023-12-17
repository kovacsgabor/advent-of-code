package aoc

import kotlin.math.sqrt

private const val minSieveSize = 10_000L
private const val maxSieveSize = 100_000_000L

private var primesKnownBelow = 3L
private val knownPrimes = mutableListOf<Long>()
private val nextValues = mutableListOf<Long>()


/** Returns true if the given number is a prime. */
fun isPrime(a: Int): Boolean = isPrime(a.toLong())

/** Returns true if the given number is a prime. */
fun isPrime(a: Long): Boolean {
    if (a < 0) throw IllegalArgumentException("Negative: $a")
    if (a == 2L) return true
    if (a == 0L || a == 1L || a % 2 == 0L) return false

    // Fast path: binary search on the list of known primes
    if (a < primesKnownBelow + 2 * maxSieveSize) {
        fillKnownPrimesUntil(a)
        return knownPrimes.binarySearch(a) >= 0
    }

    // Target is too big: check divisibility for all known primes below the root
    val root = (sqrt(a.toDouble()) + 1).toLong()
    for (p in knownPrimes) {
        if (p > root) return true
        if (a % p == 0L) return false
    }

    // Increase the known prime pool as necessary and continue
    val alreadyCheckedBelowIndex = knownPrimes.size
    fillKnownPrimesUntil(root)
    for (p in knownPrimes.subList(alreadyCheckedBelowIndex, knownPrimes.size)) {
        if (p > root) return true
        if (a % p == 0L) return false
    }
    return true
}

// Used in tests
internal fun clearKnownPrimeLists() {
    primesKnownBelow = 3
    knownPrimes.clear()
    nextValues.clear()
}

private fun fillKnownPrimesUntil(target: Long) {
    var sieveSize = (target - primesKnownBelow) / 2 + 1
    while (sieveSize > 0) {
        val nextSize = sieveSize.coerceIn(minSieveSize, maxSieveSize)
        nextSieve(nextSize.toInt())
        sieveSize -= nextSize
    }
}

private fun nextSieve(sieveSize: Int) {
    val from = primesKnownBelow
    val to = from + sieveSize * 2

    // The current sieve: odd numbers between "from" (inclusive) and "to" (exclusive)
    val bitSet = BitSet(sieveSize)

    fun toBitSetIndex(a: Long): Int = ((a - from) / 2).toInt()
    fun fromBitSetIndex(a: Int): Long = a.toLong() * 2 + from

    fun markComposites(knownPrimeIndex: Int) {
        var next = nextValues[knownPrimeIndex]
        if (next >= to) return

        val prime = knownPrimes[knownPrimeIndex]
        while (next < to) {
            bitSet.set(toBitSetIndex(next))
            next += prime * 2
        }
        nextValues[knownPrimeIndex] = next
    }

    // Mark composites based on the already known primes
    for (i in knownPrimes.indices) markComposites(i)

    // Add newly found primes and mark composites based on them
    var nextPrimeIndex = bitSet.nextClearBit(0)
    while (nextPrimeIndex < sieveSize) {
        val nextPrime = fromBitSetIndex(nextPrimeIndex)
        knownPrimes += nextPrime
        nextValues += nextPrime
        markComposites(knownPrimes.lastIndex)
        nextPrimeIndex = bitSet.nextClearBit(nextPrimeIndex + 1)
    }

    primesKnownBelow = to
}
