package advent


import org.junit.Assert
import org.junit.Test

/**
 * Created by joeyt on 2/26/16.
 */
class Day16Test {
    //@Test
    fun checkDemo() {
        Assert.assertEquals("actual should be expected", "expected", "actual")
    }

    @Test
    fun answer() {
        Assert.assertEquals("my answer", 40, Day16.answer())
        Assert.assertEquals("my answer", 241, Day16.answer2())
    }

}