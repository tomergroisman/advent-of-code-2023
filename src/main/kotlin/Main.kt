import kotlin.time.measureTime

fun main() {
    val days = mutableListOf<Day>()
    days.add(Day(1, { day01.main() }, "Trebuchet?!"))
    days.add(Day(2, { day02.main() }, "Cube Conundrum"))
    days.add(Day(3, { day03.main() }, "Gear Ratios"))
    days.add(Day(4, { day04.main() }, "Scratchcards"))
    days.add(Day(5, { day05.main() }, "If You Give A Seed A Fertilizer"))
    days.add(Day(6, { day06.main() }, "Wait For It"))
    days.add(Day(7, { day07.main() }, "Camel Cards"))
    days.add(Day(8, { day08.main() }, "Haunted Wasteland"))
    days.add(Day(9, { day09.main() }, "Mirage Maintenance"))
    days.add(Day(10, { day10.main() }, "Pipe Maze"))
    days.add(Day(11, { day11.main() }, "Cosmic Expansion"))
    days.add(Day(12, { day12.main() }, "Hot Springs"))
    days.add(Day(13, { day13.main() }, "Point of Incidence"))
    days.add(Day(14, { day14.main() }, "Parabolic Reflector Dish"))
    days.add(Day(15, { day15.main() }, "Lens Library"))
    days.add(Day(16, { day16.main() }, "The Floor Will Be Lava"))
    days.add(Day(17, { day17.main() }, "Clumsy Crucible"))
    days.add(Day(18, { day18.main() }, "Lavaduct Lagoon"))
    days.add(Day(19, { day19.main() }, "Aplenty"))
    days.add(Day(20, { day20.main() }, "Pulse Propagation"))
    days.add(Day(21, { day21.main() }, "Step Counter"))
    days.add(Day(22, { day22.main() }, "Sand Slabs"))

    stopOutput()
    val executionTime = measureTime { days.forEach { it.execution() } }
    resumeOutput()

    println("The whole advent took: ${executionTime.inWholeSeconds} seconds")
    println("Merry Christmas!")
}
