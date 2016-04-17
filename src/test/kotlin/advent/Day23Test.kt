package advent


import org.junit.Assert
import org.junit.Test

/**
 * Created by joeyt on 4/16/16.
 */
class Day23Test {

    val InitialMachine = Day23.MachineState(0, 0)
    @Test
    fun example() {
        val instructions: Array<Day23.Instruction> = """
inc a
jio a, +2
tpl a
inc a
        """.lines().filter { it.isNotBlank() }.map { Day23.str2Instruction(it) }.toTypedArray()
        val state = Day23.evaluate(instructions, InitialMachine)

        Assert.assertEquals("example reg a == 2", 2, state.a)
        Assert.assertEquals("example reg b == 0", 0, state.b)
    }

    @Test
    fun answer() {
        Assert.assertEquals("my answer", 170, Day23.evaluate(Day23.puzzleInstructions, InitialMachine).b)
    }

    @Test
    fun answer2() {
        val initial2 = Day23.MachineState(1, 0)
        Assert.assertEquals("my answer", 247, Day23.evaluate(Day23.puzzleInstructions, initial2).b)
    }
}