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

    // brute force all combos
    class NogBinCombo(val bins: Array<NogBin>) : Iterator<Array<NogBin>> {
        private val intArray = (0..bins.lastIndex).toList().toIntArray()
        private val combo = bins.copyOf()
        private var init = false
        private var next = true

        override fun hasNext(): Boolean = next

        override fun next(): Array<NogBin> {
            if (init) {
                next = Day09.nextLexPermutation(intArray)
            } else {
                init = true
            }
            for ((i, v) in intArray.withIndex()) {
                combo[i] = bins[v]
            }
            return combo
        }
    }

    val Eggnog = 150

    // check the given combo to see if it fits, returns empty set if not
    // using sets because 20 and 5 is the same as 5 and 20
    fun evalCombo(combo: Array<NogBin>, total: Int): Set<NogBin> {
        var sum = 0
        var idx = -1
        do {
            idx += 1
            sum += combo[idx].liters
        } while (sum < total)
        if (sum > total) return emptySet()
        println("found fit: ${printCombo(combo)}")
        return combo.slice(0..idx).toSet()
    }

    // This didn't work, turns out iterating through every combination was a bad idea.
    // 20! is a really big number.

    fun oldanswer(): Int {
        return findNumCombos(containers, Eggnog)
    }

    fun findNumCombos(bins: Array<NogBin>, total: Int): Int {
        val itr = NogBinCombo(bins)
        val combos = HashSet<Set<NogBin>>()
        var count = 0
        for (combo in itr) {
            //println("trying combo: ${printCombo(combo)}")
            val s = evalCombo(combo, total)
            if (s.isNotEmpty()) {
                combos.add(s)
            }
            count += 1
            if (count % 1000 == 0) {
                println("trying combo $count ${printCombo(combo)}")
            }
        }
        return combos.size
    }

    fun printCombo(combo: Array<NogBin>): String = combo.map { "${it.liters}L@${it.pos}" }.joinToString(" ")

    data class ItemAndRest(val item: NogBin, val rest: List<NogBin>)

    class ListAndRestIterator(val bins: Array<NogBin>) : Iterator<ItemAndRest> {
        private var pos = 0
        override fun hasNext(): Boolean = pos <= bins.lastIndex
        override fun next(): ItemAndRest {
            val copy = bins.toMutableList()
            val item = copy.removeAt(pos)
            pos += 1
            return ItemAndRest(item, copy)
        }
    }

    fun answer(): Int = findNumCombos3(containers, Eggnog)

    fun findNumCombos2(bins: Array<NogBin>, total: Int): Int {
        return countCombos(total, emptySet(), bins.toList())
        /*
        val itr = ListAndRestIterator(bins)
        var count = 0
        for (ir in itr) {
            val (item, rest) = ir
            count += countCombos(total, setOf(item), rest)
        }
        return count
        */
    }

    private fun countCombos(total: Int, set: Set<NogBin>, rest: List<NogBin>): Int {
        val curr = set.sumBy { it.liters }
        if (curr > total) return 0
        if (curr == total) return 1
        if (rest.isEmpty()) return 0
        val bins = rest.toTypedArray()
        val itr = ListAndRestIterator(bins)
        var sum = 0
        for (ir in itr) {
            val (item, nextRest) = ir
            val nextSet = HashSet(set)
            nextSet.add(item)
            sum += countCombos(total, nextSet, nextRest)
        }
        return sum
    }

    // ugh that didn't even work.

    fun findNumCombos3(bins: Array<NogBin>, total: Int): Int {
        val combos = HashSet<List<Int>>(2000)
        val arrayList = bins.toCollection(LinkedList())
        arrayList.sortByDescending { it.liters }
        findCombos3(LinkedList(), arrayList, total, combos)
        return combos.size
    }

    class ListAndRestIterator3(val bins: List<NogBin>) : Iterator<ItemAndRest3> {
        private val linkedList = LinkedList(bins)
        override fun hasNext(): Boolean = linkedList.isNotEmpty()
        override fun next(): ItemAndRest3 {
            val item = linkedList.removeFirst()
            return ItemAndRest3(item, linkedList)
        }
    }

    data class ItemAndRest3(val item: NogBin, val rest: LinkedList<NogBin>)

    private fun findCombos3(currList: LinkedList<NogBin>, rest: LinkedList<NogBin>, total: Int, combos: HashSet<List<Int>>): Unit {
        val currTotal = currList.sumBy { it.liters }
        if (currTotal > total) return
        if (currTotal == total) {
            val list = currList.map { it.pos }.sorted()
            combos.add(list)
            return
        }
        val delta = total - currTotal
        val binsLeft = rest.filter { it.liters <= delta }
        if (binsLeft.isEmpty()) return
        val itr = ListAndRestIterator3(binsLeft)
        for (ir in itr) {
            val currSize = combos.size
            val (item, nextRest) = ir
            val nextTotal = currTotal + item.liters
            if (nextTotal > total) continue
            val nextList = LinkedList(currList)
            nextList.add(item)
            if (nextTotal == total) {
                val list = nextList.map { it.pos }.sorted()
                combos.add(list)
                val newSize = combos.size
                if (newSize > currSize) println("total = $newSize adding ${nextList.map { "${it.liters}@${it.pos}" }.joinToString(" ")}")
            } else {
                findCombos3(nextList, nextRest, total, combos)
            }
        }
    }
}