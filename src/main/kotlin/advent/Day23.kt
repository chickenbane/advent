package advent

import java.io.File

/**
 * Created by joeyt on 4/16/16.
 */
object Day23 {
    private val copyPasta = """
--- Day 23: Opening the Turing Lock ---

Little Jane Marie just got her very first computer for Christmas from some unknown benefactor. It comes with instructions and an example program, but the computer itself seems to be malfunctioning. She's curious what the program does, and would like you to help her run it.

The manual explains that the computer supports two registers and six instructions (truly, it goes on to remind the reader, a state-of-the-art technology). The registers are named a and b, can hold any non-negative integer, and begin with a value of 0. The instructions are as follows:

hlf r sets register r to half its current value, then continues with the next instruction.
tpl r sets register r to triple its current value, then continues with the next instruction.
inc r increments register r, adding 1 to it, then continues with the next instruction.
jmp offset is a jump; it continues with the instruction offset away relative to itself.
jie r, offset is like jmp, but only jumps if register r is even ("jump if even").
jio r, offset is like jmp, but only jumps if register r is 1 ("jump if one", not odd).
All three jump instructions work with an offset relative to that instruction. The offset is always written with a prefix + or - to indicate the direction of the jump (forward or backward, respectively). For example, jmp +1 would simply continue with the next instruction, while jmp +0 would continuously jump back to itself forever.

The program exits when it tries to run an instruction beyond the ones defined.

For example, this program sets a to 2, because the jio instruction causes it to skip the tpl instruction:

inc a
jio a, +2
tpl a
inc a
What is the value in register b when the program in your puzzle input is finished executing?
"""
    enum class Register { a, b }

    sealed class Instruction {
        class Half(val register: Register) : Instruction()
        class Triple(val register: Register) : Instruction()
        class Increment(val register: Register) : Instruction()
        class Jump(val offset: Int) : Instruction()
        class JumpIfEven(val register: Register, val offset: Int) : Instruction()
        class JumpIfOne(val register: Register, val offset: Int) : Instruction()
    }

    val puzzleInput: List<String> by lazy { File("src/main/resources/input23.txt").readLines() }

    fun str2Instruction(str: String): Instruction {
        val tokens = str.split(" ")

        fun reg(): Register {
            require (tokens.size == 2)
            return Register.valueOf(tokens[1])
        }

        fun regOffset(): Pair<Register, Int> {
            require (tokens.size == 3)
            val reg = Register.valueOf(tokens[1].trimEnd(','))
            return Pair(reg, tokens[2].toInt())
        }

        return when (tokens[0]) {
            "hlf" -> Instruction.Half(reg())
            "tpl" -> Instruction.Triple(reg())
            "inc" -> Instruction.Increment(reg())
            "jmp" -> Instruction.Jump(tokens[1].toInt())
            "jie" -> {
                val (reg, offset) = regOffset()
                Instruction.JumpIfEven(reg, offset)
            }
            "jio" -> {
                val (reg, offset) = regOffset()
                Instruction.JumpIfOne(reg, offset)
            }
            else -> throw IllegalArgumentException("unknown instruction = ${tokens[0]}")
        }
    }

    val puzzleInstructions: Array<Instruction> by lazy { puzzleInput.map { str2Instruction(it) }.toTypedArray() }

    data class MachineState(val a: Long, val b: Long) {
        init { require (a >= 0 && b >= 0) }
    }

    fun evaluate(instructions: Array<Instruction>, initMachine: MachineState): MachineState {
        var currInstIdx = 0
        var a = initMachine.a
        var b = initMachine.b
        while (currInstIdx in instructions.indices) {
            val currInst = instructions[currInstIdx]
            var offset = 1
            when (currInst) {
                is Instruction.Half -> {
                    if (currInst.register == Register.a) a /= 2
                    else b /= 2
                }
                is Instruction.Triple -> {
                    if (currInst.register == Register.a) a *= 3
                    else b *= 3
                }
                is Instruction.Increment -> {
                    if (currInst.register == Register.a) a += 1
                    else b += 1
                }
                is Instruction.Jump -> offset = currInst.offset
                is Instruction.JumpIfEven -> {
                    val reg = if (currInst.register == Register.a) a else b
                    if (reg % 2L == 0L) offset = currInst.offset
                }
                is Instruction.JumpIfOne -> {
                    val reg = if (currInst.register == Register.a) a else b
                    if (reg == 1L) offset = currInst.offset
                }
            }
            require (a >= 0 && b >= 0)
            println("a = $a b = $b currIdx = $currInstIdx offset=$offset nextIdx=${currInstIdx + offset}")
            currInstIdx += offset
        }
        return MachineState(a, b)
    }

    private val part2 = """
    --- Part Two ---

The unknown benefactor is very thankful for releasi-- er, helping little Jane Marie with her computer. Definitely not to distract you, what is the value in register b after the program is finished executing if register a starts as 1 instead?
    """
}