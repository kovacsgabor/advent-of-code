package aoc.input

import aoc.Day
import java.io.File
import java.io.IOException
import java.net.CookieHandler
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.util.regex.Pattern

/**
 * Utility to read and download input files.
 */
object SmartReader {

    /**
     * Returns the input for the specified day.
     * Automatically downloads and saves the input file if it cannot be found in the input file folder.
     */
    fun readLines(day: Day, test: Boolean): List<String> =
        try {
            val puzzle = PuzzleId.fromClassName(day)
            val file = if (test) puzzle.testInputFile else puzzle.inputFile
            if (file.exists()) {
                // If file is already downloaded
                if (test) {
                    System.err.println("WARNING: test mode is active")
                }
                file.readLines()
            } else {
                // The file is not yet downloaded
                ensureTestInputFileExists(puzzle)
                if (test) {
                    throw InputLoadingException("Test mode is active, but the test file has just been created")
                }
                val input = InputDownloader.readLines(puzzle)
                file.writeText(input.joinToString(separator = "\n"))
                input
            }
        } catch (e: InputLoadingException) {
            throw e
        } catch (e: Exception) {
            throw InputLoadingException("Failed to load input file.", e)
        }

    private fun ensureTestInputFileExists(day: PuzzleId) {
        val file = day.testInputFile
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
    }

}

@Suppress("serial")
class InputLoadingException : RuntimeException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}

/**
 * Utility to automatically download the input file of a specific year and day.
 *
 * The code expects a session_id.txt file in the current working directory.
 * The first line of the file should contain your session ID. A simple guide on how to acquire the session ID:
 * https://github.com/wimglenn/advent-of-code-wim/issues/1
 */
private object InputDownloader {

    fun readLines(puzzle: PuzzleId): List<String> {
        val content = download(puzzle)

        if (content.isBlank()) {
            puzzle.inputFile.delete()
            throw InputLoadingException("Some error occurred: downloaded an empty file!")
        }
        if (content.contains("Please don't repeatedly request this endpoint before it unlocks!")) {
            puzzle.inputFile.delete()
            throw InputLoadingException("Tried to download a day that has not yet opened.")
        }

        val lines = content.lines()
        return if (lines.last().isEmpty()) lines.subList(0, lines.size - 1) else lines
    }

    private fun download(puzzle: PuzzleId): String {
        val uri = puzzle.uri
        val cookieHandler = CookieHandlerImpl()
        cookieHandler.addCookies(uri, listOf("session=" + sessionId()))
        try {
            val client = HttpClient.newBuilder()
                .cookieHandler(cookieHandler)
                .build()
            val request = HttpRequest.newBuilder()
                .uri(uri)
                .build()
            return client.send(request, BodyHandlers.ofString()).body()
        } catch (e: Exception) {
            throw InputLoadingException("Could not download the input file.", e)
        }
    }

    private fun sessionId(): String {
        val id = System.getProperty("aoc.session.id")
        if (!id.isNullOrBlank()) {
            return id
        }

        val file = File("session_id.txt")
        try {
            return file.readLines()[0]
        } catch (e: IndexOutOfBoundsException) {
            throw InputLoadingException("Session ID file exists, but it is empty at: " + file.absolutePath, e)
        } catch (e: IOException) {
            throw InputLoadingException("Missing session ID file at: " + file.absolutePath, e)
        }
    }

}

private class CookieHandlerImpl : CookieHandler() {
    private val cookies = mutableMapOf<String, MutableList<String>>()

    fun addCookies(uri: URI, newCookies: List<String>) {
        synchronized(cookies) {
            cookies.computeIfAbsent(uri.host) { ArrayList() }.addAll(newCookies)
        }
    }

    override fun get(uri: URI, requestHeaders: Map<String, List<String>>): Map<String, List<String>> =
        synchronized(cookies) {
            cookies[uri.host]
                ?.let { mapOf("Cookie" to it.toList()) }
                ?: mapOf()
        }

    override fun put(uri: URI, responseHeaders: Map<String, List<String>>) {
        val newCookies = responseHeaders["Set-Cookie"]
        if (newCookies != null) {
            addCookies(uri, newCookies)
        }
    }
}

private data class PuzzleId(val year: Int, val day: Int) {

    val inputFile get() = File("input/${year}/day${day}.txt")
    val testInputFile get() = File("input/${year}/day${day}_test.txt")
    val uri: URI get() = URI.create("https://adventofcode.com/${year}/day/${day}/input")

    companion object {
        private val CLASS_NAME_PATTERN = Pattern.compile("Y([0-9]{2})Day([0-9]*)([^0-9].*)?")

        @JvmStatic
        fun fromClassName(day: Day): PuzzleId {
            val matcher = CLASS_NAME_PATTERN.matcher(day.javaClass.simpleName)
            check(matcher.matches()) {
                "Simple name of class " + day.javaClass.name + " should be of the form " + CLASS_NAME_PATTERN.pattern()
            }
            return PuzzleId(
                year = 2000 + matcher.group(1).toInt(),
                day = matcher.group(2).toInt()
            )
        }
    }

}