package advent

import java.io.File
import java.util.*

/**
 * Created by a-jotsai on 2/2/16.
 */
object Day09 {
    private val part1 = """
--- Day 9: All in a Single Night ---

Every year, Santa manages to deliver all of his presents in a single night.

This year, however, he has some new locations to visit; his elves have provided him the distances between every pair of locations. He can start and end at any two (different) locations he wants, but he must visit each location exactly once. What is the shortest distance he can travel to achieve this?

For example, given the following distances:

London to Dublin = 464
London to Belfast = 518
Dublin to Belfast = 141
The possible routes are therefore:

Dublin -> London -> Belfast = 982
London -> Dublin -> Belfast = 605
London -> Belfast -> Dublin = 659
Dublin -> Belfast -> London = 659
Belfast -> Dublin -> London = 605
Belfast -> London -> Dublin = 982
The shortest of these is London -> Dublin -> Belfast = 605, and so the answer is 605 in this example.

What is the distance of the shortest route?
    """

    private val wiki = """
https://en.wikipedia.org/wiki/Permutation#Algorithms_to_generate_permutations

The following algorithm generates the next permutation lexicographically after a given permutation. It changes the given permutation in-place.

1) Find the largest index k such that a[k] < a[k + 1]. If no such index exists, the permutation is the last permutation.
2) Find the largest index l greater than k such that a[k] < a[l].
3) Swap the value of a[k] with that of a[l].
4) Reverse the sequence from a[k + 1] up to and including the final element a[n].

For example, given the sequence [1, 2, 3, 4] which starts in a weakly increasing order, and given that the index is zero-based, the steps are as follows:

1) Index k = 2, because 3 is placed at an index that satisfies condition of being the largest index that is still less than a[k + 1] which is 4.
2) Index l = 3, because 4 is the only value in the sequence that is greater than 3 in order to satisfy the condition a[k] < a[l].
3) The values of a[2] and a[3] are swapped to form the new sequence [1,2,4,3].
4) The sequence after k-index a[2] to the final element is reversed. Because only one value lies after this index (the 3), the sequence remains unchanged in this instance.
Thus the lexicographic successor of the initial state is permuted: [1,2,4,3].

Following this algorithm, the next lexicographic permutation will be [1,3,2,4],
and the 24th permutation will be [4,3,2,1] at which point a[k] < a[k + 1] does not exist,
indicating that this is the last permutation.
    """

    // updates the array in place
    // returns false if there are no more, or true if this method can be called again to get the next permutation
    fun nextLexPermutation(seq: IntArray): Boolean {
        if (seq.isEmpty()) {
            return false
        }
        var k = -1
        var l = -1
        for (i in 0..seq.lastIndex - 1) {
            if (seq[i] < seq[i + 1]) {
                k = i
            }
            if (k != -1 && seq[k] < seq[i + 1]) {
                l = i + 1
            }
        }
        if (k == -1) {
            return false
        }
        seq.swap(k, l)
        var rev = k + 1
        var top = seq.lastIndex
        while(rev < top) {
            seq.swap(rev, top)
            rev += 1
            top -= 1
        }
        return true
    }

    private fun IntArray.swap(i: Int, j: Int) {
        val tmp = this[i]
        this[i] = this[j]
        this[j] = tmp
    }

    fun createDistMap(dists: List<String>): Map<String, Map<String, Int>> {
        val map = HashMap<String, HashMap<String, Int>>()
        for (distStr in dists) {
            // London to Dublin = 464
            // 0      1  2      3 4
            // city1     city2    dist
            val list = distStr.split(" ")
            require(list.size == 5)
            require(list[1] == "to" && list[3] == "=")
            val city1 = list[0]
            val city2 = list[2]
            val dist = list[4].toInt()
            map.getOrPut(city1) { HashMap<String, Int>() }[city2] = dist
            map.getOrPut(city2) { HashMap<String, Int>() }[city1] = dist
        }
        val numCities = map.keys.size
        require(map.all { it.value.keys.size == numCities-1 })
        return Collections.unmodifiableMap(map)
    }

    fun routes(cities: Set<String>): Set<List<String>> {
        val cityList = cities.toArrayList()
        val cityIntArray = IntArray(cityList.size) { it }
        val set = HashSet<List<String>>()
        do {
            val list: List<String> = cityIntArray.map { cityList[it] }
            set.add(list)
            val again = nextLexPermutation(cityIntArray)
        } while (again)
        return Collections.unmodifiableSet(set)
    }

    class Salesman(val dists: List<String>) {
        val distMap = createDistMap(dists)
        val cities = distMap.keys
        val routes: Set<List<String>> = routes(cities)

        private fun routeDist(route: List<String>): Int {
            var dist = 0
            for (i in 0..route.lastIndex - 1) {
                val city1 = route[i]
                val city2 = route[i + 1]
                dist += distMap[city1]!![city2]!!
            }
            return dist
        }

        val shortest by lazy { routes.map { routeDist(it) }.min()!! }
        val longest by lazy { routes.map { routeDist(it) }.max()!! }
    }

    val input by lazy { File("src/main/resources/input9.txt").readLines() }

    private val part2 = """
    --- Part Two ---

The next year, just to show off, Santa decides to take the route with the longest distance instead.

He can still start and end at any two (different) locations he wants, and he still must visit each location exactly once.

For example, given the distances above, the longest route would be 982 via (for example) Dublin -> London -> Belfast.

What is the distance of the longest route?
    """
}