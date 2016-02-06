package advent

import org.junit.Assert
import org.junit.Test

/**
 * Created by a-jotsai on 2/5/16.
 */
class Day12Test {
    val json = Day12.parseElement(Day12.input)

    @Test
    fun answer() {
        val numbers = Day12.findNumbers(Day12.input)
        Assert.assertEquals("my answer", 119433, numbers.sum())

        Assert.assertEquals("my parsed answer", 119433, Day12.part1(json))

        Assert.assertEquals("my part2 answer", 68466, Day12.part2(json))
    }

    //@Test
    fun example() {
        val examples = arrayListOf(
                "[1,2,3]",
                """{"a":2,"b":4}""",
                """[[[3]]]""",
                """{"a":{"b":4},"c":-1}""",
                """{"a":[-1,1]}""",
                """[-1,{"a":1}]""",
                "[]",
                "{}"
        )
//        [1,2,3] and {"a":2,"b":4} both have a sum of 6.
//        [[[3]]] and {"a":{"b":4},"c":-1} both have a sum of 3.
//        {"a":[-1,1]} and [-1,{"a":1}] both have a sum of 0.
//        [] and {} both have a sum of 0.
        println(Day12.elementToString(Day12.parseElement("[1,2,3]")))
        println(Day12.elementToString(Day12.parseElement("""{"a":2,"b":4}""")))
        println(Day12.elementToString(Day12.parseElement("""[[[3]]]""")))
        println(Day12.elementToString(Day12.parseElement("""{"a":{"b":4},"c":-1}""")))
        println(Day12.elementToString(Day12.parseElement("""{"a":[-1,1]}""")))
        println(Day12.elementToString(Day12.parseElement("""[-1,{"a":1}]""")))
        println(Day12.elementToString(Day12.parseElement("""[]""")))
        println(Day12.elementToString(Day12.parseElement("""{}""")))

        // this task is annoying mainly because there are many places for off-by-one errors.  and I made a few of these errors!

        println(Day12.elementToString(Day12.parseElement("""{"c":65,"a":"orange","b":"green","d":"orange"}""")))

        println(Day12.elementToString(Day12.parseElement(Day12.input)))
    }
}