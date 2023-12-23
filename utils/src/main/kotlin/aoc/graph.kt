@file:Suppress("MemberVisibilityCanBePrivate")

package aoc

/**
 * A potentially infinite graph.
 *
 * @param T the type of nodes
 */
sealed interface BaseGraph<T> {
    /** Returns the targets of the edges going out from the given node. */
    fun neighbors(node: T): Sequence<T>

    /** Returns the edges going out from the given node. */
    fun edges(node: T): Sequence<Edge<T>>

    /**
     * Iterates over the edges of the given node.
     * Might perform better than [edges] on unweighted graphs as the wrapper objects do not have to be created.
     */
    fun forEachEdge(node: T, consumer: (T, Long) -> Unit)
}

/**
 * The unweighted variant of [BaseGraph].
 *
 * @param T the type of nodes
 */
interface Graph<T> : BaseGraph<T> {
    override fun edges(node: T): Sequence<Edge<T>> = neighbors(node).map { Edge(it, 1) }
    override fun forEachEdge(node: T, consumer: (T, Long) -> Unit) =
        neighbors(node).forEach { consumer(it, 1) }

    companion object {
        /** Creates a new unweighted graph using the given edge function. */
        operator fun <T> invoke(edges: (T) -> Iterable<T>): Graph<T> =
            ofSequences { edges(it).asSequence() }

        /** Creates a new unweighted graph using the given edge function. */
        fun <T> ofSequences(edges: (T) -> Sequence<T>): Graph<T> =
            object : Graph<T> {
                override fun neighbors(node: T): Sequence<T> = edges(node)
            }
    }
}

/**
 * The weighted variant of [BaseGraph].
 *
 * @param T the type of nodes
 */
interface WeightedGraph<T> : BaseGraph<T> {
    override fun neighbors(node: T): Sequence<T> = edges(node).map { it.to }
    override fun forEachEdge(node: T, consumer: (T, Long) -> Unit) =
        edges(node).forEach { (to, cost) -> consumer(to, cost) }

    companion object {
        /** Creates a new weighted graph using the given edge function. */
        operator fun <T> invoke(edges: (T) -> Iterable<Edge<T>>): WeightedGraph<T> =
            ofSequences { edges(it).asSequence() }

        /** Creates a new unweighted graph using the given edge function. */
        fun <T> ofSequences(edges: (T) -> Sequence<Edge<T>>): WeightedGraph<T> =
            object : WeightedGraph<T> {
                override fun edges(node: T): Sequence<Edge<T>> = edges(node)
            }
    }
}

/** The target and cost of an edge in a [WeightedGraph]. */
@JvmRecord
data class Edge<out T>(val to: T, val cost: Long = 1) {
    constructor(to: T, cost: Int) : this(to, cost.toLong())
}


/** Restricts this graph to only contain nodes that satisfy the given predicate. */
fun <T> Graph<T>.filterNodes(nodeFilter: (T) -> Boolean): Graph<T> =
    Graph.ofSequences { n -> neighbors(n).filter(nodeFilter) }

/** Restricts this graph to only contain edges that satisfy the given predicate. */
fun <T> Graph<T>.filterEdges(edgeFilter: (T, T) -> Boolean): Graph<T> =
    Graph.ofSequences { n -> neighbors(n).filter { edgeFilter(n, it) } }


/** Restricts this weighted graph to only contain edges that satisfy the given predicate. */
fun <T> WeightedGraph<T>.filterNodes(nodeFilter: (T) -> Boolean): WeightedGraph<T> =
    WeightedGraph.ofSequences { n -> edges(n).filter { nodeFilter(it.to) } }

/** Restricts this weighted graph to only contain edges that satisfy the given predicate. */
fun <T> WeightedGraph<T>.filterEdges(edgeFilter: (T, T) -> Boolean): WeightedGraph<T> =
    WeightedGraph.ofSequences { n -> edges(n).filter { edgeFilter(n, it.to) } }

/** Adds (or modifies) weights to the edges of this graph, creating a [WeightedGraph]. */
fun <T> Graph<T>.weighted(cost: (T, T) -> Long): WeightedGraph<T> =
    WeightedGraph.ofSequences { n -> neighbors(n).map { Edge(it, cost(n, it)) } }
