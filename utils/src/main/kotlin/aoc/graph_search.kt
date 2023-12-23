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
@JvmRecord
data class SearchInfo<T>(val node: T, val prev: SearchInfo<T>?, val cost: Long) {
    override fun toString(): String = "SearchInfo(node=$node, prev=..., cost=$cost)"
}

val <T> SearchResult<T>.nodes: Sequence<T> get() = map { it.node }

fun <T> SearchResult<T>.toUMap(): UMap<T, SearchInfo<T>> {
    val map = umapOf<T, SearchInfo<T>>()
    forEach { map[it.node] = it }
    return map
}

/** The path from the start to this info node. */
val <T> SearchInfo<T>?.pathForwards: List<SearchInfo<T>> get() = iterateBackwards.asIterable().reversed()

/** An iterator from this info node to the start. */
val <T> SearchInfo<T>?.iterateBackwards: Sequence<SearchInfo<T>>
    get() = Sequence {
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

/** Returns the cost of this info, or 0 if the info is null. */
val <T> SearchInfo<T>?.cost: Long get() = this?.cost ?: 0L

/**
 * Runs breadth-first-search from the given node with custom logic to decide which paths should be visited.
 *
 * @param process called exactly once whenever a node is about to be added to the queue, and should return whether the
 *   node should be added to the result, and its children processed. The function is called for *all* encountered nodes,
 *   even those that were processed before, allowing a custom search to revisit nodes as deemed necessary.
 *   The function is *not* expected to be pure: it is safe to store already visited nodes or paths.
 *   The default value only allows nodes to be processed when they are first encountered.
 */
fun <T> BaseGraph<T>.bfs(
    start: T,
    process: (T, SearchInfo<T>?, Long) -> Boolean = distinctFilter(),
): SearchResult<T> {
    val queue = mutableDequeOf<SearchInfo<T>>()

    fun enqueue(node: T, prev: SearchInfo<T>?, cost: Long) {
        if (process(node, prev, cost)) {
            queue += SearchInfo(node, prev, cost)
        }
    }

    enqueue(start, null, 0)

    return SearchResult {
        while (true) {
            val info = queue.removeFirstOrNull() ?: break
            yield(info)
            forEachEdge(info.node) { next, cost -> enqueue(next, info, info.cost + cost) }
        }
    }
}


/**
 * Runs depth-first-search from the given node.
 *
 * @param process called exactly once whenever a node is removed from the stack, and should return whether the node
 *   should be added to the result, and its children processed. The function is called for *all* encountered nodes,
 *   even those that were processed before, allowing a custom search to revisit nodes as deemed necessary.
 *   The function is *not* expected to be pure: it is safe to store already visited nodes or paths.
 *   The default value only allows nodes to be processed when they are first encountered.
 */
fun <T> BaseGraph<T>.dfs(
    start: T,
    process: (T, SearchInfo<T>?, Long) -> Boolean = distinctFilter(),
): SearchResult<T> {
    // the two stacks are always of the same size
    val childrenStack = mutableListOf<Iterator<Edge<T>>>()
    val infoStack = mutableListOf<SearchInfo<T>?>()

    childrenStack += listOf(Edge(start, 0)).iterator()
    infoStack += null

    return SearchResult {
        while (childrenStack.isNotEmpty()) {
            val last = childrenStack.last()
            if (!last.hasNext()) {
                childrenStack.removeLast()
                infoStack.removeLast()
                continue
            }

            val prev = infoStack.last()
            val (node, weight) = last.next()
            val cost = prev.cost + weight
            if (process(node, prev, cost)) {
                val info = SearchInfo(node, prev, cost)
                yield(info)
                val iterator = edges(node).iterator()
                if (iterator.hasNext()) {
                    childrenStack += iterator
                    infoStack += info
                }
            }
        }
    }
}

private fun <T> distinctFilter(): (T, SearchInfo<T>?, Long) -> Boolean {
    val complete = mutableSetOf<T>()
    return { node, _, _ -> complete.add(node) }
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
            forEachEdge(node) { to, weight -> enqueueOrUpdate(to, info, info.cost + weight) }
        }
    }
}

