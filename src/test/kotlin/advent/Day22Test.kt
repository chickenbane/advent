package advent

import advent.Day22.Boss
import advent.Day22.GameResult
import advent.Day22.GameState
import advent.Day22.Player
import advent.Day22.Spell
import org.junit.Assert
import org.junit.Test

/**
 * Created by a-jotsai on 4/6/16.
 */
class Day22Test {
    /*

For example, suppose the player has 10 hit points and 250 mana, and that the boss has 13 hit points and 8 damage:

-- Player turn --
- Player has 10 hit points, 0 armor, 250 mana
- Boss has 13 hit points
Player casts Poison.

-- Boss turn --
- Player has 10 hit points, 0 armor, 77 mana
- Boss has 13 hit points
Poison deals 3 damage; its timer is now 5.
Boss attacks for 8 damage.

-- Player turn --
- Player has 2 hit points, 0 armor, 77 mana
- Boss has 10 hit points
Poison deals 3 damage; its timer is now 4.
Player casts Magic Missile, dealing 4 damage.

-- Boss turn --
- Player has 2 hit points, 0 armor, 24 mana
- Boss has 3 hit points
Poison deals 3 damage. This kills the boss, and the player wins.

     */

    @Test
    fun example1() {
        val start = GameState(Player(10, 250), Boss(13, 8), emptySet())
        val turn1 = start.cast(Spell.POISON)
        if (turn1 !is GameResult.OnGoing) throw AssertionError("game not yet over")
        val turn2 = turn1.state.cast(Spell.MISSILE)
        println(turn2)
        Assert.assertEquals("player wins", GameResult.Win, turn2)
    }

    /*

Now, suppose the same initial conditions, except that the boss has 14 hit points instead:

-- Player turn --
- Player has 10 hit points, 0 armor, 250 mana
- Boss has 14 hit points
Player casts Recharge.

-- Boss turn --
- Player has 10 hit points, 0 armor, 21 mana
- Boss has 14 hit points
Recharge provides 101 mana; its timer is now 4.
Boss attacks for 8 damage!

turn 1

-- Player turn --
- Player has 2 hit points, 0 armor, 122 mana
- Boss has 14 hit points
Recharge provides 101 mana; its timer is now 3.
Player casts Shield, increasing armor by 7.

-- Boss turn --
- Player has 2 hit points, 7 armor, 110 mana
- Boss has 14 hit points
Shield's timer is now 5.
Recharge provides 101 mana; its timer is now 2.
Boss attacks for 8 - 7 = 1 damage!

turn 2

-- Player turn --
- Player has 1 hit point, 7 armor, 211 mana
- Boss has 14 hit points
Shield's timer is now 4.
Recharge provides 101 mana; its timer is now 1.
Player casts Drain, dealing 2 damage, and healing 2 hit points.

-- Boss turn --
- Player has 3 hit points, 7 armor, 239 mana
- Boss has 12 hit points
Shield's timer is now 3.
Recharge provides 101 mana; its timer is now 0.
Recharge wears off.
Boss attacks for 8 - 7 = 1 damage!

turn 3

-- Player turn --
- Player has 2 hit points, 7 armor, 340 mana
- Boss has 12 hit points
Shield's timer is now 2.
Player casts Poison.

-- Boss turn --
- Player has 2 hit points, 7 armor, 167 mana
- Boss has 12 hit points
Shield's timer is now 1.
Poison deals 3 damage; its timer is now 5.
Boss attacks for 8 - 7 = 1 damage!

turn 4

-- Player turn --
- Player has 1 hit point, 7 armor, 167 mana
- Boss has 9 hit points
Shield's timer is now 0.
Shield wears off, decreasing armor by 7.
Poison deals 3 damage; its timer is now 4.
Player casts Magic Missile, dealing 4 damage.

-- Boss turn --
- Player has 1 hit point, 0 armor, 114 mana
- Boss has 2 hit points
Poison deals 3 damage. This kills the boss, and the player wins.

turn 5
     */

    @Test
    fun example2() {
        val start = GameState(Player(10, 250), Boss(14, 8), emptySet())
        val turn1 = start.cast(Spell.RECHARGE)
        println(turn1)
        if (turn1 !is GameResult.OnGoing) throw AssertionError("game not yet over")
        val turn2 = turn1.state.cast(Spell.SHIELD)
        println(turn2)
        if (turn2 !is GameResult.OnGoing) throw AssertionError("game not yet over")
        val turn3 = turn2.state.cast(Spell.DRAIN)
        println(turn3)
        if (turn3 !is GameResult.OnGoing) throw AssertionError("game not yet over")
        val turn4 = turn3.state.cast(Spell.POISON)
        println(turn4)
        if (turn4 !is GameResult.OnGoing) throw AssertionError("game not yet over")
        val turn5 = turn4.state.cast(Spell.MISSILE)
        println(turn5)
        Assert.assertEquals("player wins", GameResult.Win, turn5)
    }

    @Test
    fun example2casts2() {
        val spells = listOf(
                Spell.POISON,
                Spell.DRAIN,
                Spell.RECHARGE,
                Spell.POISON,
                Spell.SHIELD,
                Spell.RECHARGE,
                Spell.POISON,
                Spell.MISSILE)
        val result = Day22.cast2(Day22.PuzzleStartState, spells)
        println("allSpells cost = ${spells.map { it.mana }.sum()}")
        Assert.assertEquals("player wins", GameResult.Win, result)
    }

    @Test
    fun answer() {
        Assert.assertEquals("my answer", 900, Day22.answer())
    }

    @Test
    fun answer2() {
        Assert.assertEquals("my answer", 1216, Day22.answer2())
    }

    @Test
    fun wtf() {
        val root = Day22.PuzzleRootNode.children2
        val rootSpells = root.map { it.spell }.filterNotNull()

        Assert.assertTrue("one", Spell.POISON in rootSpells)

        val node1: Day22.Node = root.find { it.spell == Spell.POISON } ?: throw AssertionError("one")
        val node1spells = node1.children2.filterNotNull().map { it.spell }

        Assert.assertTrue("two", Spell.DRAIN in node1spells)

        val node2 = node1.children2.find { it.spell == Spell.DRAIN } ?: throw AssertionError("two")
        val node3 = node2.children2.find { it.spell == Spell.RECHARGE } ?: throw AssertionError("three")

        Day22.answer2why(node3)
        val n3c2 = node3.children2
        println("spells = ${n3c2.map{it.spell}} n3.c2->$n3c2 size=${n3c2.size}")
        //val node4 = node3.children2.find { it.spell == Spell.POISON } ?: throw AssertionError("four")
        //val node5 = node4.children2.find { it.spell == Spell.SHIELD } ?: throw AssertionError("five")

    }
    // 1242 too high

    /*
    Hard Mode
Poison
Drain
Recharge
Poison
Shield
Recharge
Poison
Magic Missile

     */
}