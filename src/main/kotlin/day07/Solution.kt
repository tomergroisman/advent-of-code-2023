package day07

import Input
import println
import readInput

const val packageName = "day07"

fun main() {
    fun Input.toHands(): List<Hand> {
        return this.map {
            val (cards, bidString) = it.split(" ")
            Hand(cards, bidString.toInt(), false)
        }
    }

    fun Input.toJokerHands(): List<Hand> {
        return this.map {
            val (cards, bidString) = it.split(" ")
            Hand(cards, bidString.toInt(), true)
        }
    }

    fun List<Hand>.println() {
        this.forEach { it.println() }
    }

    fun part1(input: Input): Int {
        val hands = input.toHands()
        val ranks = hands.sortedWith(Hand.comparator)
        return ranks.map { it.bid }.reduceIndexed { index, acc, bid -> acc + (index + 1) * bid }
    }

    fun part2(input: Input): Int {
        cardValueByCardSymbol['J'] = 1
        val hands = input.toJokerHands()
        val ranks = hands.sortedWith(Hand.comparator)
        ranks.println()
        return ranks.map { it.bid }.reduceIndexed { index, acc, bid -> acc + (index + 1) * bid }
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

class Hand(val cards: String, val bid: Int, val isJokerHand: Boolean) {
    companion object {
        val comparator = Comparator<Hand> { hand1, hand2 ->
            val hand1Rank = handTypeRankByHandType[hand1.type]!!
            val hand2Rank = handTypeRankByHandType[hand2.type]!!
            if (hand1Rank != hand2Rank) {
                return@Comparator hand2Rank - hand1Rank
            }

            for (i in 0..<hand1.cards.length) {
                val hand1CardValue = cardValueByCardSymbol[hand1.cards[i]]!!
                val hand2CardValue = cardValueByCardSymbol[hand2.cards[i]]!!
                if (hand1CardValue != hand2CardValue) {
                    return@Comparator hand1CardValue - hand2CardValue
                }
            }
            return@Comparator 0

        }
    }

    val type: HandType = calculateHandType()

    private fun calculateHandType(): HandType {
        val occurrencesMap = mutableMapOf<Char, Int>()
        var numberOfJ = 0
        for (char in cards) {
            if (char == 'J') {
                numberOfJ++
                if (isJokerHand) {
                    continue
                }
            }
            occurrencesMap.putIfAbsent(char, 0)
            occurrencesMap[char] = occurrencesMap[char]!! + 1
        }

        val occurrences = occurrencesMap.values.sortedDescending().toMutableList()
        if (isJokerHand) {
            if (occurrences.isEmpty()) {
                occurrences.add(5)
            } else {
                occurrences[0] += numberOfJ
            }
        }
        when (occurrences.size) {
            1 -> return HandType.FiveOfAKind
            2 -> {
                if (occurrences[0] == 4) return HandType.FourOfAKind
                return HandType.FullHouse
            }

            3 -> {
                if (occurrences[0] == 3) return HandType.ThreeOfAKind
                return HandType.TwoPairs
            }

            4 -> return HandType.OnePair
            else -> return HandType.HighCard
        }
    }

    override fun toString(): String {
        return "$cards, $bid, $type"
    }
}

enum class HandType { FiveOfAKind, FourOfAKind, FullHouse, ThreeOfAKind, TwoPairs, OnePair, HighCard }

val handTypeRankByHandType = mapOf(
    HandType.FiveOfAKind to 1,
    HandType.FourOfAKind to 2,
    HandType.FullHouse to 3,
    HandType.ThreeOfAKind to 4,
    HandType.TwoPairs to 5,
    HandType.OnePair to 6,
    HandType.HighCard to 7
)

var cardValueByCardSymbol = mutableMapOf(
    '2' to 2,
    '3' to 3,
    '4' to 4,
    '5' to 5,
    '6' to 6,
    '7' to 7,
    '8' to 8,
    '9' to 9,
    'T' to 10,
    'J' to 11,
    'Q' to 12,
    'K' to 13,
    'A' to 14,
)
