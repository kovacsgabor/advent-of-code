package aoc

private typealias Rating = List<Long>

object Y23Day19 : Day() {

    private const val MIN = 1L
    private const val MAX = 4000L

    @Suppress("EnumEntryName")
    enum class Value {
        x, m, a, s
    }

    sealed interface Rule {
        val action: String
    }

    data class AutoApplyRule(override val action: String) : Rule

    /**
     * @param valueIndex the index of the value to compare, in 0..3
     * @param number the number to compare to
     * @param sign -1 or 1
     * @param action A, R or the workflow to go to
     */
    data class ComparisonRule(val valueIndex: Int, val number: Long, val sign: Int, override val action: String) : Rule

    @JvmStatic
    fun main(args: Array<String>) {
        test = false

        val (workflowDeclarations, ratingDeclarations) = lines.splitWhen { it.isEmpty() }

        val workflows = workflowDeclarations.map { line ->
            val (name, ruleDeclarations) = line.dropLast(1).split("{")
            val rules = mutableListOf<Rule>()
            ruleDeclarations
                .split(",")
                .parseEach {
                    "$word([<>])$int:$word" { (a: Value, b: String, c: Long, d: String) ->
                        rules += ComparisonRule(a.ordinal, c, if (b == "<") 1 else -1, d)
                    }
                    word { (action: String) ->
                        rules += AutoApplyRule(action)
                    }
                }
            name to rules
        }.toUMap()

        val ratings = mutableListOf<Rating>()
        ratingDeclarations.parseEach {
            "\\{x=$int,m=$int,a=$int,s=$int\\}" { (x: Long, m: Long, a: Long, s: Long) ->
                ratings += listOf(x, m, a, s)
            }
        }

        fun isAccepted(rating: Rating): Boolean {
            var workflow = workflows["in"]
            while (true) {
                for (rule in workflow) {
                    if (rule !is ComparisonRule || rating[rule.valueIndex] * rule.sign < rule.number * rule.sign) {
                        when (rule.action) {
                            "R" -> return false
                            "A" -> return true
                            else -> workflow = workflows[rule.action]
                        }
                        break
                    }
                }
            }
        }

        part1(ratings.filter { isAccepted(it) }.sumOf { it.sum() })

        fun findNumberPossibilities(rules: List<Rule>, ranges: List<LongRange>): Long {
            if (ranges.any { it.isEmpty() }) return 0

            val rule = rules.first()

            fun applyActionOfRule(ranges: List<LongRange>) =
                when (rule.action) {
                    "R" -> 0L
                    "A" -> ranges.map { it.size }.product()
                    else -> findNumberPossibilities(workflows[rule.action], ranges)
                }

            if (rule !is ComparisonRule) return applyActionOfRule(ranges)

            val (valueIndex, number, sign) = rule

            // The possible values that can be used when the rule applies and when it does not
            val thenRange = if (sign > 0) MIN..<number else number + 1..MAX
            val elseRange = if (sign > 0) number..MAX else MIN..<number + 1

            val actualRange = ranges[valueIndex]
            val thenIntersection = actualRange intersectRange thenRange
            val elseIntersection = actualRange intersectRange elseRange

            var sum = 0L
            if (thenIntersection.isNotEmpty()) {
                sum += applyActionOfRule(ranges.with(valueIndex, thenIntersection))
            }
            if (elseIntersection.isNotEmpty()) {
                val remainingRules = rules.subList(1, rules.size)
                sum += findNumberPossibilities(remainingRules, ranges.with(valueIndex, elseIntersection))
            }
            return sum
        }

        part2(findNumberPossibilities(workflows["in"], nCopies(4, MIN..MAX)))
    }

}