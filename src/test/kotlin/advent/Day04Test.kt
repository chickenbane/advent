package advent

import org.junit.Assert
import org.junit.Test

/**
 * Created by a-jotsai on 1/27/16.
 */
class Day04Test {

    @Test
    fun testExamples() {
//        If your secret key is abcdef, the answer is 609043, because the MD5 hash of abcdef609043 starts with five zeroes (000001dbbfa...), and it is the lowest such number to do so.
//        If your secret key is pqrstuv, the lowest number it combines with to make an MD5 hash starting with five zeroes is 1048970; that is, the MD5 hash of pqrstuv1048970 looks like 000006136ef....
        Assert.assertTrue("abcdef609043", Day04.hashWithKey("abcdef", 609043).startsWith("000001dbbfa"))
        Assert.assertTrue("pqrstuv1048970", Day04.hashWithKey("pqrstuv", 1048970).startsWith("000006136ef"))
    }

    @Test
    fun answer() {
        Assert.assertEquals("my answer", 346386, Day04.mine())
    }

    @Test
    fun answer2() {
        Assert.assertEquals("my answer", 9958218, Day04.mine("000000"))
    }
}