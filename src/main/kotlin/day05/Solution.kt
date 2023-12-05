package day05

import Input
import println
import readInput
import kotlinx.coroutines.*
import java.lang.Long.min
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.seconds

const val packageName = "day05"


fun main() {
    fun Input.toSeeds(): List<Long> {
        return this[0].replace("seeds: ", "").split(" ").map { it.toLong() }
    }

    fun Input.toFromCategoryToCategoryMap(): Map<String, CategoryMap> {
        val maps = this.drop(2).joinToString(" ").split("  ")
        val fromCategoryToCategoryMap = mutableMapOf<String, CategoryMap>()
        maps.forEach {
            val (fromCategory, toCategory, rangesUnionString) = it.replace(Regex("(.*)-to-(.*) map: (.*)"), "$1,$2,$3")
                .split(",")
            val rangesString = Regex("\\d+ \\d+ \\d+")
                .findAll(rangesUnionString)
                .toList()
                .map { match -> match.value }
            val rangeMaps = mutableListOf<RangeMap>()
            rangesString.forEach { rangeString ->
                val (destinationStart, sourceStart, rangeLength) = rangeString.split(" ").map { n -> n.toLong() }
                val sourceOffsetFromDestination = destinationStart - sourceStart
                val source = RangeValues(start = sourceStart, end = sourceStart + rangeLength - 1)
                val destination = RangeValues(start = destinationStart, end = destinationStart + rangeLength)
                rangeMaps.add(RangeMap(source, destination, sourceOffsetFromDestination))
            }
            fromCategoryToCategoryMap[fromCategory] = CategoryMap(fromCategory, toCategory, rangeMaps)
        }
        return fromCategoryToCategoryMap
    }

    fun Long.getLocationNumber(fromCategoryToCategoryMap: Map<String, CategoryMap>): Long {
        var currentCategory = "seed"
        var currentValue = this
        while (currentCategory != "location") {
            val categoryMap = fromCategoryToCategoryMap[currentCategory]!!
            for (rangeMap in categoryMap.rangeMaps) {
                val mappedValue = rangeMap.mapValue(currentValue)
                if (mappedValue != null) {
                    currentValue = mappedValue
                    break
                }
            }
            currentCategory = categoryMap.toCategory
        }
        return currentValue
    }

    fun part1(input: Input): Long {
        val seeds = input.toSeeds()
        val fromCategoryToCategoryMap = input.toFromCategoryToCategoryMap()

        val locationNumbers = seeds.map { it.getLocationNumber(fromCategoryToCategoryMap) }
        return locationNumbers.min()
    }

    fun part2(input: Input): Long {
        val seeds = input.toSeeds().toMutableList()
        val fromCategoryToCategoryMap = input.toFromCategoryToCategoryMap()
        val seedRanges = mutableListOf<RangeValues>()

        for (i in 0..<seeds.size step 2) {
            val start = seeds[i]
            val end = start + seeds[i + 1] - 1
            seedRanges.add(RangeValues(start, end))
        }

        val mins = mutableListOf<Long>()
        runBlocking {
            val tasks = mutableListOf<Job>()
            for (seedRangeNumber in 1..seedRanges.size) {
                val seedRange = seedRanges[seedRangeNumber - 1]
                tasks.add(
                    launch(Dispatchers.IO) {
                        val timeInMillis = measureTimeMillis {
                            var minLocationNumber = Long.MAX_VALUE
                            for (i in seedRange.start..seedRange.end) {
                                val locationNumber = i.getLocationNumber(fromCategoryToCategoryMap)
                                minLocationNumber = min(minLocationNumber, locationNumber)
                            }
                            mins.add(minLocationNumber)
                        }
                        println("Ran range $seedRangeNumber of ${seedRanges.size} [${seedRange.start} - ${seedRange.end}] in ${timeInMillis.seconds} seconds")
                    }
                )
            }
            tasks.joinAll()
        }
        
        return mins.min()
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 35.toLong())
    check(part2(testInput) == 46.toLong())

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

data class CategoryMap(val fromCategory: String, val toCategory: String, val rangeMaps: List<RangeMap>)
data class RangeMap(val source: RangeValues, val destination: RangeValues, val sourceOffsetFromDestination: Long) {
    private fun isMappable(candidate: Long): Boolean {
        return candidate in source.start..source.end
    }

    fun mapValue(value: Long): Long? {
        if (isMappable(value)) {
            return value + sourceOffsetFromDestination
        }
        return null
    }
}

data class RangeValues(val start: Long, val end: Long)
