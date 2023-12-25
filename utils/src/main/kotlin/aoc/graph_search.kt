@file:Suppress("MemberVisibilityCanBePrivate")

package aoc

import java.util.*

/**
 * A lazy cache around the result of a search algorithm.
 */
class SearchCache<T>(s: Sequence<SearchInfo<T>>) : Sequence<SearchInfo<T>> {
    private val cached = CachedSequence(s)
    private val map = mutableMapOf<T, SearchInfo<T>?>()

    operator fun get(v: T): SearchInfo<T>? = map.computeIfAbsent(v) { cached.firstOrNull { it.node == v } }

    override fun iterator(): Iterator<SearchInfo<T>> = cached.iterator()

    companion object {
        internal operator fun <T> invoke(generator: suspend SequenceScope<SearchInfo<T>>.() -> Unit): SearchCache<T> =
            SearchCache(sequence(generator))
    }
}

/**
 * Information about a node in a search result.
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

/** Creates a [SearchCache] from this search result. */
fun <T> Sequence<SearchInfo<T>>.cached(): SearchCache<T> = SearchCache(this)

val <T> Sequence<SearchInfo<T>>.nodes: Sequence<T> get() = map { it.node }

fun <T> Sequence<SearchInfo<T>>.toUMap(): UMap<T, SearchInfo<T>> {
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
): Sequence<SearchInfo<T>> {
    val queue = mutableDequeOf<SearchInfo<T>>()

    fun enqueue(node: T, prev: SearchInfo<T>?, cost: Long) {
        if (process(node, prev, cost)) {
            queue += SearchInfo(node, prev, cost)
        }
    }

    enqueue(start, null, 0)

    val iterator = object : Iterator<SearchInfo<T>> {
        override fun hasNext(): Boolean = queue.isNotEmpty()

        override fun next(): SearchInfo<T> {
            val info = queue.removeFirst()
            forEachEdge(info.node) { next, cost -> enqueue(next, info, info.cost + cost) }
            return info
        }
    }

    return Sequence { iterator }
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
): Sequence<SearchInfo<T>> {
    // the two stacks are always of the same size
    val childrenStack = mutableListOf<Iterator<Edge<T>>>()
    val infoStack = mutableListOf<SearchInfo<T>?>()

    childrenStack += listOf(Edge(start, 0)).iterator()
    infoStack += null

    val iterator = object : Iterator<SearchInfo<T>> {
        private var next: SearchInfo<T>? = computeNext()

        override fun hasNext(): Boolean = next != null

        override fun next(): SearchInfo<T> {
            val n = next!!
            next = computeNext()
            return n
        }

        private fun computeNext(): SearchInfo<T>? {
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
                    val iterator = edges(node).iterator()
                    if (iterator.hasNext()) {
                        childrenStack += iterator
                        infoStack += info
                    }
                    return info
                }
            }
            return null
        }
    }

    return Sequence { iterator }
}

private fun <T> distinctFilter(): (T, SearchInfo<T>?, Long) -> Boolean {
    val complete = mutableSetOf<T>()
    return { node, _, _ -> complete.add(node) }
}

/** Runs Dijkstra's algorithm from the given node. */
fun <T> WeightedGraph<T>.dijkstra(start: T): Sequence<SearchInfo<T>> {
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

    val iterator = object : Iterator<SearchInfo<T>> {
        override fun hasNext(): Boolean = queue.isNotEmpty()

        override fun next(): SearchInfo<T> {
            val info = queue.poll()
            val node = info.node
            infoMap -= node
            complete += node
            forEachEdge(node) { to, weight -> enqueueOrUpdate(to, info, info.cost + weight) }
            return info
        }
    }

    return Sequence { iterator }
}

