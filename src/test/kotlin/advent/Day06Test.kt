package advent

import advent.Day06.Op
import org.junit.Assert
import org.junit.Test

/**
 * Created by a-jotsai on 1/28/16.
 */
class Day06Test {
    @Test
    fun examples() {
        //a coordinate pair like 0,0 through 2,2 therefore refers to 9 lights in a 3x3 square..
        Day06.gridOff()
        Day06.range(Op.ON, 0, 0, 2, 2)
        Assert.assertEquals("0,0 to 2,2 = 9 lights", 9, Day06.countLit())

        //toggle 0,0 through 999,0 would toggle the first line of 1000 lights, turning off the ones that were on, and turning on the ones that were off.
        Day06.gridOff()
        Day06.range(Op.ON, 0, 0, 999, 0)
        Assert.assertEquals("0,0 to 999,0 = 1000 lights", 1000, Day06.countLit())

        //turn off 499,499 through 500,500 would turn off (or leave off) the middle four lights.
        Day06.gridOff()
        Day06.range(Op.ON, 499, 499, 500, 500)
        Assert.assertEquals("499,499 to 500,500 = 4 lights", 4, Day06.countLit())
    }

    @Test
    fun testParse() {
        // first = toggle 461,550 through 564,900
        val first = Day06.instructions.first()
        Assert.assertEquals("first", Day06.Instruction(Op.TOGGLE, 461, 550, 564, 900), first)
        // last = turn on 222,12 through 856,241
        val last = Day06.instructions.last()
        Assert.assertEquals("first", Day06.Instruction(Op.ON, 222, 12, 856, 241), last)
    }

    @Test
    fun answer() {
        Day06.gridOff()
        Day06.instructions.forEach { Day06.instruction(it) }
        Assert.assertEquals("my answer", 543903, Day06.countLit())
    }

    @Test
    fun example2() {
        //        turn on 0,0 through 0,0 would increase the total brightness by 1.
        Day06.brightnessOff()
        Day06.brightInstruction(Day06.Instruction(Op.ON, 0, 0, 0, 0))
        Assert.assertEquals("0,0 to 0,0 == 1", 1, Day06.countBrightness())

        //        toggle 0,0 through 999,999 would increase the total brightness by 2000000.
        Day06.brightnessOff()
        Day06.brightInstruction(Day06.Instruction(Op.TOGGLE, 0, 0, 999, 999))
        Assert.assertEquals("0,0 to 999,999 == 2000000", 2000000, Day06.countBrightness())
    }

    @Test
    fun answer2() {
        Day06.brightnessOff()
        Day06.instructions.forEach { Day06.brightInstruction(it) }
        Assert.assertEquals("my answer", 14687245, Day06.countBrightness())
    }
}