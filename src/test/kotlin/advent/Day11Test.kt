package advent

import org.junit.Assert
import org.junit.Test

/**
 * Created by a-jotsai on 2/3/16.
 */
class Day11Test {
    @Test
    fun example() {
        // hijklmmn meets the first requirement (because it contains the straight hij) but fails the second requirement requirement (because it contains i and l).
        val ex1 = "hijklmmn"
        Assert.assertTrue(ex1, Day11.hasIncreasingStraight(ex1))
        Assert.assertTrue(ex1, Day11.hasBanned(ex1))
        Assert.assertFalse(ex1, Day11.hasTwoPairs(ex1))

        // abbceffg meets the third requirement (because it repeats bb and ff) but fails the first requirement.
        val ex2 = "abbceffg"
        Assert.assertFalse(ex2, Day11.hasIncreasingStraight(ex2))
        Assert.assertFalse(ex2, Day11.hasBanned(ex2))
        Assert.assertTrue(ex2, Day11.hasTwoPairs(ex2))

        // abbcegjk fails the third requirement, because it only has one double letter (bb).
        val ex3 = "abbcegjk"
        Assert.assertFalse(ex3, Day11.hasIncreasingStraight(ex3))
        Assert.assertFalse(ex3, Day11.hasBanned(ex3))
        Assert.assertFalse(ex3, Day11.hasTwoPairs(ex3))

        // The next password after abcdefgh is abcdffaa.
        Assert.assertEquals("next after abcdefgh", "abcdffaa", Day11.nextPass("abcdefgh"))

        // The next password after ghijklmn is ghjaabcc, because you eventually skip all the passwords that start with ghi..., since i is not allowed.
        Assert.assertEquals("next after ghijklmn", "ghjaabcc", Day11.nextPass("ghijklmn"))
    }

    @Test
    fun answer() {
        Assert.assertEquals("my answer", "cqjxxyzz", Day11.nextPass("cqjxjnds"))
        Assert.assertEquals("my answer", "cqkaabcc", Day11.nextPass("cqjxxyzz"))
    }
}