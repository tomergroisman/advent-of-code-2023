package day02

import Input
import println
import readInput

const val packageName = "day02"

fun main() {
    fun Input.getGames(): List<Game> {
        return this.map { gameData ->
            val splitGameData: MutableList<String> = gameData
                .replace(Regex("Game (\\d+):"), "$1;")
                .split("; ")
                .toMutableList()
            val id: Int = splitGameData.removeFirst().toInt()
            val reds = mutableListOf<Int>()
            val greens = mutableListOf<Int>()
            val blues = mutableListOf<Int>()
             splitGameData.forEach {set ->
                 val setData = set.split(", ")
                 setData.forEach {colorData ->
                     val (n, color) = colorData.split(' ')
                     when (color) {
                         "red" -> reds.add(n.toInt())
                         "green" -> greens.add(n.toInt())
                         "blue" -> blues.add(n.toInt())
                         else -> {}
                     }
                 }

             }

            val cubesResolved = CubesReveled(
                maxReds = reds.max(),
                maxGreens = greens.max(),
                maxBlues = blues.max()
            )
            Game(id, cubesResolved)
        }
    }

    fun part1(input: Input): Int {
        val games = input.getGames()
        val possibleGames = games.filter {
            it.cubesReveled.maxReds <= 12 &&
            it.cubesReveled.maxGreens <= 13 &&
            it.cubesReveled.maxBlues  <= 14
        }
        return possibleGames.sumOf { it.id }
    }

    fun part2(input: Input): Int {
        val games = input.getGames()
        val powerOsCubeSets = games.map {
            it.cubesReveled.maxReds *
            it.cubesReveled.maxGreens *
            it.cubesReveled.maxBlues
        }
        return powerOsCubeSets.sum()
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

data class CubesReveled(
    val maxReds: Int,
    val maxGreens: Int,
    val maxBlues: Int
);

data class Game(val id: Int, val cubesReveled: CubesReveled)
