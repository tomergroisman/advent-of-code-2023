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
 * Stops the output of the program
 */
fun stopOutput() {
    System.setOut(PrintStream(object : OutputStream() { override fun write(b: Int) {} }))
}

/**
 * Resume the output of the program
 */
fun resumeOutput() {
    System.setOut(originalStream)
}

val originalStream: PrintStream = System.out
