package day01

import Input
import println
import readInput

const val packageName = "day01"

fun main() {
    fun String.getCalibrationValue(): Int {
        val value = this.replace(Regex("^[a-z]*([0-9])?.*([0-9])[a-z]*\$"), "$1$2")
        return (if (value.length == 1) "$value$value" else value).toInt()
    }

    fun part1(input: Input): Int {
        val calibrationValues = mutableListOf<Int>()
        input.forEach {line ->
            val calibrationValue = line.getCalibrationValue()
            calibrationValues.add(calibrationValue)
        }
        return calibrationValues.sum()
    }

    fun part2(input: Input): Int {
        val calibrationValues = mutableListOf<Int>()
        input.forEach {line ->
            val parsedLine = line
                .replace("one", "one1one")
                .replace("two", "two2two")
                .replace("three", "three3three")
                .replace("four", "four4four")
                .replace("five", "five5five")
                .replace("six", "six6six")
                .replace("seven", "seven7seven")
                .replace("eight", "eight8eight")
                .replace("nine", "nine9nine")

            val calibrationValue = parsedLine.getCalibrationValue()
            calibrationValues.add(calibrationValue)
        }
        return calibrationValues.sum()
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 209)
    check(part2(testInput) == 198)

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}
