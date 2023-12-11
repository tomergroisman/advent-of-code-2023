package day11

import Input
import println
import readInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

const val packageName = "day11"

fun main() {
    fun Input.toGalaxyImage(expansion: Int = 1): GalaxyImage {
        val image = this.map { it.toCharArray().map { char -> char.toString() }.toMutableList() }.toMutableList()
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

    fun part1(input: Input): Long {
        val galaxyImage = input.toGalaxyImage()
        val pairs = galaxyImage.findPairs()
        val shortDistances = mutableListOf<Long>()
        for (pair in pairs) {
            shortDistances.add(galaxyImage.calculateShortestEuclideanDistance(pair))
        }
        return shortDistances.sum()
    }

    fun part2(input: Input, expandRate: Int = 1000000): Long {
        val galaxyImage = input.toGalaxyImage()
        val pairs = galaxyImage.findPairs()
        val shortDistances = mutableListOf<Long>()
        for (pair in pairs) {
            shortDistances.add(galaxyImage.calculateShortestEuclideanDistance(pair, expandRate - 1))
        }
        return shortDistances.sum()
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 374.toLong())
    check(part2(testInput, 100) == 8410.toLong())

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

typealias StarPair = Pair<Int, Int>

data class Position(val i: Int, val j: Int)
data class GalaxyImage(val image: List<List<String>>, val starCount: Int) {
    fun findPairs(): Set<Pair<Int, Int>> {
        val pairs = mutableSetOf<StarPair>()
        for (i in 1..this.starCount) {
            for (j in (i + 1)..this.starCount) {
                pairs.add(Pair(i, j))
            }
        }
        return pairs
    }


    fun calculateShortestEuclideanDistance(starPair: StarPair, multiplier: Int = 1): Long {
        val star1Position = findPosition(starPair.first)
        val star2Position = findPosition(starPair.second)
        val nEmptyRowsBetweenStars =
            getNEmptyRowsBetweenRows(min(star1Position.i, star2Position.i), max(star1Position.i, star2Position.i))
        val nEmptyColumnsBetweenStars =
            getNEmptyColumnsBetweenColumns(min(star1Position.j, star2Position.j), max(star1Position.j, star2Position.j))

        return abs(star1Position.i - star2Position.i) +
                abs(star1Position.j - star2Position.j) +
                (nEmptyRowsBetweenStars * multiplier) +
                (nEmptyColumnsBetweenStars * multiplier)
    }

    private fun getNEmptyRowsBetweenRows(row1: Int, row2: Int): Long {
        var nEmptyRows = 0;
        for (i in row1..row2) {
            if (isEmptyRow(i)) {
                nEmptyRows++;
            }
        }
        return nEmptyRows.toLong()
    }

    private fun getNEmptyColumnsBetweenColumns(col1: Int, col2: Int): Long {
        var nEmptyColumns = 0;
        for (j in col1..col2) {
            if (isEmptyColumn(j)) {
                nEmptyColumns++;
            }
        }
        return nEmptyColumns.toLong()
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


    private fun isEmptyRow(i: Int): Boolean {
        for (observation in image[i]) {
            if (observation != ".") return false
        }
        return true
    }

    private fun isEmptyColumn(j: Int): Boolean {
        for (i in image.indices) {
            val observation = image[i][j]
            if (observation != ".") return false
        }
        return true
    }
}

/**
 * 1. Expand map
 * 2. Find tuples
 * 3. For each tuple calculate the shortest distance and save it
 * 4. Return the sum of short distances
 */
