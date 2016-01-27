package advent

import org.junit.Assert
import org.junit.Test

/**
 * Created by a-jotsai on 1/26/16.
 */
class Day02Test {

    @Test
    fun testReadInput() {
        Assert.assertEquals("first dimensions", "20x3x11", Day02.inputLines.first())
        Assert.assertEquals("last dimensions", "14x12x8", Day02.inputLines.last())
    }

    @Test
    fun testExamples() {
        val dim234 = Day02.input2Dim("2x3x4")
        Assert.assertEquals("2x3x4 == 58 ft required", 58, dim234.wrappingPaperRequired)

        val dim1110 = Day02.input2Dim("1x1x10")
        Assert.assertEquals("1x1x10 == 43 ft req", 43, dim1110.wrappingPaperRequired)

        Assert.assertEquals("2x3x4 == 34 ft ribbon", 34, dim234.ribbonRequired)
        Assert.assertEquals("1x1x10 == 14 ft ribbon", 14, dim1110.ribbonRequired)
    }

    @Test
    fun answer1() {
        val answer = Day02.inputLines.map { Day02.input2Dim(it).wrappingPaperRequired }.sum()
        Assert.assertEquals("my answer for part 1", 1606483, answer)
    }

    @Test
    fun answer2() {
        val answer = Day02.inputLines.map { Day02.input2Dim(it).ribbonRequired }.sum()
        Assert.assertEquals("part 2 answer", 3842356, answer)
    }

}