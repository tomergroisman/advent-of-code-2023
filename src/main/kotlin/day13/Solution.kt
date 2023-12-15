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

    fun List<Char>.diff(charList: List<Char>): Int {
        var diff = 0
        for (i in this.indices) {
            if (this[i] != charList[i]) {
                diff++
            }
        }
        return diff
    }

    fun Pattern.findHorizontalReflectionIndex(findSmudged: Boolean = false): Int {
        val expectedDiffs = if (findSmudged) 1 else 0
        for (i in 1..<this.size) {
            var diffs = 0
            var isMirrorCandidate = true
            for (j in 0..min(i - 1, this.size - 1 - i)) {
                val diff = this[i + j].diff(this[i - 1 - j])
                diffs += diff
                if (diff > expectedDiffs) {
                    isMirrorCandidate = false
                    break
                }
            }
            if (isMirrorCandidate && diffs == expectedDiffs) return i
        }
        return 0
    }

    fun Pattern.findVerticalReflectionIndex(findSmudged: Boolean = false): Int {
        val cols = this[0].size
        val rows = this.size
        val transposedPattern = List(cols) { j ->
            List(rows) { i -> this[i][j] }
        }
        return transposedPattern.findHorizontalReflectionIndex(findSmudged)
    }

    fun part1(input: Input): Long {
        val patterns = input.toPatterns()
        return patterns.sumOf {
            val verticalReflectionIndex = it.findVerticalReflectionIndex()
            val horizontalReflectionIndex = it.findHorizontalReflectionIndex()
            ReflectionLine(verticalReflectionIndex, horizontalReflectionIndex).summarize
        }
    }

    fun part2(input: Input): Long {
        val patterns = input.toPatterns()
        return patterns.sumOf {
            val verticalReflectionIndex = it.findVerticalReflectionIndex(true)
            val horizontalReflectionIndex = it.findHorizontalReflectionIndex(true)
            ReflectionLine(verticalReflectionIndex, horizontalReflectionIndex).summarize
        }
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 405.toLong())
    check(part2(testInput) == 400.toLong())

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

typealias Pattern = List<List<Char>>

data class ReflectionLine(val vertical: Int, val horizontal: Int) {
    val summarize = (vertical + 100 * horizontal).toLong()
}
