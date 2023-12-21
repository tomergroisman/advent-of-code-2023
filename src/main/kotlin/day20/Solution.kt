package day20

import Input
import println
import readInput
import java.util.*

const val packageName = "day20"

fun main() {
    fun Input.toModuleByName(): Map<String, Module> {
        val moduleByName = mutableMapOf<String, Module>()
        val conjunctions = mutableSetOf<String>()

        for (it in this) {
            val (module, outputsString) = it.split(" -> ")
            val outputs = outputsString.split(", ")
            if (module == "broadcaster") {
                moduleByName["broadcaster"] = Broadcaster(outputs)
                continue
            }
            val moduleName = module.drop(1)
            when (module[0]) {
                '%' -> moduleByName[moduleName] = FlipFlop(moduleName, outputs)
                '&' -> {
                    moduleByName[moduleName] = Conjunction(moduleName, outputs)
                    conjunctions.add(moduleName)
                }
            }
        }

        for (module in moduleByName.values) {
            module.outputs.forEach { if (conjunctions.contains(it)) (moduleByName[it] as Conjunction).addInput(module) }
        }

        return moduleByName
    }

    fun List<Long>.lcm(): ULong {
        fun gcd(a: ULong, b: ULong): ULong {
            if (b == 0.toULong()) return a
            return gcd(b, a % b)
        }

        fun lcm(a: ULong, b: ULong): ULong {
            return a * (b / gcd(a, b));
        }

        var result = this[0].toULong();
        for (num in this) {
            result = lcm(result, num.toULong())
        };
        return result;
    }

    fun part1(input: Input): Long {
        val moduleByName = input.toModuleByName()
        val q: Queue<Pulse> = LinkedList()
        var lows: Long = 0
        var highs: Long = 0
        val start = Button(listOf("broadcaster"))

        for (i in 1..1000) {
            q.add(Pulse(moduleByName["broadcaster"]!!, start))
            while (q.isNotEmpty()) {
                val pulse = q.poll()
                val module = pulse.module
                val input = pulse.input

                when (input.state) {
                    true -> highs++
                    false -> lows++
                }

                module.next(input.name, input.state) ?: continue
                module.outputs.forEach {
                    val nextModule = moduleByName[it]
                    if (nextModule == null) {
                        q.add(Pulse(Broadcaster(listOf()), module))
                    } else {
                        q.add(Pulse(nextModule, module))
                    }
                }
            }
        }
        return lows * highs
    }

    fun part2(input: Input): ULong {
        val moduleByName = input.toModuleByName()
        val q: Queue<Pulse> = LinkedList()
        val cycleByModuleName: MutableMap<String, Long> = mutableMapOf()
        var steps: Long = 0
        val start = Button(listOf("broadcaster"))

        while (true) {
            q.add(Pulse(moduleByName["broadcaster"]!!, start))
            steps++
            while (q.isNotEmpty()) {
                if (cycleByModuleName.values.size == 4) {
                    return cycleByModuleName.values.toList().lcm()
                }

                val pulse = q.poll()
                val module = pulse.module
                val input = pulse.input

                module.next(input.name, input.state) ?: continue

                if (module.name == "zp") {
                    if (input.state && cycleByModuleName[input.name] == null) {
                        cycleByModuleName[input.name] = steps
                    }
                }


                module.outputs.forEach {
                    val nextModule = moduleByName[it]
                    if (nextModule == null) {
                        q.add(Pulse(Broadcaster(listOf()), module))
                    } else {
                        q.add(Pulse(nextModule, module))
                    }
                }
            }
        }
    }

    val testInput = readInput("$packageName/input_test")
    check(part1(testInput) == 11687500.toLong())

    val input = readInput("$packageName/input")
    part1(input).println()
    part2(input).println()
}

abstract class Module(val name: String, var state: Boolean, val outputs: List<String>) {
    abstract fun next(input: String, payload: Boolean): Boolean?

    override fun toString(): String {
        return "$name $state ${this.javaClass}"
    }
}

class Button(outputs: List<String>) : Module("button", false, outputs) {
    override fun next(input: String, payload: Boolean): Boolean {
        return false
    }
}

class Broadcaster(outputs: List<String>) : Module("broadcaster", false, outputs) {
    override fun next(input: String, payload: Boolean): Boolean {
        state = payload
        return state
    }
}

class FlipFlop(name: String, outputs: List<String>) : Module(name, false, outputs) {
    override fun next(input: String, payload: Boolean): Boolean? {
        if (payload) return null
        state = !state
        return state
    }
}

class Conjunction(name: String, outputs: List<String>) : Module(name, false, outputs) {
    private val inputs = mutableListOf<Module>()

    fun addInput(module: Module) {
        inputs.add(module)
    }

    override fun next(input: String, payload: Boolean): Boolean {
        state = !inputs.all { it.state }
        return state
    }
}

data class Pulse(val module: Module, val input: Module)

// 14137137028067807420
// 215252378794009
