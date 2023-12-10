@file:Suppress("MemberVisibilityCanBePrivate")

package aoc

/**
 * An unweighted, potentially infinite graph.
 *
 * @param T the type of nodes
 */
interface Graph<T> {

    /** Returns the targets of the edges going out from the given node. */
    fun edges(node: T): Iterable<T>

    companion object {

        /** Creates a new graph using the given edge function. */
        operator fun <T> invoke(edges: (T) -> Iterable<T>): Graph<T> =
            object : Graph<T> {
                override fun edges(node: T): Iterable<T> = edges(node)
            }

    }
}

/**
 * The weighted variant of [Graph].
 *
 * @param T the type of nodes
 */
interface WeightedGraph<T> : Graph<T> {

    override fun edges(node: T): Iterable<T> = weightedEdges(node).map { it.to }

    /** Returns the targets of the edges going out from the given node, along with the cost of these edges. */
    fun weightedEdges(node: T): Iterable<Edge<T>>

    companion object {

        /** Creates a new graph using the given edge function. */
        operator fun <T> invoke(edges: (T) -> Iterable<Edge<T>>): WeightedGraph<T> =
            object : WeightedGraph<T> {
                override fun weightedEdges(node: T): Iterable<Edge<T>> = edges(node)
            }

    }

}


/** The target and cost of an edge in a [WeightedGraph]. */
data class Edge<out T>(val to: T, val cost: Long = 1) {
    constructor(to: T, cost: Int) : this(to, cost.toLong())
}


/** Restricts this graph to only contain nodes that satisfy the given predicate. */
fun <T> Graph<T>.filterNodes(nodeFilter: (T) -> Boolean): Graph<T> =
    Graph { n -> edges(n).filter(nodeFilter) }

/** Restricts this graph to only contain edges that satisfy the given predicate. */
fun <T> Graph<T>.filterEdges(edgeFilter: (T, T) -> Boolean): Graph<T> =
    Graph { n -> edges(n).filter { edgeFilter(n, it) } }


/** Restricts this weighted graph to only contain edges that satisfy the given predicate. */
fun <T> WeightedGraph<T>.filterNodes(nodeFilter: (T) -> Boolean): WeightedGraph<T> =
    WeightedGraph { n -> weightedEdges(n).filter { nodeFilter(it.to) } }

/** Restricts this weighted graph to only contain edges that satisfy the given predicate. */
fun <T> WeightedGraph<T>.filterEdges(edgeFilter: (T, T) -> Boolean): WeightedGraph<T> =
    WeightedGraph { n -> weightedEdges(n).filter { edgeFilter(n, it.to) } }


/** Adds (or modifies) weights to the edges of this graph, creating a [WeightedGraph]. */
fun <T> Graph<T>.weighted(cost: (T, T) -> Long): WeightedGraph<T> =
    WeightedGraph { n -> edges(n).map { Edge(it, cost(n, it)) } }
