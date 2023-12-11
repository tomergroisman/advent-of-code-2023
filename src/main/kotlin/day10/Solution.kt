package day10

import Input
import println
import readInput

const val packageName = "day10"

fun main() {
    fun Input.toMazeMap(): MazeMap {
        val paddedEmptyRow = mutableListOf<Char>()
        for (i in 1..this[0].length + 2) {
            paddedEmptyRow.add('X')
        }
        val paddedInput = mutableListOf(paddedEmptyRow)
        for (row in this) {
            val paddedRow = mutableListOf('X')
            paddedRow.addAll(row.toCharArray().toList())
            paddedRow.add('X')
            paddedInput.add(paddedRow)
        }
        paddedInput.add(paddedEmptyRow)
        return paddedInput
    }

    fun part1(input: Input): Long {
        val maze = Maze(input.toMazeMap())
        val loopSteps = maze.solve()
        return loopSteps / 2
    }

    fun part2(input: Input): Long {
        val maze = Maze(input.toMazeMap())
        return maze.countInnerTies()
    }

    val testInput = readInput("$packageName/input_test")
    check(part2(testInput) == 10.toLong())

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

typealias MazeMap = MutableList<MutableList<Char>>

data class Position(var x: Int, var y: Int)
class Maze(map: MazeMap) {
    private val originalMap = map
    private val pipeMap = map.map { it.map { char -> char }.toMutableList() }.toMutableList()
    private val innerTileMap = map.map { it.map { char -> char }.toMutableList() }.toMutableList()

    private fun findStart(): Position {
        for (i in originalMap.indices) {
            val row = originalMap[i]
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
                val acceptingDestinations = listOf('|', '7', 'F')
                return when (source) {
                    'S' -> acceptingDestinations.contains(destination)
                    '|' -> acceptingDestinations.contains(destination)
                    'L' -> acceptingDestinations.contains(destination)
                    'J' -> acceptingDestinations.contains(destination)
                    else -> false
                }
            }

            Direction.east -> {
                val acceptingDestinations = listOf('-', '7', 'J')
                return when (source) {
                    'S' -> acceptingDestinations.contains(destination)
                    '-' -> acceptingDestinations.contains(destination)
                    'L' -> acceptingDestinations.contains(destination)
                    'F' -> acceptingDestinations.contains(destination)
                    else -> false
                }
            }

            Direction.south -> {
                val acceptingDestinations = listOf('|', 'L', 'J')
                return when (source) {
                    'S' -> acceptingDestinations.contains(destination)
                    '|' -> acceptingDestinations.contains(destination)
                    'F' -> acceptingDestinations.contains(destination)
                    '7' -> acceptingDestinations.contains(destination)
                    else -> false
                }
            }

            Direction.west -> {
                val acceptingDestinations = listOf('-', 'L', 'F')
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
        var steps = 0.toLong()

        do {
            val source = pipeMap[position.y][position.x]
            var hasConnected = false
            for (direction in Direction.entries) {
                val destinationPosition = position.copy()
                when (direction) {
                    Direction.north -> destinationPosition.y -= 1
                    Direction.east -> destinationPosition.x += 1
                    Direction.south -> destinationPosition.y += 1
                    Direction.west -> destinationPosition.x -= 1
                }
                val destination = pipeMap[destinationPosition.y][destinationPosition.x]
                if (isConnected(source, destination, direction)) {
                    hasConnected = true
                    pipeMap[position.y][position.x] = 'X'
                    position = destinationPosition
                    steps++
                    break
                };
            }
        } while (hasConnected)
        pipeMap[position.y][position.x] = 'X'

        return steps
    }

    fun countInnerTies(): Long {
        solve()

        var innerTiles = 0.toLong()

        for (i in originalMap.indices) {
            var isInside = false
            var lastBendChar: Char? = null
            val row = originalMap[i]
            for (j in row.indices) {
                val isPipe = pipeMap[i][j] == 'X'
                fun incrementInnerTiles() {
                    if (!isPipe) {
                        innerTiles++
                        innerTileMap[i][j] = 'I'
                    }
                }

                when (val char = originalMap[i][j]) {
                    '|' -> if (isPipe) isInside = !isInside else if (isInside) incrementInnerTiles()

                    'F' -> if (isPipe) lastBendChar = char else if (isInside) incrementInnerTiles()

                    'J' -> {
                        if (isPipe && lastBendChar == 'F') {
                            isInside = !isInside
                            lastBendChar = char
                        } else if (isInside) incrementInnerTiles()
                    }

                    'L' -> if (isPipe) lastBendChar = char else if (isInside) incrementInnerTiles()

                    '7' -> {
                        if (isPipe && lastBendChar == 'L') {
                            isInside = !isInside
                            lastBendChar = char
                        } else if (isInside) incrementInnerTiles()
                    }

                    else -> if (isInside) incrementInnerTiles()
                }
            }
        }
        return innerTiles
    }
}

enum class Direction { north, east, south, west }
