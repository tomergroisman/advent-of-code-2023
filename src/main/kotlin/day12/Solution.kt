package day12

import Input
import kotlinx.coroutines.*
import println
import readInput
import kotlin.math.max
import kotlin.math.pow
import kotlin.time.measureTime

const val packageName = "day12"

fun main() {
    fun Input.toDamagedSprings(): List<DamagedSpring> {
        return this.map {
            val (mapString, legendString) = it.split(" ")
            val map = mapString.replace(Regex("\\.+"), ".")
                .toCharArray()
                .toMutableList()
            val legend = legendString
                .split(",")
                .map { strNum -> strNum.toInt() }
                .toMutableList()
            DamagedSpring(map, legend)
        }
    }

    fun part1(input: Input): Long {
        val damagedSprings = input.toDamagedSprings()
        return damagedSprings.sumOf { it.getNumberOfValidOptions() }
    }

    fun part2(input: Input): Long {
        val damagedSprings = input.toDamagedSprings()
        return damagedSprings.sumOf { it.getEnlargedNumberOfOptions() }
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 21.toLong())
    check(part2(testInput) == 525152.toLong())

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

data class DamagedSpring(val map: MutableList<Char>, val legend: MutableList<Int>) {
    private val memo = mutableMapOf<String, Long>()

    private fun getValidOptions(mapIndex: Int, legendIndex: Int, currentLength: Int): Long {
        val key = "$mapIndex,$legendIndex,$currentLength"
        if (memo[key] != null) return memo[key]!!
        if (mapIndex == map.size) {
            return if (legendIndex == legend.size && currentLength == 0) 1
            else if (legendIndex == legend.size - 1 && legend[legendIndex] == currentLength) 1
            else 0
        }

        var validOptions = 0.toLong()
        for (char in listOf('.', '#')) {
            if (map[mapIndex] == char || map[mapIndex] == '?') {
                if (char == '.' && currentLength == 0) {
                    validOptions += getValidOptions(mapIndex + 1, legendIndex, 0)
                } else if (char == '.' && currentLength > 0 && legendIndex < legend.size && legend[legendIndex] == currentLength) {
                    validOptions += getValidOptions(mapIndex + 1, legendIndex + 1, 0)
                } else if (char == '#') {
                    validOptions += getValidOptions(mapIndex + 1, legendIndex, currentLength + 1)
                }
            }
        }
        memo[key] = validOptions
        return validOptions
    }

    fun getNumberOfValidOptions(): Long {
        return getValidOptions(0, 0, 0)
    }

    fun getEnlargedNumberOfOptions(): Long {
        memo.clear()
        val enlargedMap = map.toMutableList()
        val enlargedLegend = legend.toMutableList()
        for (i in 1..<5) {
            enlargedMap.add('?')
            enlargedMap.addAll(map)
            enlargedLegend.addAll(legend)
        }

        val enlargedDamagedSpring = DamagedSpring(enlargedMap, enlargedLegend)
        return enlargedDamagedSpring.getValidOptions(0, 0, 0)
    }
}
