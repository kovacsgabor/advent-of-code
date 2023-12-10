package aoc

/**
 * Creates a line from this to [other].
 * The result is never empty: this vector does not have to be less than [other].
 */
operator fun <V : Vector<V>> V.rangeTo(other: V): Line<V> = Line(this, other)

/**
 * A range of vectors ([Pos] or [Xyz]), represents a straight line.
 * Supports the case when the vectors differ with different amounts in different dimensions
 * (so e.g. Pos(1, 2) -> Pos(5, 10), returning 9 elements).
 */
data class Line<V : Vector<V>>(val first: V, val last: V) : Iterable<V> {

    /**
     * Contains a list of directions we have to move in.
     * Each direction has at least one non-zero coordinate, and their coordinates are between -1 and 1.
     * Empty if and only if [first] == [last].
     */
    val dirs: List<V>

    /**
     * Specifies for each direction how often it should be used during iteration.
     * Contains distinct values as dimensions with the same increments are always grouped together.
     * Always contains 1, unless [first] == [last], in which case it is empty.
     */
    val incrementEvery: List<Long>

    init {
        val diff = last - first

        @Suppress("UNCHECKED_CAST")
        val dimensions: List<V> =
            (when (val d = diff as Vector<*>) {
                is Pos -> listOf(Pos(d.x, 0), Pos(0, d.y))
                is Xyz -> listOf(Xyz(d.x, 0, 0), Xyz(0, d.y, 0), Xyz(0, 0, d.z))
            } as List<V>)
                .filterNot { it.isZero() }

        val dirs = dimensions.map { v -> v.map { it.coerceIn(-1, 1) } }

        val incrementEvery =
            if (dimensions.isEmpty()) {
                listOf()
            } else {
                val divisions = dimensions.zip(dirs) { a, b -> (a / b)!! }
                val max = divisions.max()
                divisions.map { max / it }
            }

        // Group together dimensions with the same multiplier
        val groupTogether = incrementEvery
            .withIndex()
            .groupBy({ it.value }, { it.index })
            .toList()

        this.dirs = groupTogether
            .map { it.second }
            .map { list -> list.map { dirs[it] }.sumOrNull()!! }

        this.incrementEvery = groupTogether.map { it.first }
    }

    /**
     * Returns the singleton element of [dirs], or null if contains zero or at least two elements.
     */
    val dir: V? get() = if (dirs.size == 1) dirs[0] else null

    override fun iterator(): Iterator<V> = object : Iterator<V> {
        var next: V? = first
        var nextIndex = 0L

        override fun hasNext(): Boolean = next != null

        override fun next(): V {
            val n = next!!
            nextIndex++
            next = if (n == last) null else increment(n)
            return n
        }

        private fun increment(v: V): V =
            v + incrementEvery
                .asSequence()
                .withIndex()
                .filter { (_, divider) -> nextIndex % divider == 0L }
                .map { (index, _) -> dirs[index] }
                .sumOrNull()!!
    }

}
