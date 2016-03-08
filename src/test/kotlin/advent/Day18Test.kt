package advent


import org.junit.Assert
import org.junit.Test

/**
 * Created by joeyt on 3/7/16.
 */
class Day18Test {
    val example = """
.#.#.#
...##.
#....#
..#...
#.#..#
####..
    """
    @Test
    fun example() {
        val grid = Day18.input2Grid(example.lines().filter { it.isNotBlank() })
        Assert.assertEquals("example is 6x6", 6, grid.size)
        Assert.assertEquals("example is 6x6", 6, grid[0].size)

        Assert.assertEquals("first row has 3 on", 3, grid[0].filter { it }.size)
        Assert.assertEquals("second row has 2 on", 2, grid[1].filter { it }.size)

        Assert.assertEquals("example has 15 on", 15, Day18.countOn(grid))

        val grid1 = Day18.step(grid)
        Assert.assertEquals("example with 1 step has 11 on", 11, Day18.countOn(grid1))
        val grid2 = Day18.step(grid1)
        Assert.assertEquals("example with 2 steps has 8 on", 8, Day18.countOn(grid2))
        val grid3 = Day18.step(grid2)
        Assert.assertEquals("example with 3 steps has 4 on", 4, Day18.countOn(grid3))
        val grid4 = Day18.step(grid3)
        Assert.assertEquals("example with 4 steps has 4 on", 4, Day18.countOn(grid4))
    }

    @Test
    fun testParsing() {
        Assert.assertEquals("puzzle is 100x100", 100, Day18.puzzleGrid.size)
        Assert.assertTrue("puzzle is 100x100", Day18.puzzleGrid.all { it.size == 100 })
    }

    @Test
    fun answer() {
        var grid = Day18.puzzleGrid
        for (step in 1..100) {
            grid = Day18.step(grid)
        }
        Assert.assertEquals("my answer", 821, Day18.countOn(grid))
    }

    @Test
    fun example2() {
        var grid = Day18.input2Grid(example.lines().filter { it.isNotBlank() })
        grid = Day18.cornerOn(grid)
        for (step in 1..5) {
            grid = Day18.step(grid)
            grid = Day18.cornerOn(grid)
        }
        Assert.assertEquals("example has 17 lights on", 17, Day18.countOn(grid))
    }

    @Test
    fun answer2() {
        var grid = Day18.puzzleGrid
        grid = Day18.cornerOn(grid)
        for (step in 1..100) {
            grid = Day18.step(grid)
            grid = Day18.cornerOn(grid)
        }
        Assert.assertEquals("my answer", 886, Day18.countOn(grid))
    }
}