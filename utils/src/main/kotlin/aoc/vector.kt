@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package aoc

/** A 2- or 3-dimensional vector. */
sealed interface PosOrXyz {
    val x: Long
    val y: Long
}


/** A 2-dimensional vector (a "pos"ition on a 2D plane). */
sealed interface Pos : PosOrXyz {

    /** Returns a modified vector. */
    fun with(x: Long = this.x, y: Long = this.y): Pos = Pos(x, y)

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
    @JvmRecord
    private data class Impl(override val x: Long, override val y: Long) : Pos {
        override fun hashCode(): Int = hash()
        override fun equals(other: Any?): Boolean = isEqualTo(other)
        override fun toString(): String = "Pos($x,$y)"
    }
}


/** A 3-dimensional vector (with "x", "y" and "z" coordinates). */
sealed interface Xyz : PosOrXyz {

    val z: Long

    /** Returns a modified vector. */
    fun with(x: Long = this.x, y: Long = this.y, z: Long = this.z): Xyz = Xyz(x, y, z)

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

    /** Default implementation of [Xyz]. */
    @JvmRecord
    private data class Impl(override val x: Long, override val y: Long, override val z: Long) : Xyz {
        override fun hashCode(): Int = hash()
        override fun equals(other: Any?): Boolean = isEqualTo(other)
        override fun toString(): String = "Xyz($x,$y,$z)"
    }

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


// hashCode is optimized for small coordinates (the multiplier is the largest prime below the square root of 2^32)
internal fun Pos.hash(): Int = x.toInt() * 65_521 + y.toInt()
internal fun Pos.isEqualTo(other: Any?): Boolean = other is Pos && x == other.x && y == other.y

// hashCode is optimized for small coordinates (the multiplier is the largest prime below the cube root of 2^32)
internal fun Xyz.hash(): Int = (x.toInt() * 1621 + y.toInt()) * 1621 + z.toInt()
internal fun Xyz.isEqualTo(other: Any?): Boolean = other is Xyz && x == other.x && y == other.y && z == other.z
