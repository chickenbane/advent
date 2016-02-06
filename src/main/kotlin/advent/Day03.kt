package advent

import java.io.File
import java.util.*

/**
 * Created by a-jotsai on 1/27/16.
 */
object Day03 {
    private val part1 = """
--- Day 3: Perfectly Spherical Houses in a Vacuum ---

Santa is delivering presents to an infinite two-dimensional grid of houses.

He begins by delivering a present to the house at his starting location, and then an elf at the North Pole calls him via radio and tells him where to move next.
Moves are always exactly one house to the north (^), south (v), east (>), or west (<). After each move, he delivers another present to the house at his new location.

However, the elf back at the north pole has had a little too much eggnog, and so his directions are a little off,
and Santa ends up visiting some houses more than once. How many houses receive at least one present?

For example:

> delivers presents to 2 houses: one at the starting location, and one to the east.
^>v< delivers presents to 4 houses in a square, including twice to the house at his starting/ending location.
^v^v^v^v^v delivers a bunch of presents to some very lucky children at only 2 houses.
    """

    data class House(val x: Int, val y: Int) {
        fun north() = House(x, y+1)
        fun south() = House(x, y-1)
        fun east() = House(x+1, y)
        fun west() = House(x-1, y)

        fun go(arrow: Char): House = when(arrow) {
            '^' -> north()
            'v' -> south()
            '>' -> east()
            '<' -> west()
            else -> throw IllegalArgumentException("bad move: $arrow")
        }
    }

    private val startHouse = House(0, 0)

    fun parseElfRadio(input: String): List<House> {
        val houses = arrayListOf(startHouse)
        var curr = startHouse
        input.forEach {
            val next = curr.go(it)
            houses.add(next)
            curr = next
        }
        return Collections.unmodifiableList(houses)
    }

    fun numberHouses(input: String): Int = parseElfRadio(input).toSet().size

    private val moves: String by lazy {  File("src/main/resources/input3.txt").readText() }

    fun part1Answer(): Int = numberHouses(moves)

    private val part2 = """
--- Part Two ---

The next year, to speed up the process, Santa creates a robot version of himself, Robo-Santa, to deliver presents with him.

Santa and Robo-Santa start at the same location (delivering two presents to the same starting house),
then take turns moving based on instructions from the elf, who is eggnoggedly reading from the same script as the previous year.

This year, how many houses receive at least one present?

For example:

^v delivers presents to 3 houses, because Santa goes north, and then Robo-Santa goes south.
^>v< now delivers presents to 3 houses, and Santa and Robo-Santa end up back where they started.
^v^v^v^v^v now delivers presents to 11 houses, with Santa going one direction and Robo-Santa going the other.
        """

    fun housesWithRobot(input: String): Int {
        val santaMoves = ArrayList<Char>()
        val roboMoves = ArrayList<Char>()
        input.forEachIndexed { i, c ->
            if (i % 2 == 0) {
                roboMoves.add(c)
            } else {
                santaMoves.add(c)
            }
        }
        val santaHouses = parseElfRadio(santaMoves.joinToString(separator = ""))
        val roboHouses = parseElfRadio(roboMoves.joinToString(separator = ""))
        val bothHouses = santaHouses.toCollection(arrayListOf<House>())
        bothHouses.addAll(roboHouses)
        return bothHouses.toSet().size
    }

    fun part2Answer(): Int = housesWithRobot(moves)
}