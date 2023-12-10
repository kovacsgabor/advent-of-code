package aoc

object Y23Day5 : Day() {

    @JvmStatic
    fun main(args: Array<String>) = measured {

        fun solve(input: Iterable<LongRange>): Long {
            var current = input.toRangeSet()
            var next = emptyRangeSet()
            lines
                .splitWhen { it.isEmpty() } // split to blocks
                .drop(1) // ignore first block (parsed above)
                .forEach { mapping -> // for each mapping block
                    mapping
                        .drop(1) // ignore the header line
                        .parseEach {// parse all other lines
                            "$long $long $long" { (to: Long, from: Long, length: Long) ->
                                val intersection = current intersect aoc.rangeSetOpen(from, from + length)
                                next += intersection.shift(to - from)
                                current -= intersection
                            }
                        }
                    current += next
                    next = emptyRangeSet()
                }
            return current.min()
        }

        val valuesOfFirstLine = lines
            .first()
            .split(" ")
            .drop(1)
            .map { it.toLong() }

        part1(solve(valuesOfFirstLine.map { it..it }))
        part2(solve(valuesOfFirstLine.windowed(2, step = 2).map { it.first()..<it.first() + it.last() }))
    }

}
