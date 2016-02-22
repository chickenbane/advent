package advent

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
                          val calories: Int,
                          val spoons: Int = 1) {
        fun teaspoons(spoons: Int): Ingredient = Ingredient(
                name = name,
                capacity = spoons * capacity,
                durability = spoons * durability,
                flavor = spoons * flavor,
                texture = spoons * texture,
                calories = calories,
                spoons = spoons)
    }

    class ComboIterator(val total: Int, val buckets: Int) : Iterator<IntArray> {
        val array = IntArray(buckets)

        init {
            require (buckets > 1)
        }

        override fun next(): IntArray {
            // do stuff
            return array
        }

        override fun hasNext(): Boolean {
            throw UnsupportedOperationException()
        }

    }

    fun cookieScore(ingredients: List<Ingredient>): Long {

        val capacity = Math.max(0, ingredients.fold(0L) { acc, n -> acc + n.capacity })
        val durability = Math.max(0, ingredients.fold(0L) { acc, n -> acc + n.durability })
        val flavor = Math.max(0, ingredients.fold(0L) { acc, n -> acc + n.flavor })
        val texture = Math.max(0, ingredients.fold(0L) { acc, n -> acc + n.texture })
        return capacity * durability * flavor * texture
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

    fun distribute(total: Int, buckets: Int): Set<Set<Int>> {
        val allSets = HashSet<Set<Int>>()
        for (b in 1..buckets) {
            //for (a in )
        }
        return allSets
    }

    fun distribute100(ingredients: List<Ingredient>, prev: Map<Ingredient, Int> = emptyMap()): Map<Ingredient, Int> {
        if (ingredients.isEmpty()) {
            return prev
        }
        require(ingredients.all { it !in prev })
        val remaining = 100 - prev.values.sum()
        require(remaining > 0)
        var max = 0
        var maxMap = emptyMap<Ingredient, Int>()
        for (i in ingredients) {
            for (amt in (0..remaining)) {

            }
        }

        return prev
    }


}