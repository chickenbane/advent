package advent

import java.io.File
import java.util.*

/**
 * Created by joeyt on 2/26/16.
 */
object Day17 {
    private val copyPasta = """
--- Day 17: No Such Thing as Too Much ---

The elves bought too much eggnog again - 150 liters this time. To fit it all into your refrigerator,
you'll need to move it into smaller containers. You take an inventory of the capacities of the available containers.

For example, suppose you have containers of size 20, 15, 10, 5, and 5 liters. If you need to store 25 liters, there are four ways to do it:

15 and 10
20 and 5 (the first 5)
20 and 5 (the second 5)
15, 5, and 5
Filling all containers entirely, how many different combinations of containers can exactly fit all 150 liters of eggnog?
"""

    val puzzleInput: List<String> by lazy { File("src/main/resources/input17.txt").readLines() }

    // eggnog container
    // saving position since first 5 is different from second 5
    data class NogBin(val liters: Int, val pos: Int)

    fun input2Bins(input: List<String>): Array<NogBin> = input.mapIndexed { i, l -> NogBin(l.toInt(), i) }.toTypedArray()

    val containers: Array<NogBin> by lazy { input2Bins(puzzleInput) }

    val TotalEggnog = 150

    fun answer(): Int {
        val combos = HashSet<List<Int>>(4372)
        populateCombos(LinkedList(), containers.toCollection(LinkedList()), TotalEggnog, combos)
        return combos.size
    }

    fun populateCombos(currList: LinkedList<NogBin>, rest: LinkedList<NogBin>, total: Int, combos: HashSet<List<Int>>): Unit {
        val currTotal = currList.sumBy { it.liters }
        if (currTotal > total) return
        if (currTotal == total) {
            val currSize = combos.size
            val list = currList.map { it.pos }.sorted()
            combos.add(list)
            val newSize = combos.size
            if (newSize > currSize) println("total = $newSize adding ${currList.map { "${it.liters}@${it.pos}" }.joinToString(" ")}")
            return
        }
        val delta = total - currTotal
        val remaining = rest.filter { it.liters <= delta }.toCollection(LinkedList())
        while (remaining.isNotEmpty()) {
            val next = remaining.removeFirst()
            val nextList = LinkedList(currList)
            nextList.add(next)
            populateCombos(nextList, remaining, total, combos)
        }
    }

    private val part2 = """
    --- Part Two ---

While playing with all the containers in the kitchen, another load of eggnog arrives! The shipping and receiving department is requesting as many containers as you can spare.

Find the minimum number of containers that can exactly fit all 150 liters of eggnog. How many different ways can you fill that number of containers and still hold exactly 150 litres?

In the example above, the minimum number of containers was two. There were three ways to use that many containers, and so the answer there would be 3.
    """

    fun answer2(): Int {
        val combos = HashSet<List<Int>>(4372)
        populateCombos(LinkedList(), containers.toCollection(LinkedList()), TotalEggnog, combos)
        val groupBy: Map<Int, List<List<Int>>> = combos.groupBy { it.size }
        val minCombos = groupBy.keys.min()!!
        return groupBy[minCombos]!!.size
    }
}