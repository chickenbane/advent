package advent

import org.junit.Assert
import org.junit.Test

/**
 * Created by a-jotsai on 2/25/16.
 */
class Day15Test {

    /*
    Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
Then, choosing to use 44 teaspoons of butterscotch and 56 teaspoons of cinnamon (because the amounts of each ingredient must add up to 100) would result in a cookie with the following properties:

A capacity of 44*-1 + 56*2 = 68
A durability of 44*-2 + 56*3 = 80
A flavor of 44*6 + 56*-2 = 152
A texture of 44*3 + 56*-1 = 76
Multiplying these together (68 * 80 * 152 * 76, ignoring calories for now) results in a total score of 62842880


For example, given the ingredients above, if you had instead selected 40 teaspoons of butterscotch and 60 teaspoons of cinnamon (which still adds to 100), the total calorie count would be 40*8 + 60*3 = 500. The total score would go down, though: only 57600000, the best you can do in such trying circumstances.
     */

    val butterscotch = Day15.Ingredient("Butterscotch", -1, -2, 6, 3, 8)
    val cinnamon = Day15.Ingredient("Cinnamon", 2, 3, -2, -1, 3)

    @Test
    fun example() {
        val butterscotchStr = "Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8"
        val cinnamonStr = "Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3"

        Assert.assertEquals("butterscotch", butterscotch, Day15.parseLine(butterscotchStr))
        Assert.assertEquals("cinnamon", cinnamon, Day15.parseLine(cinnamonStr))
    }

    @Test
    fun exampleScore() {
        val ingredients = listOf(butterscotch, cinnamon)
        val part1 = intArrayOf(44, 56)
        Assert.assertEquals("cookie score", 62842880L, Day15.cookieScore(ingredients, part1))
        val part2 = intArrayOf(40, 60)
        Assert.assertEquals("cookie score", 57600000L, Day15.cookieScore(ingredients, part2))
        Assert.assertEquals("500 calories", 500, Day15.calories(ingredients, part2))

    }

    @Test
    fun answer() {
        Assert.assertEquals("my answer", 222870L, Day15.answer())
        Assert.assertEquals("part2", 117936L, Day15.answer2())
    }
}