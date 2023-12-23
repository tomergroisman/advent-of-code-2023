package day23

import Input
import Position
import println
import readInput
import java.util.*

const val packageName = "day23"

fun main() {
    fun part1(input: Input): Int {
        val trailMap = TrailMap(input, canClimb = false)
        return trailMap.getLongestHike()
    }

    fun part2(input: Input): Int {
        val trailMap = TrailMap(input, canClimb = true)
        return trailMap.getLongestHike()
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 94)
    check(part2(testInput) == 154)

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

data class TrailMap(val map: List<String>, val canClimb: Boolean) {
    private val n = map.size
    private val m = map[0].length

    fun getLongestHike(): Int {
        val q: Queue<Entity> = LinkedList()
        val start = Position(0, 1)
        q.add(Entity(start, setOf()))
        val lengths = mutableSetOf<Int>()

        while (q.isNotEmpty()) {
            val entity = q.poll()
            val position = entity.position
            val origin = entity.path
            val length = entity.length

            if (position == Position(n - 1, m - 2)) lengths.add(length)

            val path = origin.toMutableSet()
            path.add(position)

            for (direction in listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))) {
                val newPosition = position.copy(i = position.i + direction.first, j = position.j + direction.second)
                if (!newPosition.isValid(direction, origin)) continue
                q.add(Entity(newPosition, path))
            }
        }
        return lengths.max()
    }

    private fun Position.isValid(direction: Pair<Int, Int>, origin: Set<Position>): Boolean {
        if (origin.contains(this)) return false

        val isOutOfScope = !(this.i in 0..<n && this.j in 0..<m)
        if (isOutOfScope) return false

        val tile = map[this.i][this.j]

        return when (tile) {
            '.' -> true
            '#' -> false
            '^' -> if (canClimb) true else direction != Pair(1, 0)
            '>' -> if (canClimb) true else direction != Pair(0, -1)
            'v' -> if (canClimb) true else direction != Pair(-1, 0)
            else -> if (canClimb) true else direction != Pair(0, 1)
        }
    }
}

data class Entity(val position: Position, val path: Set<Position>) {
    val length = path.size
}
