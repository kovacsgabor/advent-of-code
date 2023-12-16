package aoc

object Y23Day15 : Day() {

    private fun String.hash(): Long = toChars().fold(0) { value, next -> (value + next[0].code) * 17 % 256 }

    data class LabeledValue(val label: String, val value: Int)

    @JvmStatic
    fun main(args: Array<String>) {
        part1(lines[0].split(",").sumOf { it.hash() })

        val boxes = (0..255).map { mutableListOf<LabeledValue>() }
        lines[0].split(",").forEach { instruction ->
            val removal = instruction.last() == '-'
            val label = instruction.substring(0, instruction.length - (if (removal) 1 else 2))
            val box = boxes[label.hash()]

            if (removal) {
                box.removeIf { it.label == label }
            } else {
                val value = instruction.last().toString().toInt()
                val index = box.indexOfFirst { it.label == label }

                if (index != -1) box[index] = LabeledValue(label, value)
                else box += LabeledValue(label, value)
            }
        }

        part2(
            boxes.mapIndexed { index1, box ->
                box.mapIndexed { index2, value ->
                    (index1 + 1) * (index2 + 1) * value.value
                }.sum()
            }.sum()
        )
    }

}
