package day22

import Input
import println
import readInput
import java.util.*
import kotlin.math.max
import kotlin.math.min

const val packageName = "day22"

fun main() {
    fun Input.toJanga(): Janga {
        return Janga(this.map { Brick(it) })
    }

    fun part1(input: Input): Int {
        val janga = input.toJanga()
        return janga.getDisintegrationBrickCount()
    }

    fun part2(input: Input): Int {
        val janga = input.toJanga()
        return janga.getOtherBricksFallSum()
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 7)

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

class Brick(data: String) {
    val vertical: Vertical
    private var start: Point
    private var end: Point
    private var axis: Axis?
    val supporting = mutableSetOf<Brick>()
    val supportedBy = mutableSetOf<Brick>()

    init {
        val (brickStart, brickEnd) = data.split("~")
        val (xs, ys, zs) = brickStart.split(",").map { it.toInt() }
        val (xe, ye, ze) = brickEnd.split(",").map { it.toInt() }

        vertical = Vertical(min(zs, ze), max(zs, ze))

        start = Point(xs, ys)
        end = Point(xe, ye)

        axis = if (xs != xe) Axis.X
        else if (ys != ye) Axis.Y
        else if (zs != ze) Axis.Z
        else null
    }

    fun addSupporting(brick: Brick) {
        supporting.add(brick)
    }

    fun addSupportedBy(brick: Brick) {
        supportedBy.add(brick)
    }

    fun isRedundant(): Boolean {
        if (supporting.size == 0) return true
        return supporting.all { it.supportedBy.size > 1 }
    }

    fun isColliding(other: Brick): Boolean {
        // Can collide
        if (vertical.s !in other.vertical.s..other.vertical.e && vertical.e !in other.vertical.s..other.vertical.e) return false

        // Parallel
        if (this.axis == other.axis) return (other.start.x in this.start.x..this.end.x && other.start.y in this.start.y..this.end.y) || (start.x in other.start.x..other.end.x && start.y in other.start.y..other.end.y)

        // Crossing
        return when (axis) {
            Axis.X -> other.start.x in start.x..end.x && start.y in other.start.y..other.end.y
            Axis.Y -> other.start.y in start.y..end.y && start.x in other.start.x..other.end.x
            else -> start.x in other.start.x..other.end.x && start.y in other.start.y..other.end.y
        }
    }

    override fun toString(): String {
        return "$start $end $vertical $axis ${supporting.size} ${supportedBy.size}"
    }
}

class Janga(private val snapshot: List<Brick>) {
    private val tower = groundBricks()

    fun getDisintegrationBrickCount(): Int {
        return tower.count { it.isRedundant() }
    }


    private fun groundBricks(): List<Brick> {
        val sortedSnapshot = snapshot.sortedBy { it.vertical.s }
        for (brick in sortedSnapshot) {
            var hasSettled = false
            while (!hasSettled) {
                if (brick.vertical.s == 1) break
                brick.vertical.s--
                brick.vertical.e--
                val disturbingBricks = sortedSnapshot.filter { brick.vertical.s == it.vertical.e && brick != it }
                for (disturbingBrick in disturbingBricks) {
                    if (brick.isColliding(disturbingBrick)) {
                        hasSettled = true
                        disturbingBrick.addSupporting(brick)
                        brick.addSupportedBy(disturbingBrick)
                    }
                }
                if (hasSettled) {
                    brick.vertical.s++
                    brick.vertical.e++
                }
            }

        }
        return sortedSnapshot.sortedBy { it.vertical.s }
    }

    fun getOtherBricksFallSum(): Int {
        val vitalBricks = tower.filter { !it.isRedundant() }
        return vitalBricks.sumOf {
            val q: Queue<Brick> = LinkedList()
            q.add(it)
            val fallingBricks = mutableSetOf<Brick>()
            while (q.isNotEmpty()) {
                val brick = q.poll()
                fallingBricks.add(brick)
                brick.supporting.forEach { supportedBrick ->
                    if (fallingBricks.containsAll(supportedBrick.supportedBy)) q.add(supportedBrick)
                }
            }
            fallingBricks.remove(it)
            fallingBricks.size
        }
    }
}

data class Point(val x: Int, val y: Int) {
    override fun equals(other: Any?): Boolean {
        if (other !is Point) return false
        return this.x == other.x && this.y == other.y
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}

data class Vertical(var s: Int, var e: Int)

enum class Axis {
    X,
    Y,
    Z
}

// 1419
// 858
// 857
// 658
// 652
// 631
// 553
// 124515
