package advent

import org.junit.Assert
import org.junit.Test

/**
 * Created by joeyt on 1/30/16.
 */
class Day08Test {

    val exampleStrings = """
        ""
        "abc"
        "aaa\"aaa"
        "\x27"
        """
    val examples = exampleStrings.lines().map{ it.trim() }.filter { it.isNotBlank() }

    @Test
    fun examples() {
//        "" is 2 characters of code (the two double quotes), but the string contains zero characters.
//
//        "abc" is 5 characters of code, but 3 characters in the string data.
//
//        "aaa\"aaa" is 10 characters of code, but the string itself contains six "a" characters and a single,
//        escaped quote character, for a total of 7 characters in the string data.
//
//        "\x27" is 6 characters of code, but the string itself contains just one - an apostrophe ('), escaped using hexadecimal notation.

        Assert.assertEquals("four examples", 4, examples.size)
        Assert.assertEquals("first has 2 chars of code", 2, Day08.charsOfCode(examples[0]))
        Assert.assertEquals("second has 5 chars of code", 5, Day08.charsOfCode(examples[1]))
        Assert.assertEquals("third has 10 chars of code", 10, Day08.charsOfCode(examples[2]))
        Assert.assertEquals("fourth has 6 chars of code", 6, Day08.charsOfCode(examples[3]))

        Assert.assertEquals("first has 0 chars of data", 0, Day08.charsOfString(examples[0]))
        Assert.assertEquals("second has 3 chars of data", 3, Day08.charsOfString(examples[1]))
        Assert.assertEquals("third has 7 chars of data", 7, Day08.charsOfString(examples[2]))
        Assert.assertEquals("fourth has 1 chars of data", 1, Day08.charsOfString(examples[3]))

        //the total number of characters of string code (2 + 5 + 10 + 6 = 23)
        // minus the total number of characters in memory for string values (0 + 3 + 7 + 1 = 11)
        // is 23 - 11 = 12.
        val codeSize = examples.map { Day08.charsOfCode(it) }.sum()
        val stringSize = examples.map { Day08.charsOfString(it) }.sum()
        Assert.assertEquals("23-11=12", 12, codeSize-stringSize)
    }

    @Test
    fun testParse() {
        Assert.assertEquals("input has 300 lines", 300, Day08.input.size)
        Assert.assertEquals("last input", """"\\zrs\\syur"""", Day08.input.last())
    }

    @Test
    fun answer() {
        val codeSize = Day08.input.map { Day08.charsOfCode(it) }.sum()
        val stringSize = Day08.input.map { Day08.charsOfString(it) }.sum()

        println("codeSize = $codeSize stringSize = $stringSize")

        // 1345 too big!
        // 1344 too big!
        // first time to guess more than once
        // "ubgxxcvnltzaucrzg\\xcez" caused me to double replace, first for \\, then for \xce
        Assert.assertEquals("my answer", 1342, codeSize-stringSize)
    }

    @Test
    fun examples2() {
        /*
        "" encodes to "\"\"", an increase from 2 characters to 6.
        "abc" encodes to "\"abc\"", an increase from 5 characters to 9.
        "aaa\"aaa" encodes to "\"aaa\\\"aaa\"", an increase from 10 characters to 16.
        "\x27" encodes to "\"\\x27\"", an increase from 6 characters to 11.
        */
        Assert.assertEquals("first has 6", 6, Day08.charsOfEscaped(examples[0]))
        Assert.assertEquals("second has 9", 9, Day08.charsOfEscaped(examples[1]))
        Assert.assertEquals("third has 16", 16, Day08.charsOfEscaped(examples[2]))
        Assert.assertEquals("fourth has 11", 11, Day08.charsOfEscaped(examples[3]))
    }

    @Test
    fun answer2() {
        val codeSize = Day08.input.map { Day08.charsOfCode(it) }.sum()
        val escapeSize = Day08.input.map { Day08.charsOfEscaped(it) }.sum()

        Assert.assertEquals("my answer", 2074, escapeSize-codeSize)
    }
}