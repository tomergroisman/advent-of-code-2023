package day13

import Input
import println
import readInput
import kotlin.math.min

const val packageName = "day13"

fun main() {
    fun Input.toPatterns(): List<Pattern> {
        val patterns = mutableListOf<Pattern>()
        var pattern = mutableListOf<List<Char>>()
        for (row in this) {
            if (row.isEmpty()) {
                patterns.add(pattern)
                pattern = mutableListOf()
            } else {
                pattern.add(row.toCharArray().toList())
            }
        }
        patterns.add(pattern)
        return patterns
    }

    fun Pattern.findHorizontalReflectionIndex(): Int {
        for (i in 1..<this.size) {
            if (this[i] == this[i - 1]) {
                var isMirror = true
                for (j in 1..min(i - 1, this.size - 1 - i)) {
                    if (this[i + j] != this[i - 1 - j]) {
                        isMirror = false
                        break
                    }
                }
                if (isMirror) return i
            }
        }
        return 0
    }

    fun Pattern.findVerticalReflectionIndex(): Int {
        val cols = this[0].size
        val rows = this.size
        val transposedPattern = List(cols) { j ->
            List(rows) { i -> this[i][j] }
        }
        return transposedPattern.findHorizontalReflectionIndex()
    }

    fun part1(input: Input): Long {
        val patterns = input.toPatterns()
        return patterns.sumOf {
            val verticalReflectionIndex = it.findVerticalReflectionIndex()
            val horizontalReflectionIndex = it.findHorizontalReflectionIndex()
            (verticalReflectionIndex + 100 * horizontalReflectionIndex).toLong()
        }
    }

    fun part2(input: Input): Int {
        return input.size
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 405.toLong())

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

typealias Pattern = List<List<Char>>
