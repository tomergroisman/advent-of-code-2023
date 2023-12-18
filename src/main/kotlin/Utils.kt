import java.io.OutputStream
import java.io.PrintStream
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/main/kotlin/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Print the collection with a newline after each item
 */
fun Iterable<Any>.println() {
    this.forEach { it.println() }
}
/**
 * A type alias for the input.
 */
typealias Input = List<String>

/**
 * Stops the output of the program.
 */
fun stopOutput() {
    System.setOut(PrintStream(object : OutputStream() {
        override fun write(b: Int) {}
    }))
}

/**
 * Resume the output of the program.
 */
fun resumeOutput() {
    System.setOut(originalStream)
}

/**
 * The original stream instance.
 */
val originalStream: PrintStream = System.out

/**
 * A i, j position data class
 */
data class Position(val i: Int, val j: Int) {
    fun copy(i: Int?, j: Int?): Position {
        return Position(i ?: this.i, j ?: this.j)
    }

    override fun toString() = "$i $j"
}
