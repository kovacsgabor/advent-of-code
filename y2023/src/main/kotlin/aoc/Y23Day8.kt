package aoc

object Y23Day8 : Day() {

    enum class Instruction {
        LEFT, RIGHT
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val instructions = lines[0].toChars().map {
            when (it) {
                "L" -> Instruction.LEFT
                "R" -> Instruction.RIGHT
                else -> throw IllegalArgumentException(it)
            }
        }

        val left = umapOf<String, String>()
        val right = umapOf<String, String>()

        lines.drop(2).parseEach {
            "(.{3}) = \\((.{3}), (.{3})\\)" { (from: String, ll: String, rr: String) ->
                left[from] = ll
                right[from] = rr
            }
        }

        val sources = left.keys.filter { it.endsWith("A") }.toSet()
        val targets = left.keys.filter { it.endsWith("Z") }.toSet()

        fun search(start: String): Long {
            var steps = 0L
            var current = start
            var index = 0
            while (true) {
                current = when(instructions[index]) {
                    Instruction.LEFT -> left[current]
                    Instruction.RIGHT -> right[current]
                }

                steps++
                if (current in targets) {
                    return steps
                }
                index++
                if (index == instructions.size) {
                    index = 0
                }
            }
        }

        part1(search("AAA"))
        part2(sources.map { search(it) }.lcm())
    }

}
