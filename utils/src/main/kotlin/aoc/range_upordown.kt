package aoc

infix fun Int.upOrDownTo(other: Int): IntProgression = if (this <= other) this..other else this downTo other
infix fun Long.upOrDownTo(other: Long): LongProgression = if (this <= other) this..other else this downTo other

val IntProgression.size get() = (last - first) / step + 1
val LongProgression.size get() = (last - first) / step + 1

operator fun IntProgression.contains(value: Int): Boolean =
    value in min(first, last)..max(first, last) && (value - first) % step == 0

operator fun LongProgression.contains(value: Long): Boolean =
    value in min(first, last)..max(first, last) && (value - first) % step == 0L
