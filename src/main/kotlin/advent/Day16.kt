package advent

import java.io.File

/**
 * Created by joeyt on 2/25/16.
 */
object Day16 {
    private val copyPasta = """
--- Day 16: Aunt Sue ---

Your Aunt Sue has given you a wonderful gift, and you'd like to send her a thank you card. However, there's a small problem: she signed it "From, Aunt Sue".

You have 500 Aunts named "Sue".

So, to avoid sending the card to the wrong person, you need to figure out which Aunt Sue (which you conveniently number 1 to 500, for sanity) gave you the gift.
You open the present and, as luck would have it, good ol' Aunt Sue got you a My First Crime Scene Analysis Machine! Just what you wanted. Or needed, as the case may be.

The My First Crime Scene Analysis Machine (MFCSAM for short) can detect a few specific compounds in a given sample,
 as well as how many distinct kinds of those compounds there are. According to the instructions, these are what the MFCSAM can detect:

children, by human DNA age analysis.
cats. It doesn't differentiate individual breeds.
Several seemingly random breeds of dog: samoyeds, pomeranians, akitas, and vizslas.
goldfish. No other kinds of fish.
trees, all in one group.
cars, presumably by exhaust or gasoline or something.
perfumes, which is handy, since many of your Aunts Sue wear a few kinds.
In fact, many of your Aunts Sue have many of these. You put the wrapping from the gift into the MFCSAM.
 It beeps inquisitively at you a few times and then prints out a message on ticker tape:

children: 3
cats: 7
samoyeds: 2
pomeranians: 3
akitas: 0
vizslas: 0
goldfish: 5
trees: 3
cars: 2
perfumes: 1
You make a list of the things you can remember about each Aunt Sue. Things missing from your list aren't zero - you simply don't remember the value.

What is the number of the Sue that got you the gift?
"""

    // Sue 40: vizslas: 0, cats: 7, akitas: 0

    data class SueFacts(val children: Int? = null,
                        val cats: Int? = null,
                        val samoyeds: Int? = null,
                        val pomeranians: Int? = null,
                        val akitas: Int? = null,
                        val vizslas: Int? = null,
                        val goldfish: Int? = null,
                        val trees: Int? = null,
                        val cars: Int? = null,
                        val perfumes: Int? = null) {

        fun with(key: String, value: Int): SueFacts {
            return when(key) {
                "children" -> copy(children=value)
                "cats" -> copy(cats=value)
                "samoyeds" -> copy(samoyeds=value)
                "pomeranians" -> copy(pomeranians=value)
                "akitas" -> copy(akitas=value)
                "vizslas" -> copy(vizslas=value)
                "goldfish" -> copy(goldfish=value)
                "trees" -> copy(trees=value)
                "cars" -> copy(cars=value)
                "perfumes" -> copy(perfumes=value)
                else -> throw IllegalArgumentException("unknown key=$key value=$value")
            }
        }
    }

    val TargetSue = SueFacts(
            children = 3,
            cats = 7,
            samoyeds = 2,
            pomeranians = 3,
            akitas = 0,
            vizslas = 0,
            goldfish = 5,
            trees = 3,
            cars = 2,
            perfumes = 1)


    val NoFacts = SueFacts()


    val puzzleInput by lazy { File("src/main/resources/input16.txt").readLines().filter { it.isNotBlank() } }

    val rememberSues by lazy { puzzleInput.map { parseLine(it) } }

    // Sue 1: goldfish: 9, cars: 0, samoyeds: 9
    // assume all inputs have 3 values
    private fun parseLine(input: String): SueFacts {
        // ugh, add an extra char to the end so it's consistent and i can drop the last char
        val tokens = (input + ".").split(" ")
        require (tokens.size == 8)
        var sue = NoFacts
        for (i in 2..6 step 2) {
            //println("i = $i tokens[i]=${tokens[i]} tokens[i+1]=${tokens[i+1]}")
            val key = tokens[i].dropLast(1)
            val value = tokens[i+1].dropLast(1).toInt()
            sue = sue.with(key, value)
        }
        return sue
    }

    fun matches(other: SueFacts): Boolean {
        if (other.children != null && TargetSue.children != other.children) return false
        if (other.cats != null     && TargetSue.cats != other.cats) return false
        if (other.samoyeds != null && TargetSue.samoyeds != other.samoyeds) return false
        if (other.pomeranians != null && TargetSue.pomeranians != other.pomeranians) return false
        if (other.akitas != null   && TargetSue.akitas != other.akitas) return false
        if (other.vizslas != null  && TargetSue.vizslas != other.vizslas) return false
        if (other.goldfish != null && TargetSue.goldfish != other.goldfish) return false
        if (other.trees != null    && TargetSue.trees != other.trees) return false
        if (other.cars != null     && TargetSue.cars != other.cars) return false
        if (other.perfumes != null && TargetSue.perfumes != other.perfumes) return false
        return true
    }

    fun answer(): Int {
        val filtered = rememberSues.filter { matches(it) }
        require (filtered.size == 1)
        val found = filtered.first()
        return 1 + rememberSues.indexOf(found)
    }

    private val part2 = """
    --- Part Two ---

As you're about to send the thank you note, something in the MFCSAM's instructions catches your eye.
Apparently, it has an outdated retroencabulator, and so the output from the machine isn't exact values - some of them indicate ranges.

In particular, the cats and trees readings indicates that there are greater than that many
(due to the unpredictable nuclear decay of cat dander and tree pollen),
while the pomeranians and goldfish readings indicate that there are fewer than that many
(due to the modial interaction of magnetoreluctance).

What is the number of the real Aunt Sue?
    """

    fun matches2(other: SueFacts): Boolean {
        if (other.children != null && TargetSue.children != other.children) return false
        if (other.cats != null     && TargetSue.cats!! >= other.cats) return false
        if (other.samoyeds != null && TargetSue.samoyeds != other.samoyeds) return false
        if (other.pomeranians != null && TargetSue.pomeranians!! <= other.pomeranians) return false
        if (other.akitas != null   && TargetSue.akitas != other.akitas) return false
        if (other.vizslas != null  && TargetSue.vizslas != other.vizslas) return false
        if (other.goldfish != null && TargetSue.goldfish!! <= other.goldfish) return false
        if (other.trees != null    && TargetSue.trees!! >= other.trees) return false
        if (other.cars != null     && TargetSue.cars != other.cars) return false
        if (other.perfumes != null && TargetSue.perfumes != other.perfumes) return false
        return true
    }

    fun answer2(): Int {
        val filtered = rememberSues.filter { matches2(it) }
        require (filtered.size == 1) { "expecting only one, got = ${filtered}"}
        val found = filtered.first()
        return 1 + rememberSues.indexOf(found)
    }
}