package advent

import org.junit.Assert
import org.junit.Test

/**
 * Created by a-jotsai on 12/28/15.
 */
class Day01Test {
    // (()) and ()() both result in floor 0.
    @Test fun testExample1() {
        Assert.assertEquals("floor 0", 0, Day01.parseInput("(())"))
        Assert.assertEquals("floor 0", 0, Day01.parseInput("()()"))
    }

    // ((( and (()(()( both result in floor 3.
    @Test fun testExample2() {
        Assert.assertEquals("floor 3", 3, Day01.parseInput("((("))
        Assert.assertEquals("floor 3", 3, Day01.parseInput("(()(()("))
    }

    // ))((((( also results in floor 3.
    @Test fun testExample3() {
        Assert.assertEquals("floor 3", 3, Day01.parseInput("))((((("))
    }

    // ()) and ))( both result in floor -1 (the first basement level).
    @Test fun testExample4() {
        Assert.assertEquals("floor -1", -1, Day01.parseInput("())"))
        Assert.assertEquals("floor -1", -1, Day01.parseInput("))("))
    }

    // ))) and )())()) both result in floor -3.
    @Test fun testExample5() {
        Assert.assertEquals("floor -3", -3, Day01.parseInput(")))"))
        Assert.assertEquals("floor -3", -3, Day01.parseInput(")())())"))
    }

    // ) causes him to enter the basement at character position 1.
    @Test fun testPart2Ex1() {
        Assert.assertEquals("basement @ 1", 1, Day01.findBasementPos(")"))
    }

    // ()()) causes him to enter the basement at character position 5.
    @Test fun testPart2Ex2() {
        Assert.assertEquals("basement @ 5", 5, Day01.findBasementPos("()())"))
    }
}