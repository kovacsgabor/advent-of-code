package aoc


/** A 2 by 2 matrix that represents a transformation of 2D vectors. */
data class TransformPos(val x: Pos, val y: Pos) : (Pos) -> Pos {
    override fun invoke(v: Pos): Pos = Pos(v * x, v * y)
}

/** A 3 by 3 matrix that represents a transformation of 3D vectors. */
data class TransformXyz(val x: Xyz, val y: Xyz, val z: Xyz) : (Xyz) -> Xyz {
    override fun invoke(v: Xyz): Xyz = Xyz(v * x, v * y, v * z)
}

/** All possible rotations of a 3D vector. */
val Xyz.Companion.rotations by lazy {
    Xyz.units.flatMap { x ->
        Xyz.units.filter { y -> x != y && x != -y }.map { y ->
            TransformXyz(x, y, x crossProduct y)
        }
    }
}

