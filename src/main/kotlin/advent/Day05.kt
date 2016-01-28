package advent

import java.io.File

/**
 * Created by a-jotsai on 1/27/16.
 */
object Day05 {
    private val part1 = """
--- Day 5: Doesn't He Have Intern-Elves For This? ---

Santa needs help figuring out which strings in his text file are naughty or nice.

A nice string is one with all of the following properties:

It contains at least three vowels (aeiou only), like aei, xazegov, or aeiouaeiouaeiou.
It contains at least one letter that appears twice in a row, like xx, abcdde (dd), or aabbccdd (aa, bb, cc, or dd).
It does not contain the strings ab, cd, pq, or xy, even if they are part of one of the other requirements.
For example:

ugknbfddgicrmopn is nice because it has at least three vowels (u...i...o...), a double letter (...dd...), and none of the disallowed substrings.
aaa is nice because it has at least three vowels and a double letter, even though the letters used by different rules overlap.
jchzalrnumimnmhp is naughty because it has no double letter.
haegwjzuvuyypxyu is naughty because it contains the string xy.
dvszwmarrgswjxmb is naughty because it contains only one vowel.
How many strings are nice?
    """

    private val vowels: Set<Char> = "aeiou".toSet()
    private val badStrings: Set<String> = setOf("ab", "cd", "pq", "xy")

    fun isNice(input: String): Boolean {
        val inputList = input.toArrayList()
        val inputVowels: List<Char> = inputList.filter { vowels.contains(it) }
        val hasVowels = inputVowels.size >= 3

        var hasDouble = false
        var hasBads = false
        inputList.forEachIndexed { i, c ->
            val nextIdx = i + 1
            if (nextIdx < inputList.size) {  // if not last index

                // check double characters
                val next = inputList[nextIdx]
                if (next == c) {
                    hasDouble = true
                }

                val two: String = "" + c + next
                if (badStrings.contains(two)) {
                    hasBads = true
                }
            }
        }

        return hasVowels && hasDouble && !hasBads
    }

    val inputStrings: List<String> by lazy {
        File("src/main/resources/input5.txt").readLines()
    }

    private val part2 = """
--- Part Two ---

Realizing the error of his ways, Santa has switched to a better model of determining whether a string is naughty or nice.
None of the old rules apply, as they are all clearly ridiculous.

Now, a nice string is one with all of the following properties:

It contains a pair of any two letters that appears at least twice in the string without overlapping,
like xyxy (xy) or aabcdefgaa (aa), but not like aaa (aa, but it overlaps).

It contains at least one letter which repeats with exactly one letter between them, like xyx, abcdefeghi (efe), or even aaa.


For example:

qjhvhtzxzqqjkmpb is nice because is has a pair that appears twice (qj) and a letter that repeats with exactly one letter between them (zxz).
xxyxx is nice because it has a pair that appears twice and a letter that repeats with one between, even though the letters used by each rule overlap.
uurcxstgmygtbstg is naughty because it has a pair (tg) but no repeat with a single letter between them.
ieodomkazucvgmuy is naughty because it has a repeating letter with one between (odo), but no pair that appears twice.
How many strings are nice under these new rules?
    """

    fun hasTwoTwice(input: String): Boolean {
        val inputList = input.toArrayList()
        for (i in 0..(inputList.size-2)) { // all but last index
            val check = "" + inputList[i] + inputList[i+1]
            val checkNums = setOf(i, i+1)
            for (j in 0..(inputList.size-2)) {
                val two = "" + inputList[j] + inputList[j+1]
                val twoNums = setOf(j, j+1)
                if (checkNums.none { twoNums.contains(it) } && check == two) {
                    return true
                }
            }
        }
        return false
    }

    fun hasTwoWithMid(input: String): Boolean {
        val inputList = input.toArrayList()
        for (i in 0..(inputList.size-3)) { // all but last index
            if (inputList[i] == inputList[i+2]) {
                return true
            }
        }
        return false
    }

    fun isNicePart2(input: String) = hasTwoTwice(input) && hasTwoWithMid(input)
}