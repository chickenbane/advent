package advent

import org.junit.Assert
import org.junit.Test

/**
 * Created by a-jotsai on 2/2/16.
 */
class Day09Test {
    @Test
    fun testNextLex() {
        val list = intArrayOf(1, 2, 3, 4)
        var count = 0
        do {
            val again = Day09.nextLexPermutation(list)
            count += 1
        } while (again)

        Assert.assertEquals("24 permutations", 24, count)
        Assert.assertArrayEquals("last permutation", intArrayOf(4, 3, 2, 1), list)
    }

    @Test
    fun testExample() {
        val input = """
London to Dublin = 464
London to Belfast = 518
Dublin to Belfast = 141
    """
        val inputLines = input.lines().filter { it.isNotBlank() }
        val map = Day09.createDistMap(inputLines)
        println("my map = $map")
        val routes = Day09.routes(map.keys)
        println("my routes = $routes")

        val salesman = Day09.Salesman(inputLines)
        Assert.assertEquals("605 is shortest", 605, salesman.shortest)
        Assert.assertEquals("982 is longest", 982, salesman.longest)
    }

    @Test
    fun answer() {
        val salesman = Day09.Salesman(Day09.input)
        Assert.assertEquals("my answer for shortest", 117, salesman.shortest)
        Assert.assertEquals("my answer for longest", 909, salesman.longest)
    }



}