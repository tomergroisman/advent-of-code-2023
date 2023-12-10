package day10

import Input
import println
import readInput

const val packageName = "day10"

fun main() {
    fun Input.toMazeMap(): MazeMap {
        val paddedEmptyRow = mutableListOf<Char>()
        for (i in 1..this.size + 2) {
            paddedEmptyRow.add('.')
        }
        val paddedInput = mutableListOf(paddedEmptyRow)
        for (row in this) {
            val paddedRow = mutableListOf('.')
            paddedRow.addAll(row.toCharArray().toList())
            paddedRow.add('.')
            paddedInput.add(paddedRow)
        }
        paddedInput.add(paddedEmptyRow)
        return paddedInput
    }

    fun part1(input: Input): Long {
        val maze = Maze(input.toMazeMap())
        val loopSteps = maze.solve()
        println(loopSteps)
        return loopSteps / 2
    }

    fun part2(input: Input): Int {
        return input.size
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 8.toLong())

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

typealias MazeMap = MutableList<MutableList<Char>>

data class Position(var x: Int, var y: Int)
class Maze(map: MazeMap) {
    val map = map.toMutableList()

    private fun findStart(): Position {
        for (i in map.indices) {
            val row = map[i]
            for (j in row.indices) {
                val char = row[j]
                if (char == 'S') {
                    return Position(j, i)
                }
            }
        }
        return Position(-1, -1)
    }

    private fun isConnected(source: Char, destination: Char, destinationDirection: Direction): Boolean {
        when (destinationDirection) {
            Direction.north -> {
                val acceptingDestinations = listOf('|', '7', 'F', 'S')
                return when (source) {
                    'S' -> acceptingDestinations.contains(destination)
                    '|' -> acceptingDestinations.contains(destination)
                    'L' -> acceptingDestinations.contains(destination)
                    'J' -> acceptingDestinations.contains(destination)
                    else -> false
                }
            }

            Direction.east -> {
                val acceptingDestinations = listOf('-', '7', 'J', 'S')
                return when (source) {
                    'S' -> acceptingDestinations.contains(destination)
                    '-' -> acceptingDestinations.contains(destination)
                    'L' -> acceptingDestinations.contains(destination)
                    'F' -> acceptingDestinations.contains(destination)
                    else -> false
                }
            }

            Direction.south -> {
                val acceptingDestinations = listOf('|', 'L', 'J', 'S')
                return when (source) {
                    'S' -> acceptingDestinations.contains(destination)
                    '|' -> acceptingDestinations.contains(destination)
                    'F' -> acceptingDestinations.contains(destination)
                    '7' -> acceptingDestinations.contains(destination)
                    else -> false
                }
            }

            Direction.west -> {
                val acceptingDestinations = listOf('-', 'L', 'F', 'S')
                return when (source) {
                    'S' -> acceptingDestinations.contains(destination)
                    '-' -> acceptingDestinations.contains(destination)
                    'J' -> acceptingDestinations.contains(destination)
                    '7' -> acceptingDestinations.contains(destination)
                    else -> false
                }
            }

        }
    }

    fun solve(): Long {
        var position = findStart()
        println(position)
        var steps = 0.toLong()

        do {
            val source = map[position.y][position.x]
            for (direction in Direction.entries) {
                val destinationPosition = position.copy()
                when (direction) {
                    Direction.north -> destinationPosition.y -= 1
                    Direction.east -> destinationPosition.x += 1
                    Direction.south -> destinationPosition.y += 1
                    Direction.west -> destinationPosition.x -= 1
                }
                val destination = map[destinationPosition.y][destinationPosition.x]
                if (isConnected(source, destination, direction)) {
                    if (source != 'S') {
                        map[position.y][position.x] = 'X'
                    }
                    position = destinationPosition
                    steps++
                    break
                }
            }
        } while (map[position.y][position.x] != 'S')
        return steps
    }
}

enum class Direction { north, east, south, west }
