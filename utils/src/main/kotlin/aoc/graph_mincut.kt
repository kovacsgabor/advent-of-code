package aoc

/** Calculates the minimum s-t cut. */
fun <T> BaseGraph<T>.minCut(s: T, t: T): MinimumCut<T> {
    if (s == t) throw IllegalArgumentException()

    val edges = bfs(s)
        .nodes
        .map { a -> a to edges(a).map { it.to to it.cost }.toCountMap() }
        .toUMap()

    val residualGraph = WeightedGraph<T> {
        edges[it].mapNotNull { (to, cost) -> if (cost > 0) Edge(to, cost) else null }
    }

    while (true) {
        val info = residualGraph.bfs(s).firstOrNull { it.node == t }
        if (info == null) break // we can no longer reach t from s

        val pathFlow = info.iterateBackwards.minOf { it.weight ?: Long.MAX_VALUE }

        // Update residual capacities
        info.iterateBackwards.forEach {
            val node = it.node
            val parent = it.prev?.node ?: return@forEach
            edges[parent][node] -= pathFlow
            edges[node][parent] += pathFlow
        }
    }

    return MinimumCut(this, s, t, residualGraph.findReachable(s))
}

class MinimumCut<T> internal constructor(
    val graph: BaseGraph<T>,
    val s: T,
    val t: T,
    val reachableFromS: Set<T>,
) {
    val removedEdges: List<Pair<T, T>> by lazy {
        reachableFromS.flatMap { from -> graph.neighbors(from).filter { it !in reachableFromS }.map { from to it } }
    }
}
