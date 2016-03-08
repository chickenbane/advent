package advent


import org.junit.Assert
import org.junit.Test

/**
 * Created by joeyt on 3/1/16.
 */
class Day17Test {
    @Test
    fun example() {
        val ex = listOf(20, 15, 10, 5, 5).map { it.toString() }
        val bins = Day17.input2Bins(ex)
        Assert.assertEquals("example had 4 solutions", 4, Day17.findNumCombos(bins, 25))
    }

    @Test
    fun answer() {
        Assert.assertEquals("my answer", 4372, Day17.answer())
    }

}