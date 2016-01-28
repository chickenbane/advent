package advent

import java.io.File

/**
 * Created by a-jotsai on 1/28/16.
 */
object Day06 {
    private val part1 = """
--- Day 6: Probably a Fire Hazard ---

Because your neighbors keep defeating you in the holiday house decorating contest year after year, you've decided to deploy one million lights in a 1000x1000 grid.

Furthermore, because you've been especially nice this year, Santa has mailed you instructions on how to display the ideal lighting configuration.

Lights in your grid are numbered from 0 to 999 in each direction; the lights at each corner are at 0,0, 0,999, 999,999, and 999,0.
The instructions include whether to turn on, turn off, or toggle various inclusive ranges given as coordinate pairs.
Each coordinate pair represents opposite corners of a rectangle, inclusive; a coordinate pair like 0,0 through 2,2
therefore refers to 9 lights in a 3x3 square. The lights all start turned off.

To defeat your neighbors this year, all you have to do is set up your lights by doing the instructions Santa sent you in order.

For example:

turn on 0,0 through 999,999 would turn on (or leave on) every light.
toggle 0,0 through 999,0 would toggle the first line of 1000 lights, turning off the ones that were on, and turning on the ones that were off.
turn off 499,499 through 500,500 would turn off (or leave off) the middle four lights.

After following the instructions, how many lights are lit?
    """

    enum class Op { TOGGLE, OFF, ON }

    data class Light(val x: Int, val y: Int, var on: Boolean = false) {
        fun op(op: Op): Unit = when(op) {
            Op.TOGGLE -> on = !on
            Op.OFF -> on = false
            Op.ON -> on = true
        }
    }

    val GridSize = 1000

    val grid = Array(GridSize) { x ->
        Array(GridSize) { y ->
            Light(x, y)
        }
    }

    fun range(op: Op, startX: Int, startY: Int, endX: Int, endY: Int): Unit {
        require(startX <= endX && startY <= endY)
        for (i in startX..endX) {
            for (j in startY..endY) {
                grid[i][j].op(op)
            }
        }
    }

    fun countLit(): Int {
        var count = 0
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (grid[i][j].on) {
                    count += 1
                }
            }
        }
        return count
    }

    fun gridOff(): Unit {
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                grid[i][j].op(Op.OFF)
            }
        }
    }

    data class Instruction(val op: Op, val startX: Int, val startY: Int, val endX: Int, val endY: Int)

    fun instruction(i: Instruction): Unit {
        range(i.op, i.startX, i.startY, i.endX, i.endY)
    }

    //        turn on 606,361 through 892,600
    //        turn off 448,208 through 645,684
    //        toggle 50,472 through 452,788
    private fun parseInstruction(input: String): Instruction = when {
        input.startsWith("toggle") -> parseInstructionRange(Op.TOGGLE, input.substring("toggle".length))
        input.startsWith("turn on") -> parseInstructionRange(Op.ON, input.substring("turn on".length))
        input.startsWith("turn off") -> parseInstructionRange(Op.OFF, input.substring("turn off".length))
        else -> throw IllegalArgumentException("invalid input=$input")
    }

    private fun parseInstructionRange(op: Op, range: String): Instruction {
        val startAndEnd: List<String> = range.split("through")
        require(startAndEnd.size == 2)
        val xys = startAndEnd.flatMap { it.trim().split(",") }.map { it.toInt() }.toIntArray()
        require(xys.size == 4)
        return Instruction(op, xys[0], xys[1], xys[2], xys[3])
    }

    val instructions: List<Instruction> by lazy {
        File("src/main/resources/input6.txt").readLines().map { parseInstruction(it) }
    }

    private val part2 = """
--- Part Two ---

You just finish implementing your winning light pattern when you realize you mistranslated Santa's message from Ancient Nordic Elvish.

The light grid you bought actually has individual brightness controls; each light can have a brightness of zero or more. The lights all start at zero.

The phrase turn on actually means that you should increase the brightness of those lights by 1.

The phrase turn off actually means that you should decrease the brightness of those lights by 1, to a minimum of zero.

The phrase toggle actually means that you should increase the brightness of those lights by 2.

What is the total brightness of all lights combined after following Santa's instructions?

For example:

turn on 0,0 through 0,0 would increase the total brightness by 1.
toggle 0,0 through 999,999 would increase the total brightness by 2000000.
    """

    data class BrightLight(val x: Int, val y: Int, var brightness: Int = 0) {
        fun op(op: Op): Unit = when(op) {
            Op.TOGGLE -> brightness += 2
            Op.OFF ->    brightness = Math.max(0, brightness-1)
            Op.ON ->     brightness += 1
        }
    }

    val brightGrid = Array(GridSize) { x ->
        Array(GridSize) { y ->
            BrightLight(x, y)
        }
    }

    fun brightInstruction(ins: Instruction): Unit {
        for (i in ins.startX..ins.endX) {
            for (j in ins.startY..ins.endY) {
                brightGrid[i][j].op(ins.op)
            }
        }
    }

    fun countBrightness(): Int {
        var count = 0
        for (i in brightGrid.indices) {
            for (j in brightGrid[i].indices) {
                count += brightGrid[i][j].brightness
            }
        }
        return count
    }

    fun brightnessOff(): Unit {
        for (i in brightGrid.indices) {
            for (j in brightGrid[i].indices) {
                brightGrid[i][j].brightness = 0
            }
        }
    }

}