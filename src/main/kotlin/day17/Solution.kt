package day17

import Input
import Position
import println
import readInput
import java.util.*

const val packageName = "day17"

fun main() {
    fun part1(input: Input): Long {
        return CityBlocks(input).getMinHeatLossForCrucible()
    }

    fun part2(input: Input): Long {
        return CityBlocks(input).getMinHeatLossForUltraCrucible()
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 102.toLong())
    check(part2(testInput) == 94.toLong())

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

data class CityBlocks(val map: List<String>) {
    private val n = map.size;
    private val m = map[0].length

    fun getMinHeatLossForCrucible(): Long {
        val heatLoss = crucibleTraverse(1, 3)
        return heatLoss
    }

    fun getMinHeatLossForUltraCrucible(): Long {
        val heatLoss = crucibleTraverse(4, 10)
        return heatLoss
    }

    private fun crucibleTraverse(minSteps: Int, maxSteps: Int): Long {
        val q = PriorityQueue<QueueEntity> { entity1, entity2 -> (entity1.heatLoss - entity2.heatLoss).toInt() }
        q.add(QueueEntity(0, Position(0, 0), Pair(0, 0)))
        val visited = mutableSetOf<String>()

        while (q.isNotEmpty()) {
            val current = q.remove()

            val position = current.position
            val direction = current.direction
            val heatLoss = current.heatLoss
            val key = current.key

            if (position.isEnd()) return heatLoss
            if (visited.contains(key)) continue

            visited.add(key)

            val allowedDirections = listOf(
                Pair(-1, 0),
                Pair(1, 0),
                Pair(0, -1),
                Pair(0, 1)
            ).filter { it != direction.copy(direction.first * -1, direction.second * -1) && it != direction }
            for (allowedDirection in allowedDirections) {
                var currentHeatLoss = heatLoss
                for (distance in 1..maxSteps) {
                    val newPosition = position.copy(
                        i = position.i + allowedDirection.first * distance,
                        j = position.j + allowedDirection.second * distance,
                    )
                    if (newPosition.isOutOfScope()) continue
                    currentHeatLoss += newPosition.blockHeatLoss()
                    if (distance < minSteps) continue
                    val next = QueueEntity(currentHeatLoss, newPosition, allowedDirection)
                    q.add(next)
                }
            }
        }
        return -1
    }

    private fun Position.isEnd() = this.i == n - 1 && this.j == m - 1

    private fun Position.isOutOfScope() = this.i < 0 || this.i >= n || this.j < 0 || this.j >= m

    private fun Position.blockHeatLoss() = map[this.i][this.j].toString().toLong()
}

data class QueueEntity(val heatLoss: Long, val position: Position, val direction: Pair<Int, Int>) {
    val key = "$position $direction"
}
