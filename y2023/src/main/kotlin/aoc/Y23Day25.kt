package aoc

object Y23Day25 : Day() {

    @JvmStatic
    fun main(args: Array<String>) {
        val edges = umapOf<String, MutableList<String>>()

        lines.map {
            val split = it.split(": ", " ")
            val name = split[0]
            val others = split.drop(1)
            edges.computeIfAbsent(name) { mutableListOf() } += others
            for (o in others) {
                edges.computeIfAbsent(o) { mutableListOf() } += name
            }
        }

        val nodes = edges.keys.toMutableList()
        val others = nodes.drop(1)

        val graph = Graph<String> { edges[it] }

        val s = nodes.first()
        for (t in others) {
            val minCut = graph.minCut(s, t)
            if (minCut.removedEdges.size == 3) {
                val size = minCut.reachableFromS.size
                part1(size * (edges.size - size))
                return
            }
        }
    }

}
