@file:Suppress("MemberVisibilityCanBePrivate")

package aoc

import java.util.*

/**
 * The lazily computed result of a search algorithm.
 * Due to the laziness, it can be used even on infinite graphs.
 *
 * Note: implements [Sequence] instead of [Iterable], so transformation functions like [Sequence.map] can be safely
 * called on it, even if contains infinite elements.
 */
class SearchResult<T>(s: Sequence<SearchInfo<T>>) : Sequence<SearchInfo<T>> {
    private val cached = CachedIterable(s)
    private val map = mutableMapOf<T, SearchInfo<T>?>()

    operator fun get(v: T): SearchInfo<T>? = map.computeIfAbsent(v) { cached.firstOrNull { it.node == v } }

    override fun iterator(): Iterator<SearchInfo<T>> = cached.iterator()

    companion object {
        internal operator fun <T> invoke(generator: suspend SequenceScope<SearchInfo<T>>.() -> Unit): SearchResult<T> =
            SearchResult(sequence(generator))
    }
}

/**
 * Information about a node in a [SearchResult].
 *
 * @param node the node itself
 * @param prev the previous node, or null if [node] is that start node
 * @param cost the cost to reach this node. In case of an unweighted graph, it is the number of edges traversed.
 *     The cost to reach the start node is 0.
 */
data class SearchInfo<T>(val node: T, val prev: SearchInfo<T>?, val cost: Long) {
    internal constructor(node: T, prev: SearchInfo<T>?) : this(node, prev, if (prev == null) 0 else prev.cost + 1)

    override fun toString(): String = "SearchInfo(node=$node, prev=..., cost=$cost)"
}

val <T> SearchResult<T>.nodes: Sequence<T> get() = map { it.node }

fun <T> SearchResult<T>.toUMap(): UMap<T, SearchInfo<T>> {
    val map = umapOf<T, SearchInfo<T>>()
    forEach { map[it.node] = it }
    return map
}

/** The path from the start to this info node. */
val <T> SearchInfo<T>.pathForwards: List<SearchInfo<T>> get() = iterateBackwards.reversed()

/** An iterator from this info node to the start. */
val <T> SearchInfo<T>.iterateBackwards: Iterable<SearchInfo<T>>
    get() = Iterable {
        object : Iterator<SearchInfo<T>> {
            private var current: SearchInfo<T>? = this@iterateBackwards
            override fun hasNext(): Boolean = current != null
            override fun next(): SearchInfo<T> {
                val result = current!!
                current = result.prev
                return result
            }
        }
    }


/** Runs breadth-first-search from the given node. */
fun <T> Graph<T>.bfs(start: T): SearchResult<T> {
    val queue = mutableDequeOf<SearchInfo<T>>()
    val complete = mutableSetOf<T>() // a node is complete when it enters the queue

    fun enqueue(node: T, prev: SearchInfo<T>?) {
        if (node !in complete) {
            complete += node
            queue += SearchInfo(node, prev)
        }
    }

    enqueue(start, null)

    return SearchResult {
        while (true) {
            val info = queue.removeFirstOrNull() ?: break
            yield(info)
            edges(info.node).forEach { enqueue(it, info) }
        }
    }
}


/** Runs depth-first-search from the given node. */
fun <T> Graph<T>.dfs(start: T): SearchResult<T> {
    // the two stacks are always of the same size
    val childrenStack = mutableListOf<Iterator<T>>()
    val infoStack = mutableListOf<SearchInfo<T>?>()
    val complete = mutableSetOf<T>() // a node is complete when it leaves the children stack

    childrenStack += listOf(start).iterator()
    infoStack += null

    return SearchResult {
        while (childrenStack.isNotEmpty()) {
            val last = childrenStack.last()
            if (!last.hasNext()) {
                childrenStack.removeLast()
                infoStack.removeLast()
                continue
            }

            val node = last.next()
            if (node !in complete) {
                val info = SearchInfo(node, infoStack.last())
                yield(info)
                complete += node
                childrenStack += edges(node).filter { it !in complete }.iterator()
                infoStack += info
            }
        }
    }
}


/** Runs Dijkstra's algorithm from the given node. */
fun <T> WeightedGraph<T>.dijkstra(start: T): SearchResult<T> {
    val infoMap = mutableMapOf<T, SearchInfo<T>>()
    val queue = PriorityQueue<SearchInfo<T>>(compareBy { it.cost })
    val complete = mutableSetOf<T>() // a node is complete when it leaves the queue

    fun enqueueOrUpdate(node: T, prev: SearchInfo<T>?, cost: Long) {
        if (node !in complete) {
            val oldInfo = infoMap[node]
            if (oldInfo == null || oldInfo.cost > cost) {
                if (oldInfo != null) { // remove the node from the map before updating its cost
                    queue -= oldInfo
                }
                val newInfo = SearchInfo(node, prev, cost)
                infoMap[node] = newInfo
                queue += newInfo
            }
        }
    }

    enqueueOrUpdate(start, null, 0)

    return SearchResult {
        while (true) {
            val info = queue.poll() ?: break
            yield(info)
            val node = info.node
            infoMap -= node
            complete += node
            weightedEdges(node).forEach { enqueueOrUpdate(it.to, info, info.cost + it.cost) }
        }
    }
}

