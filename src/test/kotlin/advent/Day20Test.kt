package advent

import org.junit.Assert
import org.junit.Test

/**
 * Created by a-jotsai on 3/28/16.
 */
class Day20Test {
    @Test
    fun example() {
        /*
            House 1 got 10 presents.
            House 2 got 30 presents.
            House 3 got 40 presents.
            House 4 got 70 presents.
            House 5 got 60 presents.
            House 6 got 120 presents.
            House 7 got 80 presents.
            House 8 got 150 presents.
            House 9 got 130 presents.
         */
        Assert.assertEquals("house 1", 10, Day20.numPresents(1))
        Assert.assertEquals("house 2", 30, Day20.numPresents(2))
        Assert.assertEquals("house 3", 40, Day20.numPresents(3))
        Assert.assertEquals("house 4", 70, Day20.numPresents(4))
        Assert.assertEquals("house 5", 60, Day20.numPresents(5))
        Assert.assertEquals("house 6", 120, Day20.numPresents(6))
        Assert.assertEquals("house 7", 80, Day20.numPresents(7))
        Assert.assertEquals("house 8", 150, Day20.numPresents(8))
        Assert.assertEquals("house 9", 130, Day20.numPresents(9))
    }

    // this answer took ~18m
    //@Test
    fun answer() {
        Assert.assertEquals("my answer", 786240, Day20.answer())
    }

    @Test
    fun answer2() {
        Assert.assertEquals("my answer", 831600, Day20.answer2())
    }

}