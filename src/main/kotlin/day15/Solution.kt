package day15

import Input
import println
import readInput

const val packageName = "day15"

fun main() {
    fun Input.toStringList(): List<String> {
        return this[0].split(',')
    }

    fun Input.toLensOperations(): List<LensOperation> {
        val stringList = this.toStringList()
        return stringList.map {
            val (label, operation, parameterStr) = Regex("([A-Za-z]+)([=-])([0-9])?").find(it)!!.groupValues.drop(1)
            LensOperation(label, operation[0], parameterStr.toIntOrNull())
        }
    }

    fun part1(input: Input): Int {
        return input.toStringList().sumOf { hash(it) }
    }

    fun part2(input: Input): Int {
        val lensOperations = input.toLensOperations()
        val boxes = List(256) { Box() }
        for (lensOperation in lensOperations) {
            when (lensOperation.operation) {
                '-' -> boxes[lensOperation.box].lenses.removeIf { it.label == lensOperation.label }
                '=' -> {
                    val lens = boxes[lensOperation.box].lenses.find { it.label == lensOperation.label }
                    if (lens == null) {
                        boxes[lensOperation.box].lenses.add(Lens(lensOperation.label, lensOperation.focalLength!!))
                    } else {
                        lens.focalLength = lensOperation.focalLength!!
                    }
                }
            }
        }
        return boxes.mapIndexed { boxIndex, box ->
            (boxIndex + 1) * box.lenses.mapIndexed { lensIndex, lens -> (lensIndex + 1) * lens.focalLength }.sum()
        }.sum()
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145)

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

fun hash(str: String): Int {
    var value = 0
    for (char in str) {
        value = ((value + char.code) * 17) % 256
    }
    return value
}

data class LensOperation(val label: String, val operation: Char, val focalLength: Int?) {
    val box = hash(label)

    override fun toString(): String {
        return "label=$label, operation=$operation, focalLength=$focalLength, box=$box"
    }
}

data class Lens(val label: String, var focalLength: Int)

data class Box(val lenses: MutableList<Lens> = mutableListOf())
