package advent

import java.io.File
import java.util.*

/**
 * Created by joeyt on 2/5/16.
 */
object Day13 {
    private val copyPasta = """
--- Day 13: Knights of the Dinner Table ---

In years past, the holiday feast with your family hasn't gone so well. Not everyone gets along! This year, you resolve, will be different. You're going to find the optimal seating arrangement and avoid all those awkward conversations.

You start by writing up a list of everyone invited and the amount their happiness would increase or decrease if they were to find themselves sitting next to each other person. You have a circular table that will be just big enough to fit everyone comfortably, and so each person will have exactly two neighbors.

For example, suppose you have only four attendees planned, and you calculate their potential happiness as follows:

Alice would gain 54 happiness units by sitting next to Bob.
Alice would lose 79 happiness units by sitting next to Carol.
Alice would lose 2 happiness units by sitting next to David.
Bob would gain 83 happiness units by sitting next to Alice.
Bob would lose 7 happiness units by sitting next to Carol.
Bob would lose 63 happiness units by sitting next to David.
Carol would lose 62 happiness units by sitting next to Alice.
Carol would gain 60 happiness units by sitting next to Bob.
Carol would gain 55 happiness units by sitting next to David.
David would gain 46 happiness units by sitting next to Alice.
David would lose 7 happiness units by sitting next to Bob.
David would gain 41 happiness units by sitting next to Carol.
Then, if you seat Alice next to David, Alice would lose 2 happiness units (because David talks so much), but David would gain 46 happiness units (because Alice is such a good listener), for a total change of 44.

If you continue around the table, you could then seat Bob next to Alice (Bob gains 83, Alice gains 54). Finally, seat Carol, who sits next to Bob (Carol gains 60, Bob loses 7) and David (Carol gains 55, David gains 41). The arrangement looks like this:

     +41 +46
+55   David    -2
Carol       Alice
+60    Bob    +54
     -7  +83
After trying every other seating arrangement in this hypothetical scenario, you find that this one is the most optimal, with a total change in happiness of 330.

What is the total change in happiness for the optimal seating arrangement of the actual guest list?
"""
    private val initialThoughts = """
    another permutation question.  whee!  reference day09.
    my guess for part2: what is the least happy seating arrangement

    1: consume input
    2: find set of elements (guests)
    3: go through permutations of guests, finding max

    here's what i learned from this approach, easy to get indexes confused!
"""

    val puzzleInput: List<String> by lazy { File("src/main/resources/input13.txt").readLines() }

    open class DinnerGuests(val input: List<String>) {
        data class GuestDelta(val guest: String, val target: String, val happiness: Int)

        val happy: Map<String, Map<String, Int>> by lazy { Collections.unmodifiableMap(calcHappy()) }

        open fun calcHappy(): HashMap<String, HashMap<String, Int>> {
            val map = HashMap<String, HashMap<String, Int>>()
            for (inStr in input) {
                val d: GuestDelta = parseDelta(inStr)
                map.getOrPut(d.guest) { HashMap<String, Int>() }[d.target] = d.happiness
            }
            return map
        }

        private val lose = "lose"
        private val gain = "gain"

        private fun parseDelta(inStr: String): GuestDelta {
            val tokens = inStr.split(" ")
            require(tokens.size == 11)
            require(tokens[1] == "would")
            val loseOrGain = tokens[2]
            require(loseOrGain == lose || loseOrGain == gain)
            val amt = tokens[3].toInt()
            require(tokens[4] == "happiness" && tokens[7] == "sitting" && tokens[9] == "to")
            val happiness = if (loseOrGain == gain) amt else (amt * -1)
            // lol sentence ends with a period
            val target = tokens[10].substringBefore(".")
            return GuestDelta(tokens[0], target, happiness)
        }

        val names: Array<String> by lazy { happy.keys.toTypedArray() }
    }

    fun findMaxHappiness(guests: DinnerGuests): Int {
        val seatings = IntArray(guests.names.size) { it }  // [0, 1, 2, ...n]
        val lastSeatIdx = seatings.lastIndex
        var max: Int? = null
        do {
            var amt = 0
            //println("seating = ${seatings.map { guests.names[it] }.joinToString()}")
            seatings.forEachIndexed { idx, seat ->
                val guestName = guests.names[seat]
                val leftIdx = if (idx == 0) seatings[lastSeatIdx] else seatings[idx - 1]
                val left = guests.names[leftIdx]
                val rightIdx = if (idx == lastSeatIdx) seatings[0] else seatings[idx + 1]
                val right = guests.names[rightIdx]
                val guest = guests.happy[guestName] ?: throw IllegalArgumentException("can't find guest = $guestName")
                val leftHappy = guest[left] ?: throw IllegalArgumentException("can't find $guestName left = $left")
                val rightHappy = guest[right] ?: throw IllegalArgumentException("can't find $guestName right = $right")
                //println("guest=$guestName left=$left ($leftHappy) right=$right ($rightHappy)")
                amt += leftHappy + rightHappy
            }
            if (max == null || amt > max) {
                println("prev max = $max, amt = $amt")
                max = amt
            } else {
                //println("max $max > amt = $amt")
            }
            val again = Day09.nextLexPermutation(seatings)
        } while (again)
        if (max == null) throw IllegalStateException("unset max?!")
        return max
    }


    private val part2 = """
--- Part Two ---

In all the commotion, you realize that you forgot to seat yourself.
At this point, you're pretty apathetic toward the whole thing, and your happiness wouldn't really
go up or down regardless of who you sit next to. You assume everyone else would be just as ambivalent
about sitting next to you, too.

So, add yourself to the list, and give all happiness relationships that involve you a score of 0.

What is the total change in happiness for the optimal seating arrangement that actually includes yourself?
    """

    private val huh = """
    I could either write a new findMaxHappiness() function or just add myself to the dataset, or subclass DinnerGuests
    """

    class DinnerGuestsAndMe(input: List<String>) : DinnerGuests(input) {
        private val myname = "joey"
        override fun calcHappy(): HashMap<String, HashMap<String, Int>> {
            val map = super.calcHappy()
            val people: Array<String> = map.keys.toTypedArray()
            val me = HashMap<String, Int>()
            for (p in people) {
                me[p] = 0
                val person: HashMap<String, Int> = map[p] ?: throw IllegalStateException("can't find $p")
                person[myname] = 0
            }
            map[myname] = me
            return map
        }
    }

}