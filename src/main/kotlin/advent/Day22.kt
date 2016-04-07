package advent

import java.util.*

/**
 * Created by a-jotsai on 3/29/16.
 */
object Day22 {
    private val part1 = """
    --- Day 22: Wizard Simulator 20XX ---

Little Henry Case decides that defeating bosses with swords and stuff is boring.
Now he's playing the game with a wizard. Of course, he gets stuck on another boss and needs your help again.

In this version, combat still proceeds with the player and the boss taking alternating turns.
The player still goes first. Now, however, you don't get any equipment;
instead, you must choose one of your spells to cast. The first character at or below 0 hit points loses.

Since you're a wizard, you don't get to wear armor, and you can't attack normally.
However, since you do magic damage, your opponent's armor is ignored, and so the boss effectively has zero armor as well.
As before, if armor (from a spell, in this case) would reduce damage below 1, it becomes 1 instead -
that is, the boss' attacks always deal at least 1 damage.

On each of your turns, you must select one of your spells to cast. If you cannot afford to cast any spell, you lose.
Spells cost mana; you start with 500 mana, but have no maximum limit. You must have enough mana to cast a spell,
and its cost is immediately deducted when you cast it. Your spells are Magic Missile, Drain, Shield, Poison, and Recharge.

Magic Missile costs 53 mana. It instantly does 4 damage.
Drain costs 73 mana. It instantly does 2 damage and heals you for 2 hit points.
Shield costs 113 mana. It starts an effect that lasts for 6 turns. While it is active, your armor is increased by 7.
Poison costs 173 mana. It starts an effect that lasts for 6 turns. At the start of each turn while it is active, it deals the boss 3 damage.
Recharge costs 229 mana. It starts an effect that lasts for 5 turns. At the start of each turn while it is active, it gives you 101 new mana.
Effects all work the same way. Effects apply at the start of both the player's turns and the boss' turns. Effects are created with a timer (the number of turns they last); at the start of each turn, after they apply any effect they have, their timer is decreased by one. If this decreases the timer to zero, the effect ends. You cannot cast a spell that would start an effect which is already active. However, effects can be started on the same turn they end.

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
You start with 50 hit points and 500 mana points. The boss's actual stats are in your puzzle input.

What is the least amount of mana you can spend and still win the fight? (Do not include mana recharge effects as "spending" negative mana.)

Hit Points: 51
Damage: 9
    """

    sealed class GameResult {
        object Win : GameResult() {
            override fun toString(): String = "Player wins"
        }
        object Lose : GameResult() {
            override fun toString(): String = "Player loses"
        }
        class OnGoing(val state: GameState) : GameResult() {
            override fun toString(): String = "OnGoing: ${state.player}\n${state.boss}\n${state.effects}"
        }
    }
    data class Player(val hitPoints: Int, val mana: Int, val armor: Int = 0) {
        override fun toString() = "- Player has $hitPoints hit points, $armor armor, $mana mana"
    }
    data class Boss(val hitPoints: Int, val damage: Int) {
        override fun toString() = "- Boss has $hitPoints hit points"
    }
    data class GameState(val player: Player, val boss: Boss, val effects: Set<CastSpell>) {
        fun spellEffects(): GameResult {
            val spells = effects.filter { it.turnsLeft > 0 }.map { it.spell }
            val mana = player.mana + if (Spell.RECHARGE in spells) 101 else 0
            val armor = if (Spell.SHIELD in spells) 7 else 0
            val bossHp = boss.hitPoints - if (Spell.POISON in spells) 3 else 0
            //if (spells.isNotEmpty()) println("spells in effect: $effects effects: mana=$mana armor=$armor bossHp=$bossHp")
            if (bossHp <= 0) return GameResult.Win
            val nextEffects = effects.map { it.next() }.filterNotNull().toSet()
            return GameResult.OnGoing(GameState(player.copy(mana = mana, armor = armor), boss.copy(hitPoints = bossHp), nextEffects))
        }

        fun playerSpell(castSpell: CastSpell): GameResult {
            require(castSpell.spell in castableSpells())
            val nextMana = player.mana - castSpell.spell.mana
            require(nextMana >= 0)
            val nextEffectsList = effects + castSpell
            val nextEffects = nextEffectsList.toSet()
            when(castSpell.spell) {
                Spell.MISSILE -> {
                    val bossHp = boss.hitPoints - 4
                    if (bossHp <= 0) return GameResult.Win
                    return GameResult.OnGoing(GameState(player.copy(mana = nextMana), boss.copy(hitPoints = bossHp), nextEffects))
                }
                Spell.DRAIN -> {
                    val bossHp = boss.hitPoints - 2
                    if (bossHp <= 0) return GameResult.Win
                    val hp = player.hitPoints + 2
                    return GameResult.OnGoing(GameState(player.copy(hitPoints = hp, mana = nextMana), boss.copy(hitPoints = bossHp), nextEffects))
                }
                else -> return GameResult.OnGoing(GameState(player.copy(mana = nextMana), boss, nextEffects))
            }
        }

        fun bossTurn(): GameResult {
            val bossDamage = Math.max(1, boss.damage - player.armor)
            //println("Boss attacks for $bossDamage damage.")
            val hp = player.hitPoints - bossDamage
            if (hp <= 0) return GameResult.Lose
            return GameResult.OnGoing(GameState(player.copy(hitPoints = hp), boss, effects))
        }

        fun part2(): GameResult {
            val hp = player.hitPoints - 1
            if (hp <= 0) return GameResult.Lose
            return GameResult.OnGoing(GameState(player.copy(hitPoints = hp), boss, effects))
        }

        fun cast(spell: Spell, print: Boolean = false): GameResult {
            if (print) {
                println("-- Player turn --")
                println(player)
                println(boss)
            }

            // part 2
            val part2result = part2()
            if (part2result !is GameResult.OnGoing) return part2result


            val playerEffectsResult = part2result.state.spellEffects()
            if (playerEffectsResult !is GameResult.OnGoing) return playerEffectsResult

            if (print) println("Player casts $spell.")

            val cast = spell.cast()
            val playerCastResult = playerEffectsResult.state.playerSpell(cast)
            if (playerCastResult !is GameResult.OnGoing) return playerCastResult

            if (print) {
                println("Player turn complete, effects: ${playerCastResult.state.effects}")
                println("-- Boss turn --")
                println(playerCastResult.state.player)
                println(playerCastResult.state.boss)
            }

            val bossEffectsResult = playerCastResult.state.spellEffects()
            if (bossEffectsResult !is GameResult.OnGoing) return bossEffectsResult

            val bossTurn = bossEffectsResult.state.bossTurn()

            if (print) {
                println("Boss turn complete, effects: ${bossEffectsResult.state.effects}")
                println()
            }
            return bossTurn
        }

        fun castableSpells(): Set<Spell> {
            val currentEffects = effects.filter { it.turnsLeft > 0 }.map { it.spell }.toSet()
            return Spell.values().filter { it !in currentEffects && it.mana <= player.mana }.toSet()
        }

        fun nextTurns(): Map<Spell, GameResult> {
            val nextSpells = castableSpells()
            val nextTurns = LinkedHashMap<Spell, GameResult>()
            if (nextSpells.isEmpty()) {
                Spell.values().forEach { nextTurns.put(it, GameResult.Lose) }
            } else {
                nextSpells.forEach {
                    nextTurns.put(it, cast(it))
                }
            }
            return nextTurns
        }
    }

    data class CastSpell(val spell: Spell, val turnsLeft: Int) {
        fun next(): CastSpell? {
            if (turnsLeft == 0) return null
            return CastSpell(spell, turnsLeft - 1)
        }
    }

    enum class Spell(val mana: Int, val turns: Int) {
        MISSILE(53, 0),  // bossHp - 4
        DRAIN(73, 0),  // bossHp - 2 , playerHp + 2
        SHIELD(113, 6), // playerArmor + 7
        POISON(173, 6),  // bossHp - 3
        RECHARGE(229, 5);  // mana + 101

        fun cast(): CastSpell {
            return CastSpell(this, turns)
        }
    }

    data class Node(val result: GameResult, val manaSpent: Int) {
        val win: Boolean = result is GameResult.Win
        val notLoss: Boolean = result !is GameResult.Lose
        val next: Map<Spell, GameResult> by lazy {
            if (result is GameResult.OnGoing) {
                result.state.nextTurns()
            } else {
                emptyMap()
            }
        }
        val children: Set<Node> by lazy {
            LinkedHashSet<Node>().apply {
                for ((s, r) in next) {
                    add(Node(r, manaSpent + s.mana))
                }
            }
        }
    }

    val root = GameState(Player(50, 500), Boss(51, 9), emptySet())

    fun answerManaSpent(): Int {
        val rootNode = Node(GameResult.OnGoing(root), 0)
        val queue = LinkedList<Node>()
        queue.add(rootNode)
        while (queue.isNotEmpty()) {
            val node = queue.minBy { it.manaSpent }!!
            val removed = queue.remove(node)
            require(removed)
            if (node.win) {
                return node.manaSpent
            } else {
                queue.addAll(node.children.filter { it.notLoss })
            }
        }
        throw IllegalStateException("no wins found?")
    }

    private val part2 = """
    --- Part Two ---

On the next run through the game, you increase the difficulty to hard.

At the start of each player turn (before any other effects apply), you lose 1 hit point. If this brings you to or below 0 hit points, you lose.

With the same starting stats for you and the boss, what is the least amount of mana you can spend and still win the fight?
    """

}