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

    fun List<Char>.isPointOfReflection(i: Int): Boolean {
        val distanceFromStart = i + 1
        val distanceFromEnd = this.size - i - 1
        val distanceFromEdge = min(distanceFromStart, distanceFromEnd)
        for (j in 0..<distanceFromEdge) {
            val char1 = this[i + j + 1]
            val char2 = this[i - j]
            if (char1 != char2) return false
        }
        return true
    }

    fun Pattern.findVerticalReflectionIndex(): Int {
        val perfectReflectionPointsCandidates = mutableSetOf<Int>()
        for (row in this) {
            for (i in 1..<row.size - 1) {
                if (row.isPointOfReflection(i)) {
                    perfectReflectionPointsCandidates.add(i)
                }
            }
        }

        for (candidate in perfectReflectionPointsCandidates)
            if (this.all { it.isPointOfReflection(candidate) }) return candidate + 1
        return 0
    }

    fun Pattern.findHorizontalReflectionIndex(): Int {
        val cols = this[0].size
        val rows = this.size
        val transposedPattern = List(cols) { j ->
            List(rows) { i -> this[i][j] }
        }
        return transposedPattern.findVerticalReflectionIndex()
    }


    fun part1(input: Input): Long {
        val patterns = input.toPatterns()
        println(patterns.map {
            val verticalReflectionIndex = it.findVerticalReflectionIndex()
            val horizontalReflectionIndex = it.findHorizontalReflectionIndex()
            println("$verticalReflectionIndex $horizontalReflectionIndex")
            (verticalReflectionIndex + 100 * horizontalReflectionIndex).toLong()
        })
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

// Tries: 5386, 85014, 6606, 31368
