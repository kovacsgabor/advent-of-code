package aoc

/**
 * Memoizes the given function.
 * The receiver of the lambda function is the returned wrapper itself, so it can be called recursively.
 */
fun <A, T> memoize(function: ((A) -> T).(A) -> T): (A) -> T =
    object : MemoizedFunction<A, T>(), (A) -> T {
        override fun invoke(a: A): T = invokeCached(a)
        override fun doInvoke(p: A): T = function(this, p)
    }

/**
 * Memoizes the given function.
 * The receiver of the lambda function is the returned wrapper itself, so it can be called recursively.
 */
fun <A, B, T> memoize(function: ((A, B) -> T).(A, B) -> T): (A, B) -> T =
    object : MemoizedFunction<Params2<A, B>, T>(), (A, B) -> T {
        override fun invoke(a: A, b: B): T = invokeCached(Params2(a, b))
        override fun doInvoke(p: Params2<A, B>): T = function(this, p.a, p.b)
    }

/**
 * Memoizes the given function.
 * The receiver of the lambda function is the returned wrapper itself, so it can be called recursively.
 */
fun <A, B, C, T> memoize(function: ((A, B, C) -> T).(A, B, C) -> T): (A, B, C) -> T =
    object : MemoizedFunction<Params3<A, B, C>, T>(), (A, B, C) -> T {
        override fun invoke(a: A, b: B, c: C): T = invokeCached(Params3(a, b, c))
        override fun doInvoke(p: Params3<A, B, C>): T = function(this, p.a, p.b, p.c)
    }

/**
 * Memoizes the given function.
 * The receiver of the lambda function is the returned wrapper itself, so it can be called recursively.
 */
fun <A, B, C, D, T> memoize(function: ((A, B, C, D) -> T).(A, B, C, D) -> T): (A, B, C, D) -> T =
    object : MemoizedFunction<Params4<A, B, C, D>, T>(), (A, B, C, D) -> T {
        override fun invoke(a: A, b: B, c: C, d: D): T = invokeCached(Params4(a, b, c, d))
        override fun doInvoke(p: Params4<A, B, C, D>): T = function(this, p.a, p.b, p.c, p.d)
    }

/**
 * Memoizes the given function.
 * The receiver of the lambda function is the returned wrapper itself, so it can be called recursively.
 */
fun <A, B, C, D, E, T> memoize(function: ((A, B, C, D, E) -> T).(A, B, C, D, E) -> T): (A, B, C, D, E) -> T =
    object : MemoizedFunction<Params5<A, B, C, D, E>, T>(), (A, B, C, D, E) -> T {
        override fun invoke(a: A, b: B, c: C, d: D, e: E): T = invokeCached(Params5(a, b, c, d, e))
        override fun doInvoke(p: Params5<A, B, C, D, E>): T = function(this, p.a, p.b, p.c, p.d, p.e)
    }

/**
 * Memoizes the given function.
 * The receiver of the lambda function is the returned wrapper itself, so it can be called recursively.
 */
fun <A, B, C, D, E, F, T> memoize(function: ((A, B, C, D, E, F) -> T).(A, B, C, D, E, F) -> T): (A, B, C, D, E, F) -> T =
    object : MemoizedFunction<Params6<A, B, C, D, E, F>, T>(), (A, B, C, D, E, F) -> T {
        override fun invoke(a: A, b: B, c: C, d: D, e: E, f: F): T = invokeCached(Params6(a, b, c, d, e, f))
        override fun doInvoke(p: Params6<A, B, C, D, E, F>): T = function(this, p.a, p.b, p.c, p.d, p.e, p.f)
    }

/**
 * Memoizes the given function.
 * The receiver of the lambda function is the returned wrapper itself, so it can be called recursively.
 */
fun <A, B, C, D, E, F, G, T> memoize(function: ((A, B, C, D, E, F, G) -> T).(A, B, C, D, E, F, G) -> T): (A, B, C, D, E, F, G) -> T =
    object : MemoizedFunction<Params7<A, B, C, D, E, F, G>, T>(), (A, B, C, D, E, F, G) -> T {
        override fun invoke(a: A, b: B, c: C, d: D, e: E, f: F, g: G): T = invokeCached(Params7(a, b, c, d, e, f, g))
        override fun doInvoke(p: Params7<A, B, C, D, E, F, G>): T = function(this, p.a, p.b, p.c, p.d, p.e, p.f, p.g)
    }

/** Abstract implementation of memoized functions. */
private abstract class MemoizedFunction<P, T> {
    private val cache = mutableMapOf<P, T>()

    fun invokeCached(param: P): T {
        var result = cache[param]
        if (result == null) {
            result = doInvoke(param)
            cache[param] = result
        }
        return result!!
    }

    abstract fun doInvoke(p: P): T
}

// Wrappers for multiple arguments
@JvmRecord
private data class Params2<A, B>(val a: A, val b: B)

@JvmRecord
private data class Params3<A, B, C>(val a: A, val b: B, val c: C)

@JvmRecord
private data class Params4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)

@JvmRecord
private data class Params5<A, B, C, D, E>(val a: A, val b: B, val c: C, val d: D, val e: E)

@JvmRecord
private data class Params6<A, B, C, D, E, F>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F)

@JvmRecord
private data class Params7<A, B, C, D, E, F, G>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G)
