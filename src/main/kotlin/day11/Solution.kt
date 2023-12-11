package day11

import Input
import println
import readInput
import kotlin.math.abs

const val packageName = "day11"

fun main() {
    fun Input.toGalaxyImage(): GalaxyImage {
        fun isEmptyRow(i: Int): Boolean {
            for (observation in this[i]) {
                if (observation != '.') return false
            }
            return true
        }

        fun isEmptyColumn(j: Int): Boolean {
            for (i in this.indices) {
                val observation = this[i][j]
                if (observation != '.') return false
            }
            return true
        }

        val image = this.map { it.toCharArray().map { char -> char.toString() }.toMutableList() }.toMutableList()
        val m = this[0].length

        var rowsAdded = 0
        for (i in image.indices) {
            if (isEmptyRow(i)) {
                rowsAdded++
                image.add(
                    i + rowsAdded,
                    ".".repeat(m).toCharArray().map { char -> char.toString() }.toMutableList()
                )
            }
        }

        var columnsAdded = 0
        for (j in image[0].indices) {
            if (isEmptyColumn(j)) {
                columnsAdded++
                for (i in image.indices) {
                    image[i].add(j + columnsAdded, ".")
                }
            }
        }

        var starCount = 0
        for (i in image.indices) {
            for (j in image[i].indices) {
                if (image[i][j] == "#") {
                    image[i][j] = (++starCount).toString()
                }
            }
        }

        return GalaxyImage(image, starCount)
    }

    fun part1(input: Input): Int {
        val galaxyImage = input.toGalaxyImage()
        val pairs = galaxyImage.findPairs()
        val shortDistances = mutableListOf<Int>()
        for (pair in pairs) {
            shortDistances.add(galaxyImage.calculateShortestEuclideanDistance(pair.first, pair.second))
        }
        println(shortDistances)
        return shortDistances.sum()
    }

    fun part2(input: Input): Int {
        val galaxyImage = input.toGalaxyImage()
        val pairs = galaxyImage.findPairs()
        val shortDistances = mutableListOf<Int>()
        for (pair in pairs) {
            shortDistances.add(galaxyImage.calculateShortestEuclideanDistance(pair.first, pair.second))
        }
        return shortDistances.sum()
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 374)
//    check(part2(testInput) == 8410)

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

data class Position(val i: Int, val j: Int)
data class GalaxyImage(val image: List<List<String>>, val starCount: Int) {
    fun findPairs(): Set<Pair<Int, Int>> {
        val pairs = mutableSetOf<Pair<Int, Int>>()
        for (i in 1..this.starCount) {
            for (j in (i + 1)..this.starCount) {
                pairs.add(Pair(i, j))
            }
        }
        return pairs
    }

    private fun findPosition(star: Int): Position {
        val strStar = star.toString()
        for (i in image.indices) {
            for (j in image[i].indices) {
                val data = image[i][j]
                if (data == strStar) {
                    return Position(i, j)
                }
            }
        }
        return Position(-1, -1)
    }

    fun calculateShortestEuclideanDistance(star1: Int, star2: Int): Int {
        val star1Position = findPosition(star1)
        val star2Position = findPosition(star2)


        return abs(star1Position.i - star2Position.i) + abs(star1Position.j - star2Position.j)
    }
}

/**
 * 1. Expand map
 * 2. Find tuples
 * 3. For each tuple calculate the shortest distance and save it
 * 4. Return the sum of short distances
 */
