package template

import println
import readInput

const val packageName = "template"

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 1)

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}
