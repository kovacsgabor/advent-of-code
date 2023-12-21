package aoc

object Y23Day20 : Day() {

    enum class Pulse { LOW, HIGH }

    data class SentPulse(val from: Node, val to: Node, val pulse: Pulse)

    abstract class Node(val name: String) {
        var inputCount: Int = 0
        var outNodes: List<Node> = mutableListOf()

        fun sendPulses(from: Node, pulse: Pulse): List<SentPulse> {
            val result = getPulseToSend(from, pulse)
            return if (result == null) listOf()
            else outNodes.map { SentPulse(this, it, result) }
        }

        abstract fun getPulseToSend(from: Node, pulse: Pulse): Pulse?

        /** Sets this node to its initial state. */
        abstract fun clear()

        override fun toString(): String = name
    }

    class Broadcaster : Node("broadcaster") {
        override fun getPulseToSend(from: Node, pulse: Pulse): Pulse = Pulse.LOW
        override fun clear() {}
    }

    class Normal(name: String) : Node(name) {
        override fun getPulseToSend(from: Node, pulse: Pulse): Pulse? = null
        override fun clear() {}
    }

    class FlipFlop(name: String) : Node(name) {
        var state: Pulse = Pulse.LOW

        override fun getPulseToSend(from: Node, pulse: Pulse): Pulse? =
            when (pulse) {
                Pulse.LOW -> {
                    state = when (state) {
                        Pulse.LOW -> Pulse.HIGH
                        Pulse.HIGH -> Pulse.LOW
                    }
                    state
                }

                Pulse.HIGH -> null
            }

        override fun clear() {
            state = Pulse.LOW
        }
    }

    class Conjunction(name: String) : Node(name) {
        private val highs = mutableSetOf<Node>()

        override fun getPulseToSend(from: Node, pulse: Pulse): Pulse {
            when (pulse) {
                Pulse.LOW -> highs -= from
                Pulse.HIGH -> highs += from
            }
            return if (highs.size == inputCount) Pulse.LOW else Pulse.HIGH
        }

        override fun clear() {
            highs.clear()
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val broadcaster = Broadcaster()

        // Parse the left hand send of each input line and create the nodes
        val nodes = lines
            .map { line ->
                val from = line.split(" -> ")[0]
                when {
                    from == "broadcaster" -> broadcaster
                    from.startsWith("%") -> FlipFlop(from.drop(1))
                    from.startsWith("&") -> Conjunction(from.drop(1))
                    else -> Normal(from)
                }
            }
            .map { it.name to it }
            .toUMap()

        // Parse the right hand send of each input line and register the edges
        lines.forEach { line ->
            val (left, right) = line.split(" -> ")

            val name =
                if (left.startsWith("%") || left.startsWith("&")) left.drop(1)
                else left

            val targetNodes = right
                .split(", ")
                .map { n -> nodes.computeIfAbsent(n) { Normal(it) } }

            nodes[name].outNodes += targetNodes

            targetNodes.forEach { it.inputCount++ }
        }

        // Part 1 is straightforward
        var lowCount = 0L
        var highCount = 0L

        repeat(1000) {
            val pulses = mutableDequeOf(SentPulse(broadcaster, broadcaster, Pulse.LOW))
            while (pulses.isNotEmpty()) {
                val (from, to, pulse) = pulses.removeFirst()
                when (pulse) {
                    Pulse.LOW -> lowCount++
                    Pulse.HIGH -> highCount++
                }
                pulses += to.sendPulses(from, pulse)
            }
        }

        part1(lowCount * highCount)

        // Find the length of the cycle for each out node of the broadcaster, and then find their LCM.
        // Here we have to heavily depend on the invariants of the input.
        part2(broadcaster.outNodes
            .map { out ->
                nodes.values.forEach { it.clear() }
                broadcaster.outNodes = listOf(out)
                var buttonPresses = 0L
                do {
                    buttonPresses += 1L
                    val pulses = mutableDequeOf(SentPulse(broadcaster, broadcaster, Pulse.LOW))
                    while (pulses.isNotEmpty()) {
                        val (from, to, pulse) = pulses.removeFirst()
                        pulses += to.sendPulses(from, pulse)
                    }
                } while (nodes.values.any { it is FlipFlop && it.state != Pulse.LOW })
                buttonPresses
            }
            .lcm())
    }

}
