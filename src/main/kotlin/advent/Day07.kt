package advent

import java.io.File
import java.util.*

/**
 * Created by a-jotsai on 1/28/16.
 */
object Day07 {
    private val part1 = """
--- Day 7: Some Assembly Required ---

This year, Santa brought little Bobby Tables a set of wires and bitwise logic gates! Unfortunately,
little Bobby is a little under the recommended age range, and he needs help assembling the circuit.

Each wire has an identifier (some lowercase letters) and can carry a 16-bit signal (a number from 0 to 65535). A signal
is provided to each wire by a gate, another wire, or some specific value. Each wire can only get a signal from one source,
but can provide its signal to multiple destinations. A gate provides no signal until all of its inputs have a signal.

The included instructions booklet describes how to connect the parts together: x AND y -> z means to connect wires
x and y to an AND gate, and then connect its output to wire z.

For example:

123 -> x means that the signal 123 is provided to wire x.

x AND y -> z means that the bitwise AND of wire x and wire y is provided to wire z.

p LSHIFT 2 -> q means that the value from wire p is left-shifted by 2 and then provided to wire q.

NOT e -> f means that the bitwise complement of the value from wire e is provided to wire f.

Other possible gates include OR (bitwise OR) and RSHIFT (right-shift). If, for some reason, you'd like to emulate the circuit instead,
 almost all programming languages (for example, C, JavaScript, or Python) provide operators for these gates.

For example, here is a simple circuit:

123 -> x
456 -> y
x AND y -> d
x OR y -> e
x LSHIFT 2 -> f
y RSHIFT 2 -> g
NOT x -> h
NOT y -> i
After it is run, these are the signals on the wires:

d: 72
e: 507
f: 492
g: 114
h: 65412
i: 65079
x: 123
y: 456

In little Bobby's kit's instructions booklet (provided as your puzzle input), what signal is ultimately provided to wire a?
    """

    sealed class BitOp {
        class Const(val x: Int) : BitOp()
        class Not(val x: BitOp) : BitOp()
        class And(val x: BitOp, val y: BitOp) : BitOp()
        class Or(val x: BitOp, val y: BitOp) : BitOp()
        class Lshift(val x: BitOp, val y: BitOp) : BitOp()
        class Rshift(val x: BitOp, val y: BitOp) : BitOp()
    }

    // UGH
    // In Kotlin bitwise operators only avaliable for Int and Long
    // This problem requires 16-bit numbers, unsigned (java shorts are signed)
    fun eval(op: BitOp): Int = when(op) {
        is BitOp.Const -> op.x

        // doing not on int
        // convert to unsigned
        is BitOp.Not -> eval(op.x).inv().and(0xffff)

        is BitOp.And -> eval(op.x).and(eval(op.y))
        is BitOp.Or -> eval(op.x).or(eval(op.y))
        is BitOp.Lshift -> eval(op.x).shl(eval(op.y))
        is BitOp.Rshift -> eval(op.x).shr(eval(op.y))
    }

    fun strEval(op: BitOp): String = when(op) {
        is BitOp.Const -> "${op.x}"
        is BitOp.Not -> "Not(${strEval(op.x)})"
        is BitOp.And -> "And(${strEval(op.x)}, ${strEval(op.y)})"
        is BitOp.Or -> "Or(${strEval(op.x)}, ${strEval(op.y)})"
        is BitOp.Lshift -> "Lshift(${strEval(op.x)}, ${strEval(op.y)})"
        is BitOp.Rshift -> "Rshift(${strEval(op.x)}, ${strEval(op.y)})"
    }

    val input: List<String> by lazy { File("src/main/resources/input7.txt").readLines() }

    val circuit: Circuit by lazy { Circuit(input) }

    // Put the lines in ops into a map, keys are the wire name, value is the BitOp ADT
    // This process requires using the map while populating it (later operations refer to previous ones) so this
    // class encapsulates the circuit creation process ("opMap" is for construction, "wires" is for client use)
    class Circuit(val signals: List<String>) {
        private val opMap = HashMap<String, BitOp>(signals.size)

        init {
            val signalsRemaining = signals.toCollection(arrayListOf<String>())
            while (signalsRemaining.isNotEmpty()) {
                //println("remaining signals left to process = ${opsRemaining.size}")
                val ops = signalsRemaining.toCollection(arrayListOf<String>())
                signalsRemaining.clear()
                for (signal in ops) {
                    val pair = parseSignal(signal)
                    if (pair == null) {
                        signalsRemaining.add(signal)
                    } else {
                        val (str, op) = pair

                        // adding the op doesn't work, it makes eval() take forever
                        // because (?) there are cycles in the circuit
                        //opMap.put(str, op)

                        // therefore, reduce the op
                        val v = eval(op)
                        println("reducing wire=$str ${strEval(op)} -> $v")
                        opMap.put(str, BitOp.Const(v))
                    }
                }
            }
        }

        val wires: Map<String, BitOp> by lazy {
            Collections.unmodifiableMap(opMap)
        }

        // signal:
        //        123 -> x
        //        x AND y -> d
        //        x OR y -> e
        //        x LSHIFT 2 -> f
        //        y RSHIFT 2 -> g
        //        NOT y -> i

        // gets the wire name and BitOp, or null if refers to wire not yet in circuit
        private fun parseSignal(signal: String): Pair<String, BitOp>? {
            val opStrAndName = signal.split("->")
            require(opStrAndName.size == 2)
            val name = opStrAndName[1].trim()
            val op = parseOp(opStrAndName[0]) ?: return null
            return name to op
        }


        // return the BitOp on the left side of the signal
        // opStr:
        //        123
        //        x AND y
        //        x OR y
        //        x LSHIFT 2
        //        y RSHIFT 2
        //        NOT y
        private fun parseOp(opStr: String): BitOp? = when {
            // can pass class constructors as functions?
            opStr.contains("LSHIFT") -> comboOp(opStr, "LSHIFT") { x, y -> BitOp.Lshift(x, y) }
            opStr.contains("RSHIFT") -> comboOp(opStr, "RSHIFT") { x, y -> BitOp.Rshift(x, y) }
            opStr.contains("AND") -> comboOp(opStr, "AND") { x, y -> BitOp.And(x, y) }
            opStr.contains("OR") -> comboOp(opStr, "OR") { x, y -> BitOp.Or(x, y) }
            opStr.contains("NOT") -> {
                val xList = opStr.split("NOT")
                // FYI: "NOT x".split("NOT") returns [, x]
                require(xList.size == 2)
                val op = str2Op(xList.last())
                if (op == null) null else BitOp.Not(op)
            }
            else -> str2Op(opStr)
        }

        // returns one of the "combo" BitOps which have two BitOps nested, or null if either not yet in circuit
        private fun comboOp(opStr: String, split: String, construct: (BitOp, BitOp) -> BitOp): BitOp? {
            val xAndY = opStr.split(split)
            require(xAndY.size == 2)
            val x = str2Op(xAndY[0])
            val y = str2Op(xAndY[1])
            if (x == null || y == null) {
                return null
            }
            return construct(x, y)
        }

        // returns either a Const or the BitOp referenced, or null if BitOp not yet in circuit
        private fun str2Op(str: String): BitOp? {
            val trimmed = str.trim()
            if (trimmed.toCharArray().all { it.isDigit() }) {
                return BitOp.Const(trimmed.toInt())
            } else {
                return opMap[trimmed]
            }
        }
    }

    private val part2 = """
--- Part Two ---

Now, take the signal you got on wire a, override wire b to that signal, and reset the other wires (including wire a).
What new signal is ultimately provided to wire a?
    """

    val input2 by lazy { input.map { if (it.endsWith("-> b")) "3176 -> b" else it } }
    val circuit2: Circuit by lazy { Circuit(input2) }


}