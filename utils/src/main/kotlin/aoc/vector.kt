@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package aoc

/** Abstract base class for vector types, like [Pos] and [Xyz]. */
sealed class Vector<V : Vector<V>> : (Int) -> Long {

    /** The number of dimensions of this vector. */
    abstract val dimensions: Int

    /** Returns a coordinate of this vector by its index. */
    abstract override fun invoke(i: Int): Long

    final override fun hashCode(): Int =
        reduce({ it.toInt() }, { a, b -> a * 32_452_843 + b }) // optimized for small dimensions

    final override fun equals(other: Any?): Boolean = other is Vector<*> && areEqual(this, other)
}


/** A 2-dimensional vector (a "pos"ition on a 2D plane). */
sealed class Pos : Vector<Pos>(), Vector2OrMore {

    final override val dimensions: Int get() = 2
    final override fun invoke(i: Int): Long = when (i) {
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException(i)
    }

    override fun toString(): String = "Pos($x,$y)"

    /** Returns a modified vector. */
    fun with(x: Long = this.x, y: Long = this.y): Pos = Pos(x, y)

    fun neighbors4(): List<Pos> = Dir4(this)
    fun neighbors8(): List<Pos> = Dir8(this)

    companion object {
        val zero = Pos(0, 0)
        val one = Pos(1, 1)
        val units: List<Pos> get() = Dir4.values

        operator fun invoke(x: Long, y: Long): Pos = Impl(x, y)

        operator fun invoke(x: Int, y: Int) = Pos(x.toLong(), y.toLong())
        operator fun invoke(x: Int, y: Long) = Pos(x.toLong(), y)
        operator fun invoke(x: Long, y: Int) = Pos(x, y.toLong())
    }

    /** Default implementation of [Pos]. */
    private class Impl(override val x: Long, override val y: Long) : Pos()
}


/** A 3-dimensional vector (with "x", "y" and "z" coordinates). */
sealed class Xyz : Vector<Xyz>(), Vector3OrMore {

    final override val dimensions: Int get() = 3
    final override fun invoke(i: Int): Long = when (i) {
        0 -> x
        1 -> y
        2 -> z
        else -> throw IndexOutOfBoundsException(i)
    }

    override fun toString(): String = "Xyz($x,$y,$z)"

    /** Returns a modified vector. */
    fun with(x: Long = this.x, y: Long = this.y, z: Long = this.z): Xyz = Xyz(x, y, z)

    /** The neighbors of this vector in the cardinal directions. */
    fun neighbors6(): List<Xyz> = Dir6(this)

    /** The neighbors of this vector in the cardinal and ordinal directions. */
    fun neighbors26(): List<Xyz> = Dir26(this)

    /** Calculates the cross product of this vector and [other]. */
    infix fun crossProduct(other: Xyz): Xyz =
        Xyz(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        )

    companion object {
        val zero = Xyz(0, 0, 0)
        val one = Xyz(1, 1, 1)
        val units: List<Xyz> get() = Dir6.values

        operator fun invoke(x: Long, y: Long, z: Long): Xyz = Impl(x, y, z)

        operator fun invoke(x: Int, y: Int, z: Int) = Xyz(x.toLong(), y.toLong(), z.toLong())
        operator fun invoke(x: Long, y: Int, z: Int) = Xyz(x, y.toLong(), z.toLong())
        operator fun invoke(x: Int, y: Long, z: Int) = Xyz(x.toLong(), y, z.toLong())
        operator fun invoke(x: Int, y: Int, z: Long) = Xyz(x.toLong(), y.toLong(), z)
        operator fun invoke(x: Long, y: Long, z: Int) = Xyz(x, y, z.toLong())
        operator fun invoke(x: Long, y: Int, z: Long) = Xyz(x, y.toLong(), z)
        operator fun invoke(x: Int, y: Long, z: Long) = Xyz(x.toLong(), y, z)
    }

    private class Impl(override val x: Long, override val y: Long, override val z: Long) : Xyz()

}


/** Prints this iterable in table format. */
fun Iterable<Pos>.toString(defaultText: String = "", widen: Int = 0, func: (Pos) -> String): String {
    val s = if (this is Set<Pos>) this else this.toSet()
    return y.range().widen(widen).joinToString("\n") { y ->
        x.range().widen(widen).joinToString("") { x ->
            val p = Pos(x, y)
            if (p in s) func(p) else defaultText
        }
    }
}

/** Prints this map in table format. */
fun <K> Map<Pos, K>.toString(defaultText: String = "", widen: Int = 0, func: (K) -> String): String =
    keys.toString(defaultText, widen) { func(this[it]!!) }


private fun areEqual(self: Vector<*>, other: Vector<*>): Boolean {
    if (self.dimensions != other.dimensions) return false
    self.doReduceWith(
        other,
        { a, b ->
            if (a != b) return false
            Unit
        },
        { _, _ -> }
    )
    return true
}
