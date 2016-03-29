package advent

import org.junit.Assert
import org.junit.Test

/**
 * Created by a-jotsai on 3/28/16.
 */
class Day21Test {
    @Test
    fun example() {
        /*
        For example, suppose you have 8 hit points, 5 damage, and 5 armor, and that the boss has 12 hit points, 7 damage, and 2 armor:

The player deals 5-2 = 3 damage; the boss goes down to 9 hit points.
The boss deals 7-5 = 2 damage; the player goes down to 6 hit points.
The player deals 5-2 = 3 damage; the boss goes down to 6 hit points.
The boss deals 7-5 = 2 damage; the player goes down to 4 hit points.
The player deals 5-2 = 3 damage; the boss goes down to 3 hit points.
The boss deals 7-5 = 2 damage; the player goes down to 2 hit points.
The player deals 5-2 = 3 damage; the boss goes down to 0 hit points.
In this scenario, the player wins! (Barely.)
         */
        val player = Day21.Actor(8, 5, 5)
        val boss = Day21.Actor(12, 7, 2)
        Assert.assertTrue("player wins", Day21.doesPlayerWin(player, boss))
    }

    @Test
    fun answer() {
        Assert.assertEquals("my answer", 111, Day21.answer())
    }

    @Test
    fun answer2() {
        Assert.assertEquals("my answer", 188, Day21.answer2())
    }
}