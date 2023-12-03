package day03

import println
import readInput

const val packageName = "day03"

fun main() {
    fun convertEngineSchematic(input: List<String>): EngineSchematic {
        val cols = input[0].length
        val paddedCols = cols + 2

        fun MutableList<MutableList<Char>>.addDummyRow() {
            val row = mutableListOf<Char>()
            for (i in 1..paddedCols) {
                row.add('.')
            }
                this.add(row)
        }

        val engineSchematic = mutableListOf<MutableList<Char>>()

        engineSchematic.addDummyRow()
        for (row in input) {
            val paddedRow = mutableListOf<Char>()
            paddedRow.add('.')
            paddedRow.addAll(row.toList())
            paddedRow.add('.')
            engineSchematic.add(paddedRow)
        }
        engineSchematic.addDummyRow()
        return engineSchematic
    }

    fun isDigit(digit: Char) = Regex("[0-9]").matches(digit.toString())

    fun EngineSchematic.collectEnginePartNumber(i: Int, j: Int): Int {
        val digits = mutableListOf<Int>()
        val row = this[i]

        var currentIndex = j
        while (isDigit(row[currentIndex])) {
            digits.add(row[currentIndex].toString().toInt())
            row[currentIndex] = '.'
            currentIndex++
        }
        currentIndex = j - 1
        while (isDigit(row[currentIndex])) {
            digits.add(0, row[currentIndex].toString().toInt())
            row[currentIndex] = '.'
            currentIndex--
        }
        return digits.joinToString("").toInt()
    }

    fun EngineSchematic.getSymbolAdjacentNumbers(i: Int, j: Int): List<Int> {
        val adjacentNumbers = mutableListOf<Int>()
        if (isDigit(this[i - 1][j - 1])) adjacentNumbers.add(collectEnginePartNumber(i - 1, j - 1))
        if (isDigit(this[i - 1][j])) adjacentNumbers.add(collectEnginePartNumber(i - 1, j))
        if (isDigit(this[i - 1][j + 1])) adjacentNumbers.add(collectEnginePartNumber(i - 1, j + 1))
        if (isDigit(this[i][j - 1])) adjacentNumbers.add(collectEnginePartNumber(i, j - 1))
        if (isDigit(this[i][j + 1])) adjacentNumbers.add(collectEnginePartNumber(i, j + 1))
        if (isDigit(this[i + 1][j - 1])) adjacentNumbers.add(collectEnginePartNumber(i + 1, j - 1))
        if (isDigit(this[i + 1][j])) adjacentNumbers.add(collectEnginePartNumber(i + 1, j))
        if (isDigit(this[i + 1][j + 1])) adjacentNumbers.add(collectEnginePartNumber(i + 1, j + 1))
        return adjacentNumbers
    }

    fun EngineSchematic.forEachSymbolAdjacentNumbers(action: (List<Int>) -> Unit): Unit {
        for (i in this.indices) {
            val row = this[i]
            for (j in row.indices) {
                val char = row[j]
                val isSymbol = Regex("[^0-9.]").matches(char.toString())
                if (isSymbol) {
                    val adjacentNumbers = this.getSymbolAdjacentNumbers(i, j)
                    action(adjacentNumbers)
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val engineSchematic = convertEngineSchematic(input)
        val engineParts = mutableListOf<Int>()

        engineSchematic.forEachSymbolAdjacentNumbers { engineParts.addAll(it) }

        return engineParts.sum()
    }

    fun part2(input: List<String>): Int {
        val engineSchematic = convertEngineSchematic(input)
        val gearRatios = mutableListOf<Int>()

        engineSchematic.forEachSymbolAdjacentNumbers {
            if (it.size == 2) {
                val gearRatio = it.reduce { prev, current ->  prev * current }
                gearRatios.add(gearRatio)
            }
        }

        return gearRatios.sum()
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

typealias EngineSchematic = List<MutableList<Char>>
