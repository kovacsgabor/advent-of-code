package aoc

object Y23Day7 : Day() {

    enum class Kind {
        FIVE, FOUR, FULL, THREE, TWO, ONE, HIGH
    }

    data class Hand(val cards: List<Int>, val bid: Int) {

        val kind: Kind by lazy {
            val jokerCount = cards.count { it == 1 }

            if (jokerCount == 5) {
                return@lazy Kind.FIVE
            }

            val duplications = cards
                .filterNot { it == 1 }
                .groupingBy { it }
                .eachCount()
                .toList()
                .sortedByDescending { it.second }

            if (duplications[0].second + jokerCount == 5) Kind.FIVE
            else if (duplications[0].second + jokerCount == 4) Kind.FOUR
            else if (duplications[0].second + jokerCount == 3) {
                if (duplications[1].second == 2) Kind.FULL
                else Kind.THREE
            } else if (duplications[0].second == 2) {
                if (duplications[1].second + jokerCount == 2) Kind.TWO
                else Kind.ONE
            } else if (duplications[0].second + jokerCount == 2) Kind.ONE
            else Kind.HIGH
        }

        val value = (10L - kind.ordinal) * 100_00_00_00_00L +
                cards[0] * 100_00_00_00L +
                cards[1] * 100_00_00L +
                cards[2] * 100_00L +
                cards[3] * 100L +
                cards[4]
    }

    @JvmStatic
    fun main(args: Array<String>) {
        test = false

        fun solve(hasJokers: Boolean) = lines
            .map { line ->
                val (cards, value) = line.split(" ")
                Hand(
                    cards.toChars().map {
                        when (it) {
                            "T" -> 10
                            "J" -> if (hasJokers) 1 else 11
                            "Q" -> 12
                            "K" -> 13
                            "A" -> 14
                            else -> it.toInt()
                        }
                    },
                    value.toInt()
                )
            }
            .sortedBy { it.value }
            .mapIndexed { index, hand ->
                (index + 1) * hand.bid
            }
            .sum()

        part1(solve(false))
        part2(solve(true))
    }

}
