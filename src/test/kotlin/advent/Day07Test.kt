package advent

import advent.Day07.BitOp.*
import org.junit.Assert
import org.junit.Test

/**
 * Created by a-jotsai on 1/28/16.
 */
class Day07Test {
    @Test
    fun examples() {
//        123 -> x
//        456 -> y
//        x AND y -> d
//        x OR y -> e
//        x LSHIFT 2 -> f
//        y RSHIFT 2 -> g
//        NOT x -> h
//        NOT y -> i
//
//        d: 72
//        e: 507
//        f: 492
//        g: 114
//        h: 65412
//        i: 65079
//        x: 123
//        y: 456

        val x = Const(123)
        val y = Const(456)
        val d = And(x, y)
        val e = Or(x, y)
        val f = Lshift(x, Const(2))
        val g = Rshift(y, Const(2))
        val h = Not(x)
        val i = Not(y)

        Assert.assertEquals("d=72", 72, Day07.eval(d))
        Assert.assertEquals("e=507", 507, Day07.eval(e))
        Assert.assertEquals("f=492", 492, Day07.eval(f))
        Assert.assertEquals("g=114", 114, Day07.eval(g))
        Assert.assertEquals("h=65412", 65412, Day07.eval(h))
        Assert.assertEquals("i=65079", 65079, Day07.eval(i))
        Assert.assertEquals("x=123", 123, Day07.eval(x))
        Assert.assertEquals("y=456", 456, Day07.eval(y))
    }

    @Test
    fun testParse() {
        Assert.assertEquals("file has 339 lines", 339, Day07.input.size)
        Assert.assertEquals("last line", "he RSHIFT 2 -> hf", Day07.input.last())
        Assert.assertEquals("first line", "NOT dq -> dr", Day07.input.first())

        Assert.assertEquals("339 entries", 339, Day07.circuit.wires.size)
    }

    @Test
    fun testCircuit() {
        val str = """
    123 -> x
    456 -> y
    x AND y -> d
    x OR y -> e
    x LSHIFT 2 -> f
    y RSHIFT 2 -> g
    NOT x -> h
    NOT y -> i
        """
        val circuit = Day07.Circuit(str.lines().filter { it.isNotBlank() })
        Assert.assertEquals("d=72", 72, Day07.eval(circuit.wires["d"]!!))
        Assert.assertEquals("e=507", 507, Day07.eval(circuit.wires["e"]!!))
        Assert.assertEquals("f=492", 492, Day07.eval(circuit.wires["f"]!!))
        Assert.assertEquals("g=114", 114, Day07.eval(circuit.wires["g"]!!))
        Assert.assertEquals("h=65412", 65412, Day07.eval(circuit.wires["h"]!!))
        Assert.assertEquals("i=65079", 65079, Day07.eval(circuit.wires["i"]!!))
        Assert.assertEquals("x=123", 123, Day07.eval(circuit.wires["x"]!!))
        Assert.assertEquals("y=456", 456, Day07.eval(circuit.wires["y"]!!))
    }

    fun printWire(key: String) {
        val v = Day07.circuit.wires[key]!!
        println("key=$key v=${Day07.strEval(v)}")
    }

    @Test
    fun answer() {
        Assert.assertEquals("my answer", 3176, Day07.eval(Day07.circuit.wires["a"]!!))
    }

    @Test
    fun answer2() {
        Assert.assertEquals("my answer", 14710, Day07.eval(Day07.circuit2.wires["a"]!!))
    }
}