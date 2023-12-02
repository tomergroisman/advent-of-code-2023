package day01

import println
import readInput

const val packageName = "day01"

fun main() {
    fun getCalibrationValue(line: String): Int {
        val value = line.replace(Regex("^[a-z]*([0-9])?.*([0-9])[a-z]*\$"), "$1$2")
        return (if (value.length == 1) "$value$value" else value).toInt()
    }

    fun part1(lines: List<String>): Int {
        val calibrationValues = mutableListOf<Int>()
        lines.forEach {line ->
            val calibrationValue = getCalibrationValue(line)
            calibrationValues.add(calibrationValue)
        }
        return calibrationValues.sum()
    }

    fun part2(lines: List<String>): Int {
        val calibrationValues = mutableListOf<Int>()
        val regex = Regex("(one)*(two)*(three)*(four)*(five)*(six)*(seven)*(eight)*(nine)*")
        lines.forEach {line ->
            val parsedLine = regex.replace(line) {
                when (it.value) {
                    "one" -> "1"
                    "two" -> "2"
                    "three" -> "3"
                    "four" -> "4"
                    "five" -> "5"
                    "six" -> "6"
                    "seven" -> "7"
                    "eight" -> "8"
                    "nine" -> "9"
                    else -> ""
                }
            }

            val calibrationValue = getCalibrationValue(parsedLine)
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
