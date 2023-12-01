import kotlin.time.measureTime

fun main() {
    val days = mutableListOf<Day>();
    days.add(Day(1, { day01.main() }, "Trebuchet?!"))

    stopOutput()
    val executionTime = measureTime { days.forEach { it.execution()} }
    resumeOutput()

    println("The whole advent took: ${executionTime.inWholeSeconds} seconds");
    println("Merry Christmas!");
}
