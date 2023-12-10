package aoc

/** A map to count some values, with 0 as a default value. */
data class CountMap<K>(val m: MMap<K, Long>) : MMap<K, Long> by m {
    override fun get(key: K): Long = m.getOrDefault(key, 0L)

    override fun put(key: K, value: Long): Long? = if (value != 0L) m.put(key, value) else m.remove(key)
}

/** Converts this map to a [CountMap]. */
fun <K> Map<K, Long>.toCountMap() = CountMap(toMMap())

/** Converts this list of pairs to a [CountMap]. */
fun <K> Iterable<Pair<K, Long>>.toCountMap(): CountMap<K> = CountMap(toMap(mutableMapOf()))

/** Creates an empty [CountMap]. */
fun <K> countMapOf(): CountMap<K> = CountMap(mutableMapOf())

/** Creates a [CountMap] from the given pairs. */
fun <K> countMapOf(vararg pairs: Pair<K, Long>): CountMap<K> = CountMap(mutableMapOf(*pairs))
