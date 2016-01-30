package advent

import java.io.File

/**
 * Created by joeyt on 1/29/16.
 */
object Day08 {
    private val part1 = """
--- Day 8: Matchsticks ---

Space on the sleigh is limited this year, and so Santa will be bringing his list as a digital copy. He needs to know how much space it will take up when stored.

It is common in many programming languages to provide a way to escape special characters in strings.
For example, C, JavaScript, Perl, Python, and even PHP handle special characters in very similar ways.

However, it is important to realize the difference between the number of characters in the code
representation of the string literal and the number of characters in the in-memory string itself.

For example:

"" is 2 characters of code (the two double quotes), but the string contains zero characters.

"abc" is 5 characters of code, but 3 characters in the string data.

"aaa\"aaa" is 10 characters of code, but the string itself contains six "a" characters and a single,
 escaped quote character, for a total of 7 characters in the string data.

"\x27" is 6 characters of code, but the string itself contains just one - an apostrophe ('), escaped using hexadecimal notation.


Santa's list is a file that contains many double-quoted string literals, one on each line.
 The only escape sequences used are \\ (which represents a single backslash),
  \" (which represents a lone double-quote character), and \x plus two hexadecimal
  characters (which represents a single character with that ASCII code).

Disregarding the whitespace in the file, what is the number of characters of code for string
literals minus the number of characters in memory for the values of the strings in total for the entire file?

For example, given the four strings above, the total number of characters of string code (2 + 5 + 10 + 6 = 23) minus the total number of characters in memory for string values (0 + 3 + 7 + 1 = 11) is 23 - 11 = 12.
    """

    private val charsOfCode = 0 // length of the string read in

    fun charsOfCode(input: String): Int = input.length

    val hexRegex = Regex("""\\x[0-9a-fA-F]{2}""")

    fun charsOfString(input: String): Int {
        // remove quotes
        require(input[0] == '"' && input[input.lastIndex] == '"')

        // Note, in String.substring(startIndex, endIndex), endIndex is exclusive
        val startIndex = 1
        val endIndex = input.lastIndex
        val noquotes = input.substring(startIndex, endIndex)
        //println("noquotes=$noquotes")


        // UGH
        // "ubgxxcvnltzaucrzg\\xcez"
        // will get converted to ubgxxcvnltzaucrzg1z by hexreplace
        // moving hexreplace before escaped doesn't help, returns ubgxxcvnltzaucrzg\1z
        // so don't escape \\ with \, just replace with dummy chars

        val escaped = noquotes.replace("""\\""", "X").replace("""\"""", "Y")
        //println("escaped=$escaped")

        val hexReplaced = hexRegex.replace(escaped, "1")  // just need to count
        //println("hexReplaced=$hexReplaced")

        return hexReplaced.length
    }

    val input: List<String> by lazy { File("src/main/resources/input8.txt").readLines().map { it.trim() } }

    private val part2 = """
--- Part Two ---

Now, let's go the other way. In addition to finding the number of characters of code,
 you should now encode each code representation as a new string and find the number of
  characters of the new encoded representation, including the surrounding double quotes.

For example:

"" encodes to "\"\"", an increase from 2 characters to 6.
"abc" encodes to "\"abc\"", an increase from 5 characters to 9.
"aaa\"aaa" encodes to "\"aaa\\\"aaa\"", an increase from 10 characters to 16.
"\x27" encodes to "\"\\x27\"", an increase from 6 characters to 11.

Your task is to find the total number of characters to represent the newly encoded strings minus the number of characters of code in each original string literal. For example, for the strings above, the total encoded length (6 + 9 + 16 + 11 = 42) minus the characters in the original code representation (23, just like in the first part of this puzzle) is 42 - 23 = 19.
    """

    val escapeChars = setOf('"', '\\')
    fun charsOfEscaped(input: String): Int {
        val escape: List<Char> = input.toCharArray().flatMap { if (escapeChars.contains(it)) listOf('\\', it) else listOf(it) }
        return escape.size + 2 // add the quotes
    }
}