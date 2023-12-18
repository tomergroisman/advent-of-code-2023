package day18

import Input
import Position
import println
import readInput
import kotlin.math.min

const val packageName = "day18"

fun main() {
    fun Input.toInstructions(): List<Instruction> {
        return this.map {
            val (direction, digString, hex) = it.replace(Regex("\\(\\)"), "").split(" ")
            Instruction(direction[0], digString.toLong(), hex)
        }
    }

    fun Input.toBuggyInstructions(): List<Instruction> {
        fun List<Long>.gcd(): Long {
            var result = this[0]
            for (i in 1 until this.size) {
                var num1 = result
                var num2 = this[i]
                while (num2 != 0.toLong()) {
                    val temp = num2
                    num2 = num1 % num2
                    num1 = temp
                }
                result = num1
            }
            return result
        }

        return this.map {
            val (_, _, hexRaw) = it.split(" ")
            val hex = hexRaw.replace(Regex("\\(#([0-9A-Za-z]+)\\)"), "$1")
            val direction = when (hex.last()) {
                '0' -> 'R'
                '1' -> 'D'
                '2' -> 'L'
                '3' -> 'U'
                else -> 'X'
            }
            val dig = java.lang.Long.parseLong(hex.dropLast(1), 16)
            Instruction(direction, dig, hex)
        }
    }

    fun part1(input: Input): Long {
        val instructions = input.toInstructions()
        return Trench(instructions).solveWithMath()
    }

    fun part2(input: Input): Long {
        val instructions = input.toBuggyInstructions()
        instructions.println()
        return Trench(instructions).solveWithMath()
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 62.toLong())
    check(part2(testInput) == 952408144115)

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

data class Trench(val instructions: List<Instruction>) {
    fun solveWithMath(): Long {
        var perimeter: Long = 0
        var area: Long = 0
        var current = Pair<Long, Long>(0, 0)

        for (instruction in instructions) {
            perimeter += instruction.dig
            val delta: Pair<Long, Long> = when (instruction.direction) {
                'R' -> Pair(0, instruction.dig)
                'D' -> Pair(instruction.dig, 0)
                'L' -> Pair(0, -instruction.dig)
                'U' -> Pair(-instruction.dig, 0)
                else -> Pair(0, 0)
            }
            println(current)
            current = Pair(current.first + delta.first, current.second + delta.second)
            area += current.second * delta.first
        }
        
        return (area + perimeter / 2) + 1
    }

    fun solveWithBruteForce(): Long {
        val border = getBorder()
        val q = mutableListOf(Position(1, 1))
        val visited = mutableSetOf<Position>()
        var count = border.size.toLong()

        while (q.isNotEmpty()) {
            val position = q.removeFirst()
            if (visited.contains(position)) continue
            if (border.contains(position)) continue

            count++
            visited.add(position)

            q.add(position.copy(i = position.i - 1, j = position.j - 1))
            q.add(position.copy(i = position.i - 1))
            q.add(position.copy(i = position.i - 1, j = position.j - 1))
            q.add(position.copy(j = position.j - 1))
            q.add(position.copy(j = position.j + 1))
            q.add(position.copy(i = position.i + 1, j = position.j - 1))
            q.add(position.copy(i = position.i + 1))
            q.add(position.copy(i = position.i + 1, j = position.j + 1))
        }

        return count
    }

    private fun getBorder(): Set<Position> {
        val border = mutableSetOf<Position>()
        val position = Position(0, 0)
        var count: Long = 0
        for (instruction in instructions) {
            val addition = {
                when (instruction.direction) {
                    'R' -> position.j++
                    'D' -> position.i++
                    'L' -> position.j--
                    'U' -> position.i--
                    else -> {}
                }
            }
            for (step in 1..instruction.dig) {
                border.add(position.copy())
                count++
                addition()
            }
        }
        return border
    }
}

data class Instruction(val direction: Char, val dig: Long, val hex: String)
