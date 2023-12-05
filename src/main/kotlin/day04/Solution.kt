package day04

import Input
import println
import readInput
import kotlin.math.pow

const val packageName = "day04"

fun main() {
    fun String.toScratchcardNumbersSet(): Set<Int> {
        val set = mutableSetOf<Int>()
        this.trim()
            .replace(Regex("\\s+"), " ")
            .split(" ")
            .forEach { set.add(it.toInt()) }
        return set
    }

    fun Input.toScratchcards(): List<Scratchcard> {
        return this.map {
            val (cardNumberString, winningNumbersString, myNumbersString) = it
                .replace(Regex("Card \\s*([0-9]+): "), "$1 | ")
                .split(" | ")
            Scratchcard(
                cardNumber = cardNumberString.toInt(),
                winningNumbers = winningNumbersString.toScratchcardNumbersSet(),
                myNumbers = myNumbersString.toScratchcardNumbersSet()
            )
        }
    }

    fun MutableMap<Int, Int>.increment(key: Int) {
        if (this[key] == null) {
            this[key] = 0
        }
        this[key] = this[key]!! + 1

    }

    fun List<Scratchcard>.getPrize(): Map<Int, Int> {
        val ownedCards = mutableMapOf<Int, Int>()
        val copy = this.toMutableList()
        while (copy.isNotEmpty()) {
            val scratchcard = copy.removeFirst()
            val cardNumber = scratchcard.cardNumber
            ownedCards.increment(cardNumber)
            val matchingNumbers = scratchcard.getNumberOfMatchingNumbers()
            for (i in 1..ownedCards[cardNumber]!!) {
                for (j in 1..matchingNumbers) {
                    val copyCardNumber = cardNumber + j
                    ownedCards.increment(copyCardNumber)
                }
            }
        }
        return ownedCards
    }

    fun part1(input: Input): Int {
        val scratchcards = input.toScratchcards()
        return scratchcards.sumOf { it.getPoints() }
    }

    fun part2(input: Input): Int {
        val scratchcards = input.toScratchcards()
        return scratchcards.getPrize().values.sum()
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

class Scratchcard(val cardNumber: Int, val winningNumbers: Set<Int>, val myNumbers: Set<Int>) {
    fun getNumberOfMatchingNumbers(): Int {
        var numberOfMatchingNumbers = 0
        myNumbers.forEach {
            if (winningNumbers.contains(it)) {
                numberOfMatchingNumbers++
            }
        }
        return numberOfMatchingNumbers
    }

    fun getPoints(): Int {
        val matchingNumbers = getNumberOfMatchingNumbers()
        if (matchingNumbers == 0) return 0
        return 2.0.pow((matchingNumbers - 1).toDouble()).toInt()
    }
}
