package advent

import java.io.File
import java.util.*

/**
 * Created by joeyt on 3/7/16.
 */
object Day18 {
    private val copyPasta = """
--- Day 18: Like a GIF For Your Yard ---

After the million lights incident, the fire code has gotten stricter: now, at most ten thousand lights are allowed. You arrange them in a 100x100 grid.

Never one to let you down, Santa again mails you instructions on the ideal lighting configuration. With so few lights, he says, you'll have to resort to animation.

Start by setting your lights to the included initial configuration (your puzzle input). A # means "on", and a . means "off".

Then, animate your grid in steps, where each step decides the next configuration based on the current one. Each light's next state (either on or off) depends on its current state and the current states of the eight lights adjacent to it (including diagonals). Lights on the edge of the grid might have fewer than eight neighbors; the missing ones always count as "off".

For example, in a simplified 6x6 grid, the light marked A has the neighbors numbered 1 through 8, and the light marked B, which is on an edge, only has the neighbors marked 1 through 5:

1B5...
234...
......
..123.
..8A4.
..765.
The state a light should have next is based on its current state (on or off) plus the number of neighbors that are on:

A light which is on stays on when 2 or 3 neighbors are on, and turns off otherwise.
A light which is off turns on if exactly 3 neighbors are on, and stays off otherwise.
All of the lights update simultaneously; they all consider the same current state before moving to the next.

Here's a few steps from an example configuration of another 6x6 grid:

Initial state:
.#.#.#
...##.
#....#
..#...
#.#..#
####..

After 1 step:
..##..
..##.#
...##.
......
#.....
#.##..

After 2 steps:
..###.
......
..###.
......
.#....
.#....

After 3 steps:
...#..
......
...#..
..##..
......
......

After 4 steps:
......
......
..##..
..##..
......
......
After 4 steps, this example has four lights on.

In your grid of 100x100 lights, given your initial configuration, how many lights are on after 100 steps?


"""

    val puzzleInput: List<String> by lazy { File("src/main/resources/input18.txt").readLines() }

    fun input2Grid(input: List<String>): Array<BooleanArray> {
        val list = ArrayList<BooleanArray>()
        for (line in input) {
            val arr = BooleanArray(line.length) { line[it] == '#' }
            list.add(arr)
        }

        // validate all arrays are the same size
        list.all { it.size == list[0].size }

        return list.toTypedArray()
    }

    val puzzleGrid by lazy { input2Grid(puzzleInput) }

    fun step(grid: Array<BooleanArray>): Array<BooleanArray> {
        val nextGrid = Array(grid.size) { BooleanArray(grid[it].size) }
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                val light = grid[row][col]
                val neighbors = countNeighborsOn(grid, row, col)
                val nextLight = if (light) {
                    neighbors == 2 || neighbors == 3
                } else {
                    neighbors == 3
                }
                nextGrid[row][col] = nextLight
            }
        }
        return nextGrid
    }

    fun countOn(grid: Array<BooleanArray>): Int {
        var count = 0
        for (row in grid.indices) {
            count += grid[row].count { it }
        }
        return count
    }

    fun countNeighborsOn(grid: Array<BooleanArray>, row: Int, col: Int): Int {
        fun lightOn(rowOffset: Int, colOffset: Int): Boolean {
            val r = row + rowOffset
            if (r !in grid.indices) return false
            val c = col + colOffset
            if (c !in grid[r].indices) return false
            return grid[r][c]
        }

        val lights = LinkedList<Boolean>()
        for (r in -1..1) {
            for (c in -1..1) {
                if (r == 0 && c == 0) continue
                lights.add(lightOn(r, c))
            }
        }
        return lights.count { it }
    }

    private val part2 = """
    --- Part Two ---

You flip the instructions over; Santa goes on to point out that this is all just an implementation
of Conway's Game of Life. At least, it was, until you notice that something's wrong with the grid
of lights you bought: four lights, one in each corner, are stuck on and can't be turned off.
The example above will actually run like this:

Initial state:
##.#.#
...##.
#....#
..#...
#.#..#
####.#

After 1 step:
#.##.#
####.#
...##.
......
#...#.
#.####

After 2 steps:
#..#.#
#....#
.#.##.
...##.
.#..##
##.###

After 3 steps:
#...##
####.#
..##.#
......
##....
####.#

After 4 steps:
#.####
#....#
...#..
.##...
#.....
#.#..#

After 5 steps:
##.###
.##..#
.##...
.##...
#.#...
##...#
After 5 steps, this example now has 17 lights on.

In your grid of 100x100 lights, given your initial configuration, but with the four corners always in the on state, how many lights are on after 100 steps?
    """

    fun cornerOn(grid: Array<BooleanArray>): Array<BooleanArray> {
        val nextGrid = Array(grid.size) { BooleanArray(grid[it].size) }
        val lastRow = grid.lastIndex
        for (row in 0..lastRow) {
            for (col in grid[row].indices) {
                nextGrid[row][col] = grid[row][col]
            }
        }
        nextGrid[0][0] = true
        nextGrid[0][grid[0].lastIndex] = true
        nextGrid[lastRow][0] = true
        nextGrid[lastRow][grid[lastRow].lastIndex] = true
        return nextGrid
    }
}