package day24

import Input
import println
import readInput

const val packageName = "day24"

fun main() {
    fun Input.toHail(): List<Hail> {
        return this.map {
            val (position, velocity) = it.replace(Regex("\\s+"), " ").split(" @ ")
            val (px, py, pz) = position.split(", ").map { value -> value.toLong() }
            val (vx, vy, vz) = velocity.split(", ").map { value -> value.toLong() }
            Hail(Position(px, py, pz), Velocity(vx, vy, vz))
        }
    }

    fun part1(input: Input, min: Long, max: Long): Long {
        val hail = input.toHail()
        var intersections: Long = 0
        for (i in hail.indices) {
            val hail1 = hail[i]
            for (j in (i + 1)..<hail.size) {
                val hail2 = hail[j]
                if (hail1.isIntersectingInArea(hail2, min, max)) intersections++
            }
        }
        return intersections
    }

    fun part2(input: Input): Long {
        return input.size.toLong()
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput, 7, 27) == 2.toLong())
    check(part2(testInput) == 47.toLong())

    val input = readInput("$packageName/input")
    part1(input, 200000000000000, 400000000000000).println()
    part2(input).println()
}

data class Hail(val position: Position, val velocity: Velocity) {
    private val m = velocity.y.toDouble() / velocity.x.toDouble()
    private val n = position.y - position.x * m

    fun isIntersectingInArea(other: Hail, min: Long, max: Long): Boolean {
        val ix = (other.n - this.n) / (this.m - other.m)
        val iy = m * ix + n
        val intersectionPoint = Pair(ix, iy)
        if (this.isInPast(intersectionPoint) || other.isInPast(intersectionPoint)) return false

        val range = min.toDouble()..max.toDouble()
        return (ix in range && iy in range)
    }

    private fun isInPast(point: Pair<Double, Double>): Boolean {
        return if (velocity.x < 0) point.first - position.x > 0 else point.first - position.x < 0
    }

    override fun toString(): String = "$position $velocity m=$m n=$n"
}

data class Position(val x: Long, val y: Long, val z: Long)

data class Velocity(val x: Long, val y: Long, val z: Long)
