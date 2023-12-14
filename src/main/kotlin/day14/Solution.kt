package day14

import Input
import println
import readInput

const val packageName = "day14"

fun main() {
    fun Input.toRockMap(): RockMap {
        return RockMap(this.map { it.toCharArray().toList() })
    }

    fun part1(input: Input): Long {
        val rockMap = input.toRockMap()
        return rockMap
            .simulateNorthTilt()
            .getTotalLoad()
    }

    fun part2(input: Input): Long {
        val rockMap = input.toRockMap()
        val total = rockMap
            .simulateRockCycles()
            .getTotalLoad()
        return total
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 136.toLong())
    check(part2(testInput) == 64.toLong())

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

class RockMap(map: List<List<Char>>) {
    var map: MutableList<MutableList<Char>> = map.map { it.toMutableList() }.toMutableList()
    private val n = map.size
    private val m = map[0].size
    val memo = mutableMapOf<String, MutableList<MutableList<Char>>>()

    fun getTotalLoad(): Long {
        var totalLoad: Long = 0
        for (i in map.indices) {
            totalLoad += map[i].count { it == 'O' } * (n - i)
        }
        return totalLoad
    }

    fun simulateNorthTilt(): RockMap {
        for (i in map.indices) {
            for (j in map[i].indices) {
                when (map[i][j]) {
                    '.' -> {
                        for (elevator in i..<n) {
                            when (map[elevator][j]) {
                                'O' -> {
                                    map[i][j] = 'O'
                                    map[elevator][j] = '.'
                                    break
                                }

                                '#' -> break
                            }
                        }
                    }

                    else -> continue
                }
            }
        }
        return this
    }

    private fun simulateSouthTilt() {
        for (i in n - 1 downTo 0) {
            for (j in map[i].indices) {
                when (map[i][j]) {
                    '.' -> {
                        for (elevator in i - 1 downTo 0) {
                            when (map[elevator][j]) {
                                'O' -> {
                                    map[i][j] = 'O'
                                    map[elevator][j] = '.'
                                    break
                                }

                                '#' -> break
                            }
                        }
                    }

                    else -> continue
                }
            }
        }
    }

    private fun simulateWestTilt() {
        for (j in map[0].indices) {
            for (i in map.indices) {
                when (map[i][j]) {
                    '.' -> {
                        for (elevator in j..<m) {
                            when (map[i][elevator]) {
                                'O' -> {
                                    map[i][j] = 'O'
                                    map[i][elevator] = '.'
                                    break
                                }

                                '#' -> break
                            }
                        }
                    }

                    else -> continue
                }
            }
        }
    }

    private fun simulateEastTilt() {
        for (j in m - 1 downTo 0) {
            for (i in map.indices) {
                when (map[i][j]) {
                    '.' -> {
                        for (elevator in j - 1 downTo 0) {
                            when (map[i][elevator]) {
                                'O' -> {
                                    map[i][j] = 'O'
                                    map[i][elevator] = '.'
                                    break
                                }

                                '#' -> {
                                    break
                                }
                            }
                        }
                    }

                    else -> continue
                }
            }
        }
    }

    fun simulateRockCycles(): RockMap {
        for (cycle in 1..1000) {
            simulateNorthTilt()
            simulateWestTilt()
            simulateSouthTilt()
            simulateEastTilt()
        }
        return this
    }
}
