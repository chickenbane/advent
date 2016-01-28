package advent

import org.junit.Assert
import org.junit.Test

/**
 * Created by a-jotsai on 1/27/16.
 */
class Day05Test {
    @Test
    fun examples() {
//        ugknbfddgicrmopn is nice because it has at least three vowels (u...i...o...), a double letter (...dd...), and none of the disallowed substrings.
//        aaa is nice because it has at least three vowels and a double letter, even though the letters used by different rules overlap.
//        jchzalrnumimnmhp is naughty because it has no double letter.
//        haegwjzuvuyypxyu is naughty because it contains the string xy.
//        dvszwmarrgswjxmb is naughty because it contains only one vowel.

        Assert.assertTrue("ugknbfddgicrmopn is nice", Day05.isNice("ugknbfddgicrmopn"))
        Assert.assertTrue("aaa is nice", Day05.isNice("aaa"))

        Assert.assertFalse("jchzalrnumimnmhp is naughty", Day05.isNice("jchzalrnumimnmhp"))
        Assert.assertFalse("haegwjzuvuyypxyu is naughty", Day05.isNice("haegwjzuvuyypxyu"))
        Assert.assertFalse("dvszwmarrgswjxmb is naughty", Day05.isNice("dvszwmarrgswjxmb"))
    }

    @Test
    fun answer() {
        Assert.assertEquals("input has 1000 words", 1000, Day05.inputStrings.size)
        Assert.assertEquals("my answer", 236, Day05.inputStrings.filter { Day05.isNice(it) }.size)
    }

    @Test
    fun examples2() {
        // like xyxy (xy) or aabcdefgaa (aa), but not like aaa (aa, but it overlaps).
        Assert.assertTrue("xy", Day05.hasTwoTwice("xyxy"))
        Assert.assertTrue("aa", Day05.hasTwoTwice("aabcdefgaa"))
        Assert.assertFalse("aaa", Day05.hasTwoTwice("aaa"))

        //  like xyx, abcdefeghi (efe), or even aaa
        Assert.assertTrue("xyx", Day05.hasTwoWithMid("xyx"))
        Assert.assertTrue("abcdefeghi", Day05.hasTwoWithMid("abcdefeghi"))
        Assert.assertTrue("aaa", Day05.hasTwoWithMid("aaa"))

//        qjhvhtzxzqqjkmpb is nice because is has a pair that appears twice (qj) and a letter that repeats with exactly one letter between them (zxz).
//        xxyxx is nice because it has a pair that appears twice and a letter that repeats with one between, even though the letters used by each rule overlap.

        Assert.assertTrue("qjhvhtzxzqqjkmpb", Day05.isNicePart2("qjhvhtzxzqqjkmpb"))
        Assert.assertTrue("xxyxx", Day05.isNicePart2("xxyxx"))

        //        uurcxstgmygtbstg is naughty because it has a pair (tg) but no repeat with a single letter between them.
        Assert.assertFalse("uurcxstgmygtbstg", Day05.isNicePart2("uurcxstgmygtbstg"))
        Assert.assertTrue("uurcxstgmygtbstg", Day05.hasTwoTwice("uurcxstgmygtbstg"))
        Assert.assertFalse("uurcxstgmygtbstg", Day05.hasTwoWithMid("uurcxstgmygtbstg"))

        //        ieodomkazucvgmuy is naughty because it has a repeating letter with one between (odo), but no pair that appears twice.
        Assert.assertFalse("ieodomkazucvgmuy", Day05.isNicePart2("ieodomkazucvgmuy"))
        Assert.assertFalse("ieodomkazucvgmuy", Day05.hasTwoTwice("ieodomkazucvgmuy"))
        Assert.assertTrue("ieodomkazucvgmuy", Day05.hasTwoWithMid("ieodomkazucvgmuy"))
    }

    @Test
    fun answer2() {
        Assert.assertEquals("my answer", 51, Day05.inputStrings.filter { Day05.isNicePart2(it) }.size)

    }
}