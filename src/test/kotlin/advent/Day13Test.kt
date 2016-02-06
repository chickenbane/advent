package advent


import org.junit.Assert
import org.junit.Test

/**
 * Created by joeyt on 2/6/16.
 */
class Day13Test {
    @Test
    fun checkDemo() {
        val demo = """
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
        """
        val demoInput = demo.lines().map { it.trim() }.filter { it.isNotBlank() }
        val guests = Day13.DinnerGuests(demoInput)
        val alice: Map<String, Int> = guests.happy["Alice"] ?: throw AssertionError("no alice")
        println("my alice=$alice")
        Assert.assertEquals("Alice-Bob = 54", 54, alice["Bob"] ?: throw AssertionError("no alice-bob"))
        Assert.assertEquals("total change in happiness of 330", 330, Day13.findMaxHappiness(guests))
    }

    @Test
    fun answer() {
        val guests = Day13.DinnerGuests(Day13.puzzleInput)
        Assert.assertEquals("my answer", 618, Day13.findMaxHappiness(guests))
    }

    @Test
    fun answer2() {
        val guests = Day13.DinnerGuestsAndMe(Day13.puzzleInput)
        Assert.assertEquals("my part2 answer", 601, Day13.findMaxHappiness(guests))
    }

    // wow, am I learn from this lesson people are less happy when I'm there?! rude.

    // or, maybe the lesson is: make sure you bring others happiness, not apathy.

}