package advent


import org.junit.Assert
import org.junit.Test
import java.util.*

/**
 * Created by joeyt on 3/1/16.
 */
class Day17Test {

    @Test
    fun example() {
        val ex = listOf(20, 15, 10, 5, 5).map { it.toString() }
        val bins = Day17.input2Bins(ex)
        val combos = HashSet<List<Int>>()
        Day17.populateCombos(LinkedList(), bins.toCollection(LinkedList()), 25, combos)
        Assert.assertEquals("example had 4 solutions", 4, combos.size)
    }

    @Test
    fun answer() {
        Assert.assertEquals("my answer", 4372, Day17.answer())
    }

    @Test
    fun example2() {
        val ex = listOf(20, 15, 10, 5, 5).map { it.toString() }
        val bins = Day17.input2Bins(ex)
        val combos = HashSet<List<Int>>()
        Day17.populateCombos(LinkedList(), bins.toCollection(LinkedList()), 25, combos)
        val groupBy: Map<Int, List<List<Int>>> = combos.groupBy { it.size }
        val minCombos = groupBy.keys.min()!!
        val num = groupBy[minCombos]!!.size
        Assert.assertEquals("example had 3 ways", 3, num)
    }

    @Test
    fun answer2() {
        Assert.assertEquals("my answer", 4, Day17.answer2())
    }
}