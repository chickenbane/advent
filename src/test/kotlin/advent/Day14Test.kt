package advent


import org.junit.Assert
import org.junit.Test

/**
 * Created by joeyt on 2/6/16.
 */
class Day14Test {
    @Test
    fun checkDemo() {
//        Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
//        Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
//
//        After one second, Comet has gone 14 km, while Dancer has gone 16 km.
//        After ten seconds, Comet has gone 140 km, while Dancer has gone 160 km.
//        On the eleventh second, Comet begins resting (staying at 140 km), and Dancer continues on for a total distance of 176 km.
//        On the 12th second, both reindeer are resting.
//        They continue to rest until the 138th second, when Comet flies for another ten seconds.
//        On the 174th second, Dancer flies for another 11 seconds.
//
//        In this example, after the 1000th second, both reindeer are resting,
//        and Comet is in the lead at 1120 km (poor Dancer has only gotten 1056 km by that point).
        val input = """
Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
        """
        val inputLines = input.lines().map { it.trim() }.filter { it.isNotBlank() }
        val olympics = Day14.Olympics(inputLines)
        val comet = olympics.reindeer.find { it.name == "Comet" } ?: throw AssertionError("no comet")
        val dancer = olympics.reindeer.find { it.name == "Dancer" } ?: throw AssertionError("no dancer")

        Assert.assertEquals("comet @ 1 = 14", 14, comet.dist(1))
        Assert.assertEquals("dancer @ 1 = 16", 16, dancer.dist(1))

        Assert.assertEquals("comet @ 10 = 140", 140, comet.dist(10))
        Assert.assertEquals("dancer @ 10 = 160", 160, dancer.dist(10))

        Assert.assertEquals("comet @ 11 = 140", 140, comet.dist(11))
        Assert.assertEquals("dancer @ 11 = 176", 176, dancer.dist(11))

        Assert.assertEquals("comet @ 1000 = 1120", 1120, comet.dist(1000))
        Assert.assertEquals("dancer @ 1000 = 1056", 1056, dancer.dist(1000))
    }

    @Test
    fun answer() {
        val olympics = Day14.Olympics(Day14.puzzleInput)
        val actual = olympics.reindeer.map { it.dist(2503) }.max()!!
        Assert.assertEquals("my answer", 2660, actual)
    }

    @Test
    fun answer2() {
        val olympics = Day14.Olympics(Day14.puzzleInput)
        val points: Map<Day14.Reindeer, Int> = olympics.points(2503)
        //println("my points: $points")
        val winner: Map.Entry<Day14.Reindeer, Int> = points.maxBy { it.value }!!
        Assert.assertEquals("my answer", 1256, winner.value)
    }

}