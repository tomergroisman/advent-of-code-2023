package day16

import Input
import println
import readInput
import kotlin.math.max

const val packageName = "day16"

fun main() {
    fun part1(input: Input): Int {
        return Contraption(input).getEnergizedTiles()
    }

    fun part2(input: Input): Int {
        println(Contraption(input).getMaxEnergizedTiles())
        return Contraption(input).getMaxEnergizedTiles()
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 46)
    check(part2(testInput) == 51)

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

data class Contraption(val input: Input) {
    val map = input.map { it.toCharArray().toList() }
    private val n = map.size
    private val m = map[0].size
    private val directedVisited = mutableSetOf<String>()
    private val visited = mutableSetOf<String>()

    fun getEnergizedTiles(): Int {
        energize(0, 0, Direction.Right)
        return visited.size
    }

    fun getMaxEnergizedTiles(): Int {
        var maxEnergizedTiles = 0
        for (direction in Direction.entries) {
            when (direction) {
                Direction.Right -> {
                    for (i in 0..<n) {
                        visited.clear()
                        directedVisited.clear()
                        energize(i, 0, direction)
                        maxEnergizedTiles = max(maxEnergizedTiles, visited.size)
                    }
                }

                Direction.Left -> {
                    for (i in 0..<n) {
                        visited.clear()
                        directedVisited.clear()
                        energize(i, m - 1, direction)
                        maxEnergizedTiles = max(maxEnergizedTiles, visited.size)
                    }
                }

                Direction.Down -> {
                    for (j in 0..<m) {
                        visited.clear()
                        directedVisited.clear()
                        energize(0, j, direction)
                        maxEnergizedTiles = max(maxEnergizedTiles, visited.size)
                    }
                }

                Direction.Up -> {
                    for (j in 0..<m) {
                        visited.clear()
                        directedVisited.clear()
                        energize(n - 1, j, direction)
                        maxEnergizedTiles = max(maxEnergizedTiles, visited.size)
                    }
                }
            }
        }
        return maxEnergizedTiles
    }

    private fun energize(i: Int, j: Int, direction: Direction) {
        val key = generateKey(i, j)
        val directedKey = generateDirectedKey(i, j, direction)
        if (i < 0 || i >= n || j < 0 || j >= m || directedVisited.contains(directedKey)) return

        visited.add(key)
        directedVisited.add(directedKey)

        when (map[i][j]) {
            '.' -> {
                when (direction) {
                    Direction.Up -> energize(i - 1, j, Direction.Up)
                    Direction.Down -> energize(i + 1, j, Direction.Down)
                    Direction.Left -> energize(i, j - 1, Direction.Left)
                    Direction.Right -> energize(i, j + 1, Direction.Right)
                }
            }

            '/' -> {
                when (direction) {
                    Direction.Up -> energize(i, j + 1, Direction.Right)
                    Direction.Down -> energize(i, j - 1, Direction.Left)
                    Direction.Left -> energize(i + 1, j, Direction.Down)
                    Direction.Right -> energize(i - 1, j, Direction.Up)
                }
            }

            '\\' -> {
                when (direction) {
                    Direction.Up -> energize(i, j - 1, Direction.Left)
                    Direction.Down -> energize(i, j + 1, Direction.Right)
                    Direction.Left -> energize(i - 1, j, Direction.Up)
                    Direction.Right -> energize(i + 1, j, Direction.Down)
                }
            }

            '-' -> {
                when (direction) {
                    Direction.Up -> {
                        energize(i, j - 1, Direction.Left)
                        energize(i, j + 1, Direction.Right)
                    }

                    Direction.Down -> {
                        energize(i, j - 1, Direction.Left)
                        energize(i, j + 1, Direction.Right)
                    }

                    Direction.Left -> energize(i, j - 1, Direction.Left)
                    Direction.Right -> energize(i, j + 1, Direction.Right)
                }
            }

            '|' -> {
                when (direction) {
                    Direction.Up -> energize(i - 1, j, Direction.Up)
                    Direction.Down -> energize(i + 1, j, Direction.Down)

                    Direction.Left -> {
                        energize(i - 1, j, Direction.Up)
                        energize(i + 1, j, Direction.Down)
                    }

                    Direction.Right -> {
                        energize(i - 1, j, Direction.Up)
                        energize(i + 1, j, Direction.Down)
                    }
                }
            }
        }
    }

    private fun generateKey(i: Int, j: Int): String {
        return "$i,$j"
    }

    private fun generateDirectedKey(i: Int, j: Int, direction: Direction): String {
        return "$i,$j,$direction"
    }
}

enum class Direction { Up, Down, Left, Right }
