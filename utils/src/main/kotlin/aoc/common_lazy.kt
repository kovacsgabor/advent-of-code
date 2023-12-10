package aoc


/**
 * A wrapper around an iterator that lazily caches each returned value, so it can be iterated repeatedly,
 * but without the need to consume the iterator upfront. Useful when handling very big (potentially infinite) sequences.
 */
open class CachedIterable<T>(private var underlyingIterator: Iterator<T>?) : Iterable<T> {
    constructor(iterable: Iterable<T>) : this(iterable.iterator())
    constructor(sequence: Sequence<T>) : this(sequence.iterator())

    private val cache = mutableListOf<T>()

    /** Returns true if the underlying iterator contains at least [n] elements, false otherwise.  */
    fun hasSizeAtLeast(n: Int): Boolean = increaseCacheSizeTo(n)

    /** Returns an element by its index from the underlying iterator. */
    operator fun get(index: Int): T {
        if (!increaseCacheSizeTo(index + 1)) throw NoSuchElementException()
        return cache[index]
    }

    private fun increaseCacheSizeTo(size: Int): Boolean {
        repeat(size - cache.size) {
            if (iteratorHasNext()) cache += underlyingIterator!!.next()
            else return false
        }
        return true
    }

    override fun iterator(): Iterator<T> =
        object : Iterator<T> {
            private var idx = 0
            override fun hasNext(): Boolean = increaseCacheSizeTo(idx + 1)
            override fun next(): T = get(idx++)
        }

    private fun iteratorHasNext(): Boolean {
        val it = underlyingIterator
        return if (it != null && it.hasNext()) {
            true
        } else {
            underlyingIterator = null // release the original iterator
            false
        }
    }

}

/** Creates a new [CachedIterable] that transforms each element in this iterable using the given function. */
fun <T, R> Iterable<T>.mapLazily(f: T.() -> R): Iterable<R> = CachedIterable(object : Iterable<R> {
    override fun iterator(): Iterator<R> {
        val it = this@mapLazily.iterator()
        return object : Iterator<R> {
            override fun hasNext(): Boolean = it.hasNext()
            override fun next(): R = f(it.next())
        }
    }
})
