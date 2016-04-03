package advent


import advent.Day19.multiFind
import org.junit.Assert
import org.junit.Test

/**
 * Created by joeyt on 3/9/16.
 */
class Day19Test {
    @Test
    fun input() {
        Assert.assertEquals("input has 43 replacements", 43, Day19.puzzleInput.replacements.size)
        Assert.assertEquals("input has 488 characters", 488, Day19.puzzleInput.molecule.length)
    }

    @Test
    fun multiFind() {
        Assert.assertEquals("multfind!", listOf(1, 3), "abcb".multiFind("b"))
    }

    @Test
    fun example() {
        val replacements = listOf(
                Day19.Replacement("H", "HO"),
                Day19.Replacement("H", "OH"),
                Day19.Replacement("O", "HH")
        )

        val example1 = Day19.PuzzleInput(replacements, "HOH")
        Assert.assertEquals("HOH has 4 distinct", 4, Day19.findDistinct(example1).size)

        val example2 = Day19.PuzzleInput(replacements, "HOHOHO")
        Assert.assertEquals("HOHOHO has 7 distinct", 7, Day19.findDistinct(example2).size)
    }

    @Test
    fun answer() {
        Assert.assertEquals("my answer", 518, Day19.findDistinct(Day19.puzzleInput).size)
    }

    @Test
    fun answer2() {
        Assert.assertEquals("my answer", 200, Day19.reduceSteps3(Day19.puzzleInput))
    }

}