package advent

import java.util.*

/**
 * Created by a-jotsai on 3/29/16.
 */
object Day22 {
    private val part1 = """
    --- Day 22: Wizard Simulator 20XX ---

Little Henry Case decides that defeating bosses with swords and stuff is boring. Now he's playing the game with a wizard. Of course, he gets stuck on another boss and needs your help again.

In this version, combat still proceeds with the player and the boss taking alternating turns. The player still goes first. Now, however, you don't get any equipment; instead, you must choose one of your spells to cast. The first character at or below 0 hit points loses.

Since you're a wizard, you don't get to wear armor, and you can't attack normally. However, since you do magic damage, your opponent's armor is ignored, and so the boss effectively has zero armor as well. As before, if armor (from a spell, in this case) would reduce damage below 1, it becomes 1 instead - that is, the boss' attacks always deal at least 1 damage.

On each of your turns, you must select one of your spells to cast. If you cannot afford to cast any spell, you lose. Spells cost mana; you start with 500 mana, but have no maximum limit. You must have enough mana to cast a spell, and its cost is immediately deducted when you cast it. Your spells are Magic Missile, Drain, Shield, Poison, and Recharge.

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
You start with 50 hit points and 500 mana points. The boss's actual stats are in your puzzle input. What is the least amount of mana you can spend and still win the fight? (Do not include mana recharge effects as "spending" negative mana.)


Hit Points: 51
Damage: 9
    """


    sealed class GameResult {
        object Win : GameResult()
        object Lose : GameResult()
        class OnGoing(val state: GameState) : GameResult()
    }
    data class Player(val hitPoints: Int, val mana: Int, val armor: Int = 0) {
        override fun toString() = "- Player has $hitPoints hit points, $armor armor, $mana mana"
    }
    data class Boss(val hitPoints: Int, val damage: Int) {
        override fun toString() = "- Boss has $hitPoints hit points"
        fun next(newHp: Int): Boss = copy(hitPoints = newHp)
    }
    data class GameState(val player: Player, val boss: Boss, val effects: Set<CastSpell>) {
        fun nextSpells(): Set<Spell> = effects.map { it.next() }.filterNotNull().map { it.spell }.toSet()

        fun nextTurns(): Map<Spell, GameResult> {
            val nextSpells = nextSpells()
            val nextTurns = LinkedHashMap<Spell, GameResult>()
            if (nextSpells.isEmpty()) {
                Spell.values().forEach { nextTurns.put(it, GameResult.Lose) }
            } else {
                nextSpells.forEach {
                    val castSpell = it.cast()
                    nextTurns.put(it, playerSpell(castSpell))
                }
            }
            return nextTurns
        }

        fun effects(): GameResult {
            // TODO print effects
            return GameResult.Win
        }

        fun playerSpell(castSpell: CastSpell): GameResult {
            val nextMana = player.mana - castSpell.spell.mana
            require(nextMana >= 0)
            val nextEffectsList = effects.map { it.next() }.filterNotNull() + castSpell
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
            val hp = player.hitPoints - bossDamage()
            if (hp <= 0) return GameResult.Lose
            val armor = 0 // TODO armor
            val nextPlayer = Player(hp, player.mana, armor)
            // TODO effects
            val effects = emptySet<CastSpell>()
            return GameResult.OnGoing(GameState(nextPlayer, boss, effects))
        }

        fun bossDamage(): Int = Math.max(1, boss.damage - player.armor)

        fun cast(spell: Spell): GameResult {
            println("-- Player turn --")
            println(player)
            println(boss)

            val playerEffectsResult = effects()
            if (playerEffectsResult !is GameResult.OnGoing) return playerEffectsResult

            println("Player casts $spell.")

            val cast = spell.cast()
            val playerCastResult = playerEffectsResult.state.playerSpell(cast)
            if (playerCastResult !is GameResult.OnGoing) return playerCastResult

            println("-- Boss turn --")
            println(playerCastResult.state.player)
            println(playerCastResult.state.boss)

            val bossEffectsResult = playerCastResult.state.effects()
            if (bossEffectsResult !is GameResult.OnGoing) return bossEffectsResult

            println("Boss attacks for ${bossDamage()} damage.")

            return bossEffectsResult.state.bossTurn()
        }
    }

    val root = GameState(Player(50, 500), Boss(51, 9), emptySet())

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

    class Node(state: GameResult) {
        val children: MutableSet<Node> = HashSet()
    }
}