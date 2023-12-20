package day19

import Input
import println
import readInput
import java.util.*

const val packageName = "day19"

fun main() {
    fun Input.toWorkflowByName(): Map<String, Workflow> {
        val rulesByWorkflow = mutableMapOf<String, Workflow>()
        this.dropLast(this.size - this.indexOf("")).forEach {
            val re = Regex("^([a-zA-Z]+)(.*)$")
            val (name, rulesString) = re.matchEntire(it)!!.groupValues.drop(1)
            val rules: Queue<Rule> = LinkedList()

            for (ruleString in rulesString.substring(1, rulesString.length - 1).split(',')) {
                val splittedRule = ruleString.split(':').toMutableList()
                val isLast = splittedRule.size == 1
                val rule = Rule(
                    condition = if (isLast) null else splittedRule[0],
                    to = if (isLast) splittedRule[0] else splittedRule[1]
                )
                rules.add(rule)
            }

            rulesByWorkflow[name] = Workflow(name, rules)
        }
        return rulesByWorkflow
    }

    fun Input.toParts(): List<Part> {
        return this.drop(this.indexOf("") + 1).map {
            val re = Regex("^\\{x=([0-9]+),m=([0-9]+),a=([0-9]+),s=([0-9]+)\\}$")
            val (x, m, a, s) = re.matchEntire(it)!!.groupValues.drop(1).map { value -> value.toInt() }
            Part(x, m, a, s)
        }
    }

    fun part1(input: Input): Long {
        val parts = input.toParts()
        val workflowByName = input.toWorkflowByName()
        return parts.getAcceptedPartsSum(workflowByName)
    }

    fun part2(input: Input): ULong {
        val workflowByName = input.toWorkflowByName()
        return getDistinctPartCombination(workflowByName)
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 19114.toLong())
    check(part2(testInput) == 167409079868000.toULong())

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

fun List<Part>.getAcceptedPartsSum(workflowByName: Map<String, Workflow>): Long {
    var sum: Long = 0
    val conditionRe = Regex("([xams])([><])([0-9]+)")
    for (part in this) {
        var workflow = "in"
        while (!listOf("R", "A").contains(workflow)) {
            val rules = LinkedList(workflowByName[workflow]!!.rules)
            while (rules.isNotEmpty()) {
                val rule = rules.poll()
                if (rule.condition == null) {
                    workflow = rule.to
                    break
                }

                val (source, op, targetString) = conditionRe.matchEntire(rule.condition)!!.groupValues.drop(1)
                val target = targetString.toInt()
                val result = when (source) {
                    "x" -> {
                        when (op) {
                            ">" -> part.x > target
                            else -> part.x < target
                        }
                    }

                    "a" -> {
                        when (op) {
                            ">" -> part.a > target
                            else -> part.a < target
                        }
                    }

                    "m" -> {
                        when (op) {
                            ">" -> part.m > target
                            else -> part.m < target
                        }
                    }

                    else -> {
                        when (op) {
                            ">" -> part.s > target
                            else -> part.s < target
                        }
                    }
                }

                if (result) {
                    workflow = rule.to
                    break
                }
            }
        }

        if (workflow == "A") sum += part.x + part.m + part.a + part.s
    }

    return sum
}

fun getDistinctPartCombination(workflowByName: Map<String, Workflow>): ULong {
    val conditionRe = Regex("([xams])([><])([0-9]+)")
    val q: Queue<Entity> = LinkedList()
    val combinations = mutableListOf<ULong>()
    q.add(
        Entity(
            mutableMapOf(
                "x" to 1..4000,
                "a" to 1..4000,
                "m" to 1..4000,
                "s" to 1..4000
            ), "in",
            0
        )
    )

    while (q.isNotEmpty()) {
        val entity = q.poll()
        val workflowName = entity.workflowName
        val currentRanges = entity.ranges.toMutableMap()

        if (workflowName == "A") {
            combinations.add(currentRanges.values.map { it.count().toULong() }.reduce { acc, cur -> acc * cur })
            continue
        }

        if (workflowName == "R") continue

        val workflow = workflowByName[workflowName]!!
        val ruleIndex = entity.ruleIndex
        val rule = workflow.rules.toList()[ruleIndex]

        if (rule.condition == null) {
            q.add(Entity(currentRanges, rule.to, 0))
            continue
        }

        val (source, op, targetString) = conditionRe.matchEntire(rule.condition)!!.groupValues.drop(1)
        val target = targetString.toInt()

        if (currentRanges[source]!!.contains(target)) {
            val part1 = currentRanges.toMutableMap()
            val part2 = currentRanges.toMutableMap()
            when (op) {
                ">" -> {
                    part1[source] = currentRanges[source]!!.first..target
                    part2[source] = (target + 1)..currentRanges[source]!!.last
                    q.add(Entity(part1, workflowName, ruleIndex + 1))
                    q.add(Entity(part2, rule.to, 0))
                }

                else -> {
                    part1[source] = currentRanges[source]!!.first..<target
                    part2[source] = target..currentRanges[source]!!.last
                    q.add(Entity(part1, rule.to, 0))
                    q.add(Entity(part2, workflowName, ruleIndex + 1))
                }
            }
            continue
        }

        when (op) {
            ">" -> {
                if (currentRanges[source]!!.first < target) {
                    currentRanges[source] = (target + 1)..currentRanges[source]!!.last
                    q.add(Entity(currentRanges, rule.to, 0))
                } else {
                    currentRanges[source] = currentRanges[source]!!.first..target
                    q.add(Entity(currentRanges, workflowName, ruleIndex + 1))
                }
            }

            else -> {
                if (currentRanges[source]!!.first < target) {
                    currentRanges[source] = target..currentRanges[source]!!.last
                    q.add(Entity(currentRanges, workflowName, ruleIndex + 1))
                } else {
                    currentRanges[source] = currentRanges[source]!!.first..<target
                    q.add(Entity(currentRanges, rule.to, 0))
                }
            }
        }
    }
    return combinations.sum()
}

data class Part(val x: Int, val m: Int, val a: Int, val s: Int)

data class Rule(val condition: String?, val to: String)

data class Workflow(val name: String, val rules: Queue<Rule>)

data class Entity(val ranges: MutableMap<String, IntRange>, val workflowName: String, val ruleIndex: Int)
