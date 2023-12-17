package aoc

/** The neighbors of this vector in the cardinal directions. */
fun Pos.neighbors4(): List<Pos> = Dir4(this)

/** The neighbors of this vector in the cardinal and ordinal directions. */
fun Pos.neighbors8(): List<Pos> = Dir8(this)

/** The neighbors of this vector in the cardinal directions. */
fun Xyz.neighbors6(): List<Xyz> = Dir6(this)

/** The neighbors of this vector in the cardinal and ordinal directions. */
fun Xyz.neighbors26(): List<Xyz> = Dir26(this)

/**
 * Base interface for companion objects of types that define directions in the 2D plane.
 *
 * As a function, returns the neighbors of a vector in these directions.
 * As a graph, represents the infinite graph of vectors in which each vector is connected to its neighbors.
 */
sealed interface Directions2D : (Pos) -> List<Pos>, Graph<Pos> {
    val values: List<Pos>

    override fun edges(node: Pos): Iterable<Pos> = this(node)
    override fun invoke(c: Pos): List<Pos> = values.map { c + it }
}

/**
 * Base interface for companion objects of types that define directions in the 3D plane.
 *
 * As a function, returns the neighbors of a vector in these directions.
 * As a graph, represents the infinite graph of vectors in which each vector is connected to its neighbors.
 */
sealed interface Directions3D : (Xyz) -> List<Xyz>, Graph<Xyz> {
    val values: List<Xyz>
    override fun edges(node: Xyz): Iterable<Xyz> = this(node)
    override fun invoke(c: Xyz): List<Xyz> = values.map { c + it }
}


/** The cardinal directions in 2D. */
sealed class Dir4 private constructor(override val x: Long, override val y: Long) : Pos {

    object UP : Dir4(0, -1)
    object LEFT : Dir4(-1, 0)
    object DOWN : Dir4(0, 1)
    object RIGHT : Dir4(1, 0)

    /** Returns the direction that is 90 degrees to the left to this one. */
    fun left(): Dir4 = when (this) {
        UP -> LEFT
        LEFT -> DOWN
        DOWN -> RIGHT
        RIGHT -> UP
    }

    /** Returns the direction that is 90 degrees to the right to this one. */
    fun right(): Dir4 = when (this) {
        UP -> RIGHT
        LEFT -> UP
        DOWN -> LEFT
        RIGHT -> DOWN
    }

    /** Returns the direction that is 180 degrees to this one. */
    fun opposite(): Dir4 = when (this) {
        UP -> DOWN
        LEFT -> RIGHT
        DOWN -> UP
        RIGHT -> LEFT
    }

    /** A one-character representation of [Dir4]. Useful as a target type in [parser DSL][ParsingContext]. */
    enum class OneChar {
        U, D, R, L;

        fun toPos() = when (this) {
            U -> UP
            D -> DOWN
            R -> RIGHT
            L -> LEFT
        }
    }

    /** A multi-character representation of [Dir4]. Useful as a target type in the [parser DSL][ParsingContext]. */
    enum class MultiChar {
        UP, DOWN, RIGHT, LEFT;

        fun toPos() = when (this) {
            UP -> Dir4.UP
            DOWN -> Dir4.DOWN
            RIGHT -> Dir4.RIGHT
            LEFT -> Dir4.LEFT
        }
    }

    final override fun hashCode(): Int = hash()
    final override fun equals(other: Any?): Boolean = isEqualTo(other)
    final override fun toString(): String = javaClass.simpleName

    companion object : Directions2D {
        override val values: List<Dir4> by lazy { listOf(UP, LEFT, DOWN, RIGHT) }
    }

}


/** The cardinal and ordinal directions in 2D. */
sealed class Dir8 private constructor(
    final override val x: Long,
    final override val y: Long,
) : Pos {
    private constructor(p: Pos) : this(p.x, p.y)

    object N : Dir8(Dir4.UP)
    object W : Dir8(Dir4.LEFT)
    object S : Dir8(Dir4.DOWN)
    object E : Dir8(Dir4.RIGHT)

    object NW : Dir8(N + W)
    object NE : Dir8(N + E)
    object SW : Dir8(S + W)
    object SE : Dir8(S + E)

    /** Returns the direction that is 45 degrees to the left to this one. */
    fun left(): Dir8 = when (this) {
        N -> NW
        NW -> W
        W -> SW
        SW -> S
        S -> SE
        SE -> E
        E -> NE
        NE -> N
    }

    /** Returns the direction that is 45 degrees to the right to this one. */
    fun right(): Dir8 = when (this) {
        N -> NE
        NW -> N
        W -> NW
        SW -> W
        S -> SW
        SE -> S
        E -> SE
        NE -> E
    }

    final override fun hashCode(): Int = hash()
    final override fun equals(other: Any?): Boolean = isEqualTo(other)
    final override fun toString(): String = javaClass.simpleName

    companion object : Directions2D {
        override val values: List<Dir8> by lazy { listOf(N, W, S, E, NW, NE, SW, SE) }
    }

}


/** The cardinal directions in 3D. */
sealed class Dir6 private constructor(override val x: Long, override val y: Long, override val z: Long) : Xyz {
    object NegX : Dir6(-1, 0, 0)
    object PosX : Dir6(1, 0, 0)
    object NegY : Dir6(0, -1, 0)
    object PosY : Dir6(0, 1, 0)
    object NegZ : Dir6(0, 0, -1)
    object PosZ : Dir6(0, 0, 1)

    final override fun hashCode(): Int = hash()
    final override fun equals(other: Any?): Boolean = isEqualTo(other)
    final override fun toString(): String = javaClass.simpleName

    companion object : Directions3D {
        override val values: List<Dir6> by lazy { listOf(NegX, PosX, NegY, PosY, NegZ, PosZ) }
    }
}


/** The cardinal and ordinal directions in 3D. */
sealed class Dir26 private constructor() : Xyz {
    final override val x: Long
    final override val y: Long
    final override val z: Long

    init {
        fun parse(s: String): Xyz {
            var result = Xyz.zero
            for (i in s.indices step 2) {
                val sign = when (s[i]) {
                    'P' -> 1
                    'N' -> -1
                    else -> throw IllegalArgumentException(s)
                }
                val dir = when (s[i + 1]) {
                    'x' -> Dir6.PosX
                    'y' -> Dir6.PosY
                    'z' -> Dir6.PosZ
                    else -> throw IllegalArgumentException(s)
                }
                result += sign * dir
            }
            return result
        }

        val xyz = parse(javaClass.simpleName)
        x = xyz.x
        y = xyz.y
        z = xyz.z
    }

    object Nx : Dir26()
    object Px : Dir26()
    object Ny : Dir26()
    object Py : Dir26()
    object Nz : Dir26()
    object Pz : Dir26()

    object NxNy : Dir26()
    object NxPy : Dir26()
    object NxNz : Dir26()
    object NxPz : Dir26()
    object PxNy : Dir26()
    object PxPy : Dir26()
    object PxNz : Dir26()
    object PxPz : Dir26()
    object NyNz : Dir26()
    object NyPz : Dir26()
    object PyNz : Dir26()
    object PyPz : Dir26()

    object NxNyNz : Dir26()
    object NxNyPz : Dir26()
    object NxPyNz : Dir26()
    object NxPyPz : Dir26()
    object PxNyNz : Dir26()
    object PxNyPz : Dir26()
    object PxPyNz : Dir26()
    object PxPyPz : Dir26()

    final override fun hashCode(): Int = hash()
    final override fun equals(other: Any?): Boolean = isEqualTo(other)
    final override fun toString(): String = javaClass.simpleName

    companion object : Directions3D {
        override val values: List<Dir26> by lazy {
            listOf(
                Nx, Px, Ny, Py, Nz, Pz,
                NxNy, NxPy, NxNz, NxPz, PxNy, PxPy, PxNz, PxPz, NyNz, NyPz, PyNz, PyPz,
                NxNyNz, NxNyPz, NxPyNz, NxPyPz, PxNyNz, PxNyPz, PxPyNz, PxPyPz,
            )
        }
    }

}
