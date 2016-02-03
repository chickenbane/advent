package advent

import org.junit.Assert
import org.junit.Test

/**
 * Created by a-jotsai on 2/3/16.
 */
class Day10Test {
    @Test
    fun example() {
//        1 becomes 11 (1 copy of digit 1).
//        11 becomes 21 (2 copies of digit 1).
//        21 becomes 1211 (one 2 followed by one 1).
//        1211 becomes 111221 (one 1, one 2, and two 1s).
//        111221 becomes 312211 (three 1s, two 2s, and one 1).
        Assert.assertEquals("1 -> 11", "11", Day10.nextNum("1"))
        Assert.assertEquals("11 -> 21", "21", Day10.nextNum("11"))
        Assert.assertEquals("21 -> 1211", "1211", Day10.nextNum("21"))
        Assert.assertEquals("1211 -> 111221", "111221", Day10.nextNum("1211"))
        Assert.assertEquals("111221 -> 312211", "312211", Day10.nextNum("111221"))
    }

    @Test
    fun answer() {
        // 1321131112
        var next = "1321131112"
        var count = 0
        while (count < 40) {
            count += 1
            //println("count=$count next=$next")
            next = Day10.nextNum(next)
        }
        Assert.assertEquals("my answer", 492982, next.length)
    }

    @Test
    fun answer2() {
        var next = "1321131112"
        for (i in 1..50) {
            next = Day10.nextNum(next)
        }
        Assert.assertEquals("my answer", 6989950, next.length)
    }
}