package day08

import Input
import println
import readInput

const val packageName = "day08"

fun main() {
    fun Input.toDesertMap(): DesertMap {
        val pattern = this[0]
        val nodesData = this.drop(2)
        val nodeById = mutableMapOf<String, Node>()
        for (nodeData in nodesData) {
            val (id, left, right) = Regex("([A-Za-z0-9]+) = \\(([A-Za-z0-9]+), ([A-Za-z0-9]+)\\)")
                .find(nodeData)!!
                .groupValues
                .drop(1)
            nodeById[id] = Node(id, left, right)
        }
        return DesertMap(pattern, nodeById)
    }

    fun part1(input: Input): Int {
        val desertMap = input.toDesertMap()
        return desertMap.navigateAsHuman()
    }

    fun part2(input: Input): Long {
        val desertMap = input.toDesertMap()
        return desertMap.navigateAsGhost()
    }

    val testInput = readInput("$packageName/input_test")
    check(part2(testInput) == 6.toLong())

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

data class Node(val id: String, val left: String, val right: String)
data class DesertMap(val pattern: String, val nodeById: Map<String, Node>) {
    fun navigateAsHuman(): Int {
        var steps = 0
        var currentNode = nodeById["AAA"]!!

        while (currentNode.id != "ZZZ") {
            val direction = pattern[steps % pattern.length]
            when (direction) {
                'L' -> currentNode = nodeById[currentNode.left]!!
                'R' -> currentNode = nodeById[currentNode.right]!!
            }
            steps++
        }

        return steps
    }

    fun navigateAsGhost(): Long {
        fun List<Long>.lcm(): Long {
            fun gcd(a: Long, b: Long): Long {
                if (b == 0.toLong()) return a
                return gcd(b, a % b)
            }

            fun lcm(a: Long, b: Long): Long {
                return a * (b / gcd(a, b));
            }

            var result = this[0];
            for (num in this) {
                result = lcm(result, num)
            };
            return result;
        }

        fun Node.hasFinished(): Boolean {
            val endWithZRegex = Regex(".*Z$")
            return endWithZRegex.matches(this.id)
        }

        val nodes = mutableListOf<Node>()
        nodeById.values.forEach {
            if (Regex(".*A$").matches(it.id)) {
                nodes.add(it)
            }
        }

        val finishStepsByNodeId = mutableMapOf<String, Long>()
        for (node in nodes) {
            var steps: Long = 0
            var currentNode = node
            while (!currentNode.hasFinished()) {
                val direction = pattern[(steps % pattern.length).toInt()]
                when (direction) {
                    'L' -> currentNode = nodeById[currentNode.left]!!
                    'R' -> currentNode = nodeById[currentNode.right]!!
                }
                steps++
            }
            finishStepsByNodeId[node.id] = steps
        }

        return finishStepsByNodeId.values.toList().lcm()
    }
}
