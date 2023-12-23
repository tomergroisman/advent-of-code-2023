package day23

import Input
import Position
import println
import readInput
import java.util.*

const val packageName = "day23"

fun main() {
    fun part1(input: Input): Int {
        val trailMap = TrailMap(input, canClimb = false)
        return trailMap.getLongestHike()
    }

    fun part2(input: Input): Int {
        val trailMap = TrailMap(input, canClimb = true)
        println(trailMap.getLongestHike())
        return trailMap.getLongestHike()
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 94)
    check(part2(testInput) == 154)

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

data class TrailMap(val map: List<String>, val canClimb: Boolean) {
    private val n = map.size
    private val m = map[0].length
    private val directions = listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))
    private val graph = mutableSetOf<Vertex>()

    init {
        val vertexByPosition = mutableMapOf<Position, Vertex>()
        for (i in 0..<n) {
            for (j in 0..<m) {
                val tile = map[i][j]
                if (tile == '#') continue
                val vertex = vertexByPosition[Position(i, j)] ?: Vertex(i, j)
                vertexByPosition[Position(i, j)] = vertex
                graph.add(vertex)

                for (direction in directions) {
                    val neighborPosition = Position(vertex.i + direction.first, vertex.j + direction.second)
                    val neighbor = vertexByPosition[neighborPosition] ?: Vertex(neighborPosition.i, neighborPosition.j)
                    vertexByPosition[neighborPosition] = neighbor
                    if (neighbor.i !in 0..<n || neighbor.j !in 0..<m) continue
                    val neighborTile = map[neighbor.i][neighbor.j]
                    val shouldAdd = when (neighborTile) {
                        '.' -> true
                        '#' -> false
                        '^' -> if (canClimb) true else direction != Pair(1, 0)
                        '>' -> if (canClimb) true else direction != Pair(0, -1)
                        'v' -> if (canClimb) true else direction != Pair(-1, 0)
                        else -> if (canClimb) true else direction != Pair(0, 1)
                    }
                    if (shouldAdd) {
                        vertex.addEdge(neighbor, 1)
                    }
                }
            }
        }

        if (canClimb) {
            for (vertex in graph) {
                if (vertex.edges.size == 2) {
                    val vertices = listOf(vertex.edges[0].to, vertex.edges[1].to)

                    for (i in 0..1) {
                        val weight = vertex.edges[(i + 1) % 2].weight
                        val edge = graph.find { it == vertices[i] }!!.edges.find { it.to == vertex }!!
                        edge.to = vertices[(i + 1) % 2]
                        edge.weight += weight
                    }
                }
            }
        }
    }

    fun getLongestHike(): Int {
        val stack = Stack<Entity>()
        val start = graph.first()
        stack.push(Entity(start, setOf(), 0))
        val distances = mutableSetOf<Int>()

        while (stack.isNotEmpty()) {
            val entity = stack.pop()
            val vertex = entity.vertex
            val distance = entity.distance
            val seen = entity.seen.toMutableSet()

            if (vertex == Vertex(n - 1, m - 2)) distances.add(distance)
            if (seen.contains(vertex)) continue

            seen.add(vertex)

            for (edge in vertex.edges) {
                stack.push(Entity(edge.to, seen, distance + edge.weight))
            }
        }
        return distances.max()
    }
}

data class Entity(val vertex: Vertex, val seen: Set<Vertex>, val distance: Int)

class Vertex(var i: Int, var j: Int) {
    val edges = mutableListOf<Edge>()

    fun addEdge(to: Vertex, weight: Int) {
        edges.add(Edge(this, to, weight))
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Vertex) return false
        return this.i == other.i && this.j == other.j
    }

    override fun toString() = "$i $j"
    override fun hashCode(): Int {
        var result = i
        result = 31 * result + j
        return result
    }
}

data class Edge(val from: Vertex, var to: Vertex, var weight: Int)
