package day06

import Input
import println
import readInput

const val packageName = "day06"

fun main() {
    fun Input.toRaces(): List<Race> {
        val times = this[0]
            .replace(Regex("Time:\\s*"), "")
            .replace(Regex("\\s+"), " ")
            .split(Regex("\\s+"))
            .map { it.toLong() }
        val bestDistances = this[1]
            .replace(Regex("Distance:\\s*"), "")
            .replace(Regex("\\s+"), " ")
            .split(" ")
            .map { it.toLong() }
        val races = mutableListOf<Race>()

        for (i in times.indices) {
            races.add(Race(times[i], bestDistances[i]))
        }
        return races
    }

    fun Input.toRace(): Race {
        val time = this[0]
            .replace(Regex("Time:\\s*"), "")
            .replace(Regex("\\s+"), "")
            .toLong()
        val bestDistance = this[1]
            .replace(Regex("Distance:\\s*"), "")
            .replace(Regex("\\s+"), "")
            .toLong()
        return Race(time, bestDistance)
    }

    fun Long.willWinRace(race: Race): Boolean {
        val speed = this
        val time = race.time - speed
        val distance = time * speed
        return distance > race.bestDistance
    }

    fun Race.getHoldingTimesRange(): LongRange {
        var minHoldingTimeWinner: Long? = null
        var maxHoldingTimeWinner: Long? = null
        for (i in 0..this.time) {
            if (i.willWinRace(this)) {
                if (minHoldingTimeWinner == null) {
                    minHoldingTimeWinner = i
                }
                maxHoldingTimeWinner = i
            } else if (maxHoldingTimeWinner != null) {
                break
            }
        }
        return minHoldingTimeWinner!!..maxHoldingTimeWinner!!
    }

    fun part1(input: Input): Long {
        val races = input.toRaces()
        val waitingTimes = mutableListOf<LongRange>()

        for (race in races) {
            waitingTimes.add(race.getHoldingTimesRange())
        }

        return waitingTimes.map { it.count().toLong() }.reduce { acc, cur -> acc * cur }
    }

    fun part2(input: Input): Long {
        val race = input.toRace()
        return race.getHoldingTimesRange().count().toLong()
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 288.toLong())
    check(part2(testInput) == 71503.toLong())

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

data class Race(val time: Long, val bestDistance: Long)
