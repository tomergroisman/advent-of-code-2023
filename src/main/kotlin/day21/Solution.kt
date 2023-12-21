package day21

import Input
import Position
import println
import readInput
import java.util.*

const val packageName = "day21"

fun main() {
    fun part1(input: Input, stepsToday: Int): Long {
        val stepCounter = StepCounter(input)
        return stepCounter.getReachableGardenPlotsCount(stepsToday)
    }

    fun part2(input: Input, stepsToday: Int): Long {
        val stepCounter = StepCounter(input)
        return stepCounter.getInfiniteReachableGardenPlotsCount(stepsToday)
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput, 6) == 16.toLong())
    check(part2(testInput, 5) == 13.toLong())

    val input = readInput("$packageName/input")
    part1(input, 64).println()
    part2(input, 26501365).println()
}

data class StepCounter(val map: List<String>) {
    private val enlargedMap = enlarge()
    private val n = map.size
    private val nXl = enlargedMap.size
    private val start = Position(nXl / 2, nXl / 2)

    fun getReachableGardenPlotsCount(steps: Int): Long {
        val q: Queue<Position> = LinkedList()
        q.add(start)

        for (step in 1..steps) {
            val currentPositions = q.size
            val currentAdditions = mutableSetOf<String>()
            for (i in 1..currentPositions) {
                val position = q.poll()
                for (direction in listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))) {
                    val newPosition = position.copy(i = position.i + direction.first, j = position.j + direction.second)
                    if (!newPosition.isValid()) continue
                    if (currentAdditions.contains("$newPosition")) continue
                    q.add(newPosition)
                    currentAdditions.add("$newPosition")
                }
            }
        }

        return q.size.toLong()
    }

    fun getInfiniteReachableGardenPlotsCount(steps: Int): Long {
        val cycles = (steps.toDouble() - n / 2) / n
        val data = getQuadraticData()
        val (a, b, c) = data
        return (a + cycles * (b - a + (cycles - 1) * (c - b - b + a) / 2)).toLong()
    }

    private fun enlarge(): List<String> {
        val copies = 101
        val enlargedMap = mutableListOf<String>()
        for (row in map) {
            var str = ""
            for (i in 1..copies) str += row
            enlargedMap.add(str)
        }
        for (count in 1..<copies) {
            for (i in map.indices) {
                enlargedMap.add(enlargedMap[i])
            }
        }
        return enlargedMap
    }

    private fun getQuadraticData(): List<Long> {
        val values = mutableListOf<Long>()
        val base = n / 2
        for (i in 0..2) {
            val value = getReachableGardenPlotsCount(base + i * n)
            values.add(value)
        }
        return values
    }

    private fun Position.isValid(): Boolean {
        return enlargedMap[this.i][this.j] != '#'
    }
}
