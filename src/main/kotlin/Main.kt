import kotlin.time.measureTime

fun main() {
    val days = mutableListOf<Day>();
    days.add(Day(1, { day01.main() }, "Trebuchet?!"))
    days.add(Day(2, { day02.main() }, "Cube Conundrum"))
    days.add(Day(3, { day03.main() }, "Gear Ratios"))
    days.add(Day(4, { day04.main() }, "Scratchcards"))
    days.add(Day(5, { day05.main() }, "If You Give A Seed A Fertilizer"))

    stopOutput()
    val executionTime = measureTime { days.forEach { it.execution()} }
    resumeOutput()

    println("The whole advent took: ${executionTime.inWholeMilliseconds} milliseconds");
    println("Merry Christmas!");
}
