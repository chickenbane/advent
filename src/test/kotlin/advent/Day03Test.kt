package advent

import org.junit.Assert
import org.junit.Test

/**
 * Created by a-jotsai on 1/27/16.
 */
class Day03Test {
    @Test
    fun testExamples() {
//        > delivers presents to 2 houses: one at the starting location, and one to the east.
//        ^>v< delivers presents to 4 houses in a square, including twice to the house at his starting/ending location.
//        ^v^v^v^v^v delivers a bunch of presents to some very lucky children at only 2 houses.
        Assert.assertEquals("> = 2 houses", 2, Day03.numberHouses(">"))
        Assert.assertEquals("^>v< = 4 houses", 4, Day03.numberHouses("^>v<"))
        Assert.assertEquals("^v^v^v^v^v = 2 houses", 2, Day03.numberHouses("^v^v^v^v^v"))
    }

    @Test
    fun answer1() {
        Assert.assertEquals("my answer", 2565, Day03.part1Answer())
    }

    @Test
    fun test2Examples() {
//        ^v delivers presents to 3 houses, because Santa goes north, and then Robo-Santa goes south.
//        ^>v< now delivers presents to 3 houses, and Santa and Robo-Santa end up back where they started.
//        ^v^v^v^v^v now delivers presents to 11 houses, with Santa going one direction and Robo-Santa going the other.
        Assert.assertEquals("^v = 3 houses", 3, Day03.housesWithRobot("^v"))
        Assert.assertEquals("^>v< = 3 houses", 3, Day03.housesWithRobot("^>v<"))
        Assert.assertEquals("^v^v^v^v^v = 11 houses", 11, Day03.housesWithRobot("^v^v^v^v^v"))
    }

    @Test
    fun answer2() {
        Assert.assertEquals("my answer", 2639, Day03.part2Answer())
    }
}