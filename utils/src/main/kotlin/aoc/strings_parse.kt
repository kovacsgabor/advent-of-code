package aoc

import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * The main class of the parser DSL that can be used to parse strings with regex and convert them to specific types.
 *
 * Example:
 * ```
 * val groups = "57 and 32".parse {
 *  "(\\d*) and (\\d*)"()
 *  "(\\d*) or (\\d*)"()
 * }
 * ```
 * Here, the first string is the target.
 * The "parse" method accepts a function whose context is an instance of this type.
 * In that context, multiple regular expressions can be listed, each of which will be applied to the target until one
 * matches (and then the remaining regular expressions will be ignored).
 *
 * The parse function returns a [Groups] object that can be used to extract the captured groups and also automatically
 * convert them to the appropriate type:
 * ```
 * val (a : Long, b : Long) = groups
 * println(a) // 57
 * println(b) // 32
 * ```
 *
 * By passing a function to [invoke], these two steps can even be combined:
 * ```
 * "57 and 32".parse {
 *  "(\\d*) and (\\d*)" { (a : Long, b : Long) ->
 *    // ...
 *  }
 *  "(\\d*) or (\\d*)" { (a : Long, b : Long) ->
 *    // ...
 *  }
 * }
 * ```
 */
class ParsingContext(val s: String) {
    var result: Groups? = null

    inline operator fun String.invoke(func: (Groups) -> Unit = {}) {
        if (result == null) {
            val match = this.toRegex().matchEntire(s)
            if (match != null) {
                val g = Groups(match)
                func(g)
                result = g
            }
        }
    }
}

/**
 * Wrapper around a regex match with smart componentN functions that not just return the matched groups, but also
 * convert them to the required type (if that type is supported).
 * Supports simple types like String, Int, Long, Boolean and enums.
 *
 * Example:
 * ```
 * val groups = Groups("(\\d*) and (\\d*)".toRegex().matchEntire("57 and 32"))
 * val a : Long = groups.component1()
 * val b : Long = groups.component2()
 *
 * // or rather:
 * val (aa : Long, bb : Long) = groups
 * ```
 */
class Groups(private val match: MatchResult?) {
    inline operator fun <reified T : Any> component1(): T = componentN(1)
    inline operator fun <reified T : Any> component2(): T = componentN(2)
    inline operator fun <reified T : Any> component3(): T = componentN(3)
    inline operator fun <reified T : Any> component4(): T = componentN(4)
    inline operator fun <reified T : Any> component5(): T = componentN(5)
    inline operator fun <reified T : Any> component6(): T = componentN(6)
    inline operator fun <reified T : Any> component7(): T = componentN(7)
    inline operator fun <reified T : Any> component8(): T = componentN(8)
    inline operator fun <reified T : Any> component9(): T = componentN(9)
    inline operator fun <reified T : Any> component10(): T = componentN(10)

    inline fun <reified T : Any> componentN(index: Int): T = componentN(index, T::class)

    @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
    fun <T : Any> componentN(index: Int, cls: KClass<T>): T {
        if (match == null || index !in match.groupValues.indices) {
            throw IllegalStateException("Group not found at index $index, number of groups: ${match?.groupValues?.size ?: 0}")
        }

        val group = match.groupValues[index]
        return when (cls) {
            Int::class -> group.toInt()
            Long::class -> group.toLong()
            String::class -> group
            Char::class -> group[0]
            Boolean::class -> group.toBoolean()
            else -> when {
                cls.isSubclassOf(Enum::class) ->
                    cls.java.enumConstants.find { (it as Enum<*>).name.lowercase() == group.lowercase() }
                        ?: throw IllegalArgumentException("No enum constant in class $cls matching $group")

                else -> throw IllegalArgumentException("Cannot parse to class: $cls")
            }
        } as T
    }

}

inline operator fun <reified T : Any> Groups?.component1(): T? = this?.component1()
inline operator fun <reified T : Any> Groups?.component2(): T? = this?.component2()
inline operator fun <reified T : Any> Groups?.component3(): T? = this?.component3()
inline operator fun <reified T : Any> Groups?.component4(): T? = this?.component4()
inline operator fun <reified T : Any> Groups?.component5(): T? = this?.component5()
inline operator fun <reified T : Any> Groups?.component6(): T? = this?.component6()
inline operator fun <reified T : Any> Groups?.component7(): T? = this?.component7()
inline operator fun <reified T : Any> Groups?.component8(): T? = this?.component8()
inline operator fun <reified T : Any> Groups?.component9(): T? = this?.component9()
inline operator fun <reified T : Any> Groups?.component10(): T? = this?.component10()


/**
 * Parses this string using the regex and returns a [Groups] object that can be used to extract captured groups.
 * Throws an exception if the regex does not match.
 */
fun String.parse(s: String): Groups = parse { s() }

/**
 * Parses this string using the regex and returns a [Groups] object that can be used to extract captured groups.
 * Returns null if the regex does not match.
 */
fun String.parseOrNull(s: String): Groups? = parseOrNull { s() }

/** Parses each string using the given parser DSL. */
inline fun Iterable<String>.parseEach(func: ParsingContext.() -> Unit) = forEach { it.parse(func) }

/**
 * Parses this string using the given parser DSL.
 * Returns a [Groups] object for the matched regex.
 * Throws an exception if the regex does not match.
 */
inline fun String.parse(func: ParsingContext.() -> Unit): Groups =
    parseOrNull(func) ?: throw IllegalArgumentException("Cannot parse: $this")

/**
 * Parses this string using the given parser DSL.
 * Returns a [Groups] object for the matched regex.
 * Returns null if the regex does not match.
 */
inline fun String.parseOrNull(func: ParsingContext.() -> Unit): Groups? {
    if (isEmpty()) {
        return Groups(null)
    }

    val parser = ParsingContext(this)
    parser.func()
    return parser.result
}


/** A group that can be parsed as an [Int]. */
val int get() = """([-+]?\d+)"""

/** A group that can be parsed as a [Long]. */
val long get() = int

/** A group that can be parsed as an [Int], but contains no sign. */
val intNoSign get() = """(\d+)"""

/** A group that can be parsed as a [Long], but contains no sign. */
val longNoSign get() = intNoSign

/** A group that is a single word. */
val word get() = """([^ ,;]+)"""

/** A group that is a single word without a hyphen. */
val wordNoHyphen get() = """([^ ,;-]+)"""

/** A group that can contain anything. */
val anyText get() = "(.*)"
