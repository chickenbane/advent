package advent

import java.io.File
import java.util.*

/**
 * Created by a-jotsai on 2/10/16.
 */
object Day15 {
    private val part1 = """
--- Day 15: Science for Hungry People ---

Today, you set out on the task of perfecting your milk-dunking cookie recipe.
All you have to do is find the right balance of ingredients.

Your recipe leaves room for exactly 100 teaspoons of ingredients.
You make a list of the remaining ingredients you could use to finish the recipe (your puzzle input) and their properties per teaspoon:

capacity (how well it helps the cookie absorb milk)
durability (how well it keeps the cookie intact when full of milk)
flavor (how tasty it makes the cookie)
texture (how it improves the feel of the cookie)
calories (how many calories it adds to the cookie)
You can only measure ingredients in whole-teaspoon amounts accurately, and you have to be accurate so you can reproduce your results in the future.
 The total score of a cookie can be found by adding up each of the properties (negative totals become 0) and then multiplying together everything except calories.

For instance, suppose you have these two ingredients:

Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
Then, choosing to use 44 teaspoons of butterscotch and 56 teaspoons of cinnamon (because the amounts of each ingredient must add up to 100) would result in a cookie with the following properties:

A capacity of 44*-1 + 56*2 = 68
A durability of 44*-2 + 56*3 = 80
A flavor of 44*6 + 56*-2 = 152
A texture of 44*3 + 56*-1 = 76
Multiplying these together (68 * 80 * 152 * 76, ignoring calories for now) results in a total score of 62842880, which happens to be the best score possible given these ingredients. If any properties had produced a negative total, it would have instead become zero, causing the whole score to multiply to zero.

Given the ingredients in your kitchen and their properties, what is the total score of the highest-scoring cookie you can make?
    """

    data class Ingredient(val name: String,
                          val capacity: Int,
                          val durability: Int,
                          val flavor: Int,
                          val texture: Int,
                          val calories: Int)

    val TotalSpoons = 100

    fun cookieScore(ingredients: List<Ingredient>, spoons: IntArray): Long {
        require (spoons.size == ingredients.size)
        require (spoons.all { it > 0 } && spoons.sum() == TotalSpoons)

        var capacity = 0
        var durability = 0
        var flavor = 0
        var texture = 0

        for ((idx, ing) in ingredients.withIndex()) {
            val ingSpoons = spoons[idx]
            capacity += (ing.capacity * ingSpoons)
            durability += (ing.durability * ingSpoons)
            flavor += (ing.flavor * ingSpoons)
            texture += (ing.texture * ingSpoons)
        }

        val scores = listOf(capacity, durability, flavor, texture)
        val noNegs = scores.map { Math.max(0, it) }

        return noNegs.fold(1L) { acc, n -> acc * n }
    }

    fun parseLine(line: String): Ingredient {
        val tokens = line.split(" ")
        require(tokens.size == 11)
        val name = tokens[0].dropLast(1)
        require(tokens[1] == "capacity" && tokens[3] == "durability" && tokens[5] == "flavor" && tokens[7] == "texture" && tokens[9] == "calories")
        val cap = tokens[2].dropLast(1).toInt()
        val dur = tokens[4].dropLast(1).toInt()
        val fla = tokens[6].dropLast(1).toInt()
        val text = tokens[8].dropLast(1).toInt()
        val cal = tokens[10].toInt()
        return Ingredient(name, cap, dur, fla, text, cal)
    }

    val input: List<String> by lazy { File("src/main/resources/input15.txt").readLines() }

    val puzzleIngredients: List<Ingredient> by lazy { input.map { parseLine(it) } }

    class ComboIterator(val total: Int, val buckets: Int) : Iterator<IntArray> {
        // IntArray "view" returned by next()
        // the state of the array is determined by the LinkedList bars, this IntArray is for the consumer
        val array = IntArray(buckets)

        // the max value of a bucket, which assumes that each bucket must have at least one element
        private val bucketMax = total - buckets + 1
        private val bars = LinkedList<Int>()

        init {
            require (buckets > 1)
        }

        // the "bars" from this class is from https://en.wikipedia.org/wiki/Stars_and_bars_(combinatorics)
        // the bars are between an array of stars and create the buckets to divide the stars
        // for this iterator, the bars start at the front of the array, and as we iterate we pop the last element and increment it
        // or recurse if we've reached the end
        // therefore, the end of iteration occurs when the first element has reached max value
        override fun next(): IntArray {
            updateBars()
            updateArray()
            return array
        }

        // bars.size == buckets - 1
        private fun updateBars() {
            require (hasNext())

            if (bars.isEmpty()) { // first run
                for (b in 1..buckets - 1) {
                    bars.add(b)
                }
            } else {
                var popDepth = 0
                var next: Int

                do {
                    val last = bars.removeLast()
                    popDepth += 1
                    next = last + 1
                } while (next + popDepth > total)

                while (popDepth > 0) {
                    bars.add(next)
                    next += 1
                    popDepth -= 1
                }
            }
        }

        // array.size == bars.size + 1
        private fun updateArray() {
            var sum = 0
            var prev = 0
            for ((i, b) in bars.withIndex()) {
                val stars = b - prev
                array[i] = stars
                prev = b
                sum += stars
            }
            array[array.lastIndex] = total - sum
        }


        override fun hasNext(): Boolean {
            if (bars.isEmpty()) return true
            return bars.first() < bucketMax
        }

    }

    fun answer(): Long {
        var max = 0L
        val spoons = ComboIterator(TotalSpoons, puzzleIngredients.size)
        for (arr in spoons) {
            val score = cookieScore(puzzleIngredients, arr)
            if (score > max) {
                println("new score = $max old score = $score")
                max = score
            }
        }
        return max
    }


}