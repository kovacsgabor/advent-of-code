package aoc

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SearchTest {

    private val testGraph =
        Graph<Int> { listOf(it - 1 mod 10, it + 1 mod 10) }
            .weighted { from, to -> if (from == 0 && to == 9) 10 else 1 }

    @Test
    fun bfs() {
        assertEquals(
            listOf(0, 9, 1, 8, 2, 7, 3, 6, 4, 5),
            testGraph.bfs(0).nodes.toList()
        )
    }

    @Test
    fun dfs() {
        assertEquals(
            listOf(0, 9, 8, 7, 6, 5, 4, 3, 2, 1),
            testGraph.dfs(0).nodes.toList()
        )
    }

    @Test
    fun dijkstra() {
        assertEquals(
            listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
            testGraph.dijkstra(0).nodes.toList()
        )
    }

    @Test
    @Timeout(1)
    fun lazyBfs() {
        assertNotNull(
            Graph<Int> {
                listOf(it + 1)
            }
                .bfs(0)
                .find { it.node > 100 })
    }

    @Test
    @Timeout(1)
    fun lazyDfs() {
        assertNotNull(
            Graph<Int> {
                listOf(it + 1)
            }
                .dfs(0)
                .find { it.node > 100 })
    }

    @Test
    @Timeout(1)
    fun lazyDijkstra() {
        assertNotNull(
            Graph<Int> {
                listOf(it + 1)
            }
                .weighted { _, _ -> 1L }
                .dijkstra(0)
                .find { it.node > 100 })
    }

}

