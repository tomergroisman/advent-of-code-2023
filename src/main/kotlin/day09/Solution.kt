package day09

import Input
import println
import readInput
import kotlin.math.abs

const val packageName = "day09"

fun main() {
    fun ValueHistory.toDiffSequences(): List<DiffSequence> {
        var currentSequence = this.toList()
        val diffSequences = mutableListOf(currentSequence)
        do {
            val diffSequence = mutableListOf<Long>()
            for (i in 0..<currentSequence.size - 1) {
                diffSequence.add(currentSequence[i + 1] - currentSequence[i])
            }
            diffSequences.add(diffSequence)
            currentSequence = diffSequence
        } while (currentSequence.toSet().size > 1)

        return diffSequences
    }

    fun ValueHistory.findNext(): Long {
        val diffSequences = this.toDiffSequences()
        return diffSequences.sumOf { it.last() }
    }

    fun ValueHistory.findPrev(): Long {
        val diffSequences = this.toDiffSequences().reversed()
        val firstValues = diffSequences.map { it.first() }
        var prevValue = 0.toLong()
        for (value in firstValues) {
            prevValue = value - prevValue
        }
        return prevValue
    }

    fun Input.toValueHistory(): List<ValueHistory> = this.map { sequence -> sequence.split(" ").map { it.toLong() } }

    fun part1(input: Input): Long {
        val valueHistory = input.toValueHistory()
        return valueHistory.sumOf { it.findNext() }
    }

    fun part2(input: Input): Long {
        val valueHistory = input.toValueHistory()
        return valueHistory.sumOf { it.findPrev() }
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 114.toLong())
    check(part2(testInput) == 2.toLong())

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

typealias ValueHistory = List<Long>
typealias DiffSequence = List<Long>

/**
 * tries:
 */
