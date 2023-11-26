import kotlin.time.measureTime

fun main() {
    val days = mutableListOf<Day>();

    stopOutput()
    val executionTime = measureTime { days.forEach { it.execution()} }
    resumeOutput()

    println("The whole advent took: ${executionTime.inWholeSeconds} seconds");
    println("Merry Christmas!");
}
