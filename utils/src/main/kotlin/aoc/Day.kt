package aoc

import aoc.input.SmartReader

/** Base class for puzzle solutions. */
abstract class Day {

    /** If true, then the dayN_test.txt input will be loaded instead of the primary dayN.txt file. */
    var test: Boolean = false

    internal var part1: Any? = null
    internal var part2: Any? = null
    private var timer = 0L

    /** Runs the given code and prints the elapsed time. */
    inline fun <T> measured(func: () -> T): T {
        startTimer()
        try {
            return func()
        } finally {
            printElapsedTime()
        }
    }

    /** Saves the current time to be used by the next [printElapsedTime] call. */
    fun startTimer() {
        timer = System.currentTimeMillis()
    }

    /** Prints the elapsed time since the last call to [startTimer]. */
    fun printElapsedTime() {
        val time = System.currentTimeMillis() - timer
        println("Elapsed time: ${time / 1000} s ${time % 1000} ms")
    }

    /** Returns the lines of the input file. */
    val lines: List<String> by lazy { SmartReader.readLines(this, test) }

    /** Parses each line of the input file as an int. */
    val ints: MList<Int> get() = lines.map { it.toInt() }.toMutableList()

    /** Parses each line of the input file as a long. */
    val longs: MList<Long> get() = lines.map { it.toLong() }.toMutableList()

    /** Parses the input file as a comma-separated list of ints. */
    val intsByComma: MList<Int> get() = byComma { it.toInt() }

    /** Parses the input file as a comma-separated list of longs. */
    val longsByComma: MList<Long> get() = byComma { it.toLong() }

    private fun <T> byComma(func: (String) -> T): MList<T> =
        lines
            .flatMap { it.split(",") }
            .filter { it.isNotEmpty() }
            .map { func(it) }
            .toMutableList()

    /** Sets and prints the result for part 1. */
    fun part1(result: Any) {
        part1 = result
        println("Part1: $result")
    }

    /** Sets and prints the result for part 2. */
    fun part2(result: Any) {
        part2 = result
        println("Part2: $result")
    }

    override fun toString(): String = javaClass.simpleName

}
