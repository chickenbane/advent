package advent

import java.io.File
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by joeyt on 3/8/16.
 */
object Day19 {
    private val copyPasta = """
--- Day 19: Medicine for Rudolph ---

Rudolph the Red-Nosed Reindeer is sick! His nose isn't shining very brightly, and he needs medicine.

Red-Nosed Reindeer biology isn't similar to regular reindeer biology; Rudolph is going to need custom-made medicine. Unfortunately, Red-Nosed Reindeer chemistry isn't similar to regular reindeer chemistry, either.

The North Pole is equipped with a Red-Nosed Reindeer nuclear fusion/fission plant, capable of constructing any Red-Nosed Reindeer molecule you need. It works by starting with some input molecule and then doing a series of replacements, one per step, until it has the right molecule.

However, the machine has to be calibrated before it can be used. Calibration involves determining the number of molecules that can be generated in one step from a given starting point.

For example, imagine a simpler machine that supports only the following replacements:

H => HO
H => OH
O => HH
Given the replacements above and starting with HOH, the following molecules could be generated:

HOOH (via H => HO on the first H).
HOHO (via H => HO on the second H).
OHOH (via H => OH on the first H).
HOOH (via H => OH on the second H).
HHHH (via O => HH).
So, in the example above, there are 4 distinct molecules (not five, because HOOH appears twice) after one replacement from HOH. Santa's favorite molecule, HOHOHO, can become 7 distinct molecules (over nine replacements: six from H, and three from O).

The machine replaces without regard for the surrounding characters. For example, given the string H2O, the transition H => OO would result in OO2O.

Your puzzle input describes all of the possible replacements and, at the bottom, the medicine molecule for which you need to calibrate the machine. How many distinct molecules can be created after all the different ways you can do one replacement on the medicine molecule?


"""

    // replacements can't be a map because there are multiple "keys"
    data class Replacement(val left: String, val right: String)

    data class PuzzleInput(val replacements: List<Replacement>, val molecule: String)

    private val Sep = " => "

    val puzzleInput by lazy {
        val lines = File("src/main/resources/input19.txt").readLines().filter { it.isNotBlank() }
        val list = lines.filter { Sep in it }.map {
            val tokens = it.split(Sep)
            require(tokens.size == 2)
            Replacement(tokens[0], tokens[1])
        }
        val molecule = lines.filterNot { Sep in it }.single()
        PuzzleInput(list, molecule)
    }

    fun findDistinct(input: PuzzleInput): Set<String> {
        val set = HashSet<String>()
        for (rep in input.replacements) {
            val indices = input.molecule.multiFind(rep.left)
            for (i in indices) {
                val mol = input.molecule.replaceRange(i, i + rep.left.length, rep.right)
                set.add(mol)
            }
        }
        return set
    }

    // returns indices of the occurrences of string in this
    fun String.multiFind(string: String): List<Int> {
        val list = LinkedList<Int>()
        var startIdx = 0
        do {
            val idx = this.indexOf(string, startIdx)
            if (idx != -1) {
                list.add(idx)
                startIdx = idx + 1
            }
        } while (idx != -1)
        return list
    }

    private val part2 = """
    --- Part Two ---

Now that the machine is calibrated, you're ready to begin molecule fabrication.

Molecule fabrication always begins with just a single electron, e, and applying replacements one at a time, just like the ones during calibration.

For example, suppose you have the following replacements:

e => H
e => O
H => HO
H => OH
O => HH
If you'd like to make HOH, you start with e, and then make the following replacements:

e => O to get O
O => HH to get HH
H => OH (on the second H) to get HOH
So, you could make HOH after 3 steps. Santa's favorite molecule, HOHOHO, can be made in 6 steps.

How long will it take to make the medicine? Given the available replacements and the medicine molecule in your puzzle input, what is the fewest number of steps to go from e to the medicine molecule?
    """

    // go backwards?
    // reduces molecule to the returned set given the replacements
    fun reduceMolecule(molecule: String, replacements: List<Replacement>): Set<String> {
        val set = HashSet<String>()
        for (rep in replacements) {
            val indices = molecule.multiFind(rep.right)
            for (i in indices) {
                val mol = molecule.replaceRange(i, i + rep.right.length, rep.left)
                set.add(mol)
            }
        }
        return set
    }


    // str is descending in this graph, so root is the answer and the deepest child is the e
    private class Node(val str: String) {
        val children: MutableSet<Node> = HashSet()
        var parent by Delegates.notNull<Node>()

        val isElectron: Boolean = str == "e"

        fun parentMatching(nodes: Set<Node>): Unit {
            children.filter { it in nodes }.forEach {
                it.parent = this
            }
        }
    }

    // the replacements go from right to left, then.  so HO -> H
    private class Graph(val replacements: List<Replacement>) {
        private val population = LinkedHashMap<String, Node>()
        fun getNode(str: String): Node = population.getOrPut(str) { Node(str) }

        fun createNode(str: String): Node {
            val node = getNode(str)
            populateNode(node)
            return node
        }

        fun populateNode(node: Node): Unit {
            val reductions = reduceMolecule(node.str, replacements)
            val children = reductions.map { getNode(it) }
            node.children.addAll(children)
        }

        fun populate(root: Node): Unit {
            val queue = LinkedList<Node>()
            queue.add(root)
            var complete = false
            while (queue.isNotEmpty()) {
                val node = queue.minBy { it.str.length }!!
                val removed = queue.remove(node)
                require(removed)
                if (node.isElectron) {
                    complete = true
                } else if (node.children.isEmpty()) {
                    populateNode(node)
                }
                if (!complete) queue.addAll(node.children)
            }
            println("populated size = ${population.size}")
            require(complete)
        }

        fun findPath(root: Node): List<String> {
            val queue = LinkedList<Node>()
            queue.add(root)
            val seen = HashSet<String>()
            seen.add(root.str)
            while (queue.isNotEmpty()) {
                val electron = queue.find { it.isElectron }
                if (electron != null) {
                    var node: Node = electron
                    val path = LinkedList<String>()
                    while (node != root) {
                        path.add(node.str)
                        node = node.parent
                    }
                    return path
                }
                seen.addAll(queue.map { it.str })
                val nextQueue = queue.flatMap { it.children }.filter { it.str !in seen }.toSet()
                queue.forEach { it.parentMatching(nextQueue) }

                queue.clear()
                queue.addAll(nextQueue)
            }
            throw IllegalStateException("must be found, yo.")
        }
    }

    fun numSteps(input: PuzzleInput): Int {
        val graph = Graph(input.replacements)
        val root = graph.createNode(input.molecule)
        graph.populate(root)
        val path = graph.findPath(root)
        path.forEachIndexed { i, s ->
            println("$i (${s.length}) = $s")
        }
        return path.size
    }
}
