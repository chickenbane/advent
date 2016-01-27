package advent

import java.math.BigInteger
import java.security.MessageDigest

/**
 * Created by a-jotsai on 1/27/16.
 */
object Day04 {
    private val part1 = """
--- Day 4: The Ideal Stocking Stuffer ---

Santa needs help mining some AdventCoins (very similar to bitcoins) to use as gifts for all the economically forward-thinking little girls and boys.

To do this, he needs to find MD5 hashes which, in hexadecimal, start with at least five zeroes.
The input to the MD5 hash is some secret key (your puzzle input, given below) followed by a number in decimal.
To mine AdventCoins, you must find Santa the lowest positive number (no leading zeroes: 1, 2, 3, ...) that produces such a hash.

For example:

If your secret key is abcdef, the answer is 609043, because the MD5 hash of abcdef609043 starts with five zeroes (000001dbbfa...), and it is the lowest such number to do so.
If your secret key is pqrstuv, the lowest number it combines with to make an MD5 hash starting with five zeroes is 1048970; that is, the MD5 hash of pqrstuv1048970 looks like 000006136ef....

Your puzzle input is iwrupvqb.
    """
    val md = MessageDigest.getInstance("MD5")

    fun hashWithKey(key: String, num: Int): String = md5hash(key + num.toString())

    fun md5hash(input: String): String {
        val digest = md.digest(input.toByteArray())
        return java.lang.String.format("%032x", BigInteger(1, digest))
    }

    val secretKey = "iwrupvqb"

    fun mine(startsWith: String = "00000"): Int {
        var i = 0
        while(i < Int.MAX_VALUE) {
            val hash = hashWithKey(secretKey, i)
            if (hash.startsWith(startsWith)) {
                println("found hash=$hash")
                return i
            }
            i += 1
        }
        throw IllegalStateException("no input found")
    }
}