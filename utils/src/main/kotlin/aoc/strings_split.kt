package aoc

/**
 * Splits each line in the given list by the specified separator (if not given, each character is considered separately),
 * then gives them back in a map based on their position: the row is considered the Y index, the column the X.
 */
fun List<String>.splitByPositions(separator: String = "", default: String? = null): UMap<Pos, String> =
    splitByPositions(separator, default) { it }

/**
 * Splits each line in the given list by the specified separator (if not given, each character is considered separately),
 * then gives them back in a map based on their position: the row is considered the Y index, the column the X.
 *
 * The given mapping function is applied to each element.
 */
fun <T> List<String>.splitByPositions(separator: String = "", default: T? = null, func: (String) -> T): UMap<Pos, T> =
    indices.flatMap { y ->
        val parts = this[y].split(separator).filter { it.isNotEmpty() }
        this[y].indices.map { x ->
            Pos(x, y) to func(parts[x])
        }
    }.toUMap(default)

/**
 * Splits the given list on elements that satisfy the given predicate.
 * The matched elements will not appear in the result.
 */
fun <T> List<T>.splitWhen(pred: (T) -> Boolean): List<List<T>> {
    val result = mutableListOf<List<T>>()
    var current = mutableListOf<T>()
    for (e in this) {
        if (pred(e)) {
            current = mutableListOf()
        } else {
            if (current.isEmpty()) {
                result += current
            }
            current += e
        }
    }
    return result
}
