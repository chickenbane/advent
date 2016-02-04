package advent

import java.util.*

/**
 * Created by a-jotsai on 2/3/16.
 */
object Day11 {
    private val part1 = """
--- Day 11: Corporate Policy ---

Santa's previous password expired, and he needs help choosing a new one.

To help him remember his new password after the old one expires, Santa has devised a method of coming up with a password
based on the previous one. Corporate policy dictates that passwords must be exactly eight lowercase letters
(for security reasons), so he finds his new password by incrementing his old password string repeatedly until it is valid.

Incrementing is just like counting with numbers: xx, xy, xz, ya, yb, and so on. Increase the rightmost letter one step;
if it was z, it wraps around to a, and repeat with the next letter to the left until one doesn't wrap around.

Unfortunately for Santa, a new Security-Elf recently started, and he has imposed some additional password requirements:

Passwords must include one increasing straight of at least three letters, like abc, bcd, cde, and so on, up to xyz. They cannot skip letters; abd doesn't count.
Passwords may not contain the letters i, o, or l, as these letters can be mistaken for other characters and are therefore confusing.
Passwords must contain at least two different, non-overlapping pairs of letters, like aa, bb, or zz.
For example:

hijklmmn meets the first requirement (because it contains the straight hij) but fails the second requirement requirement (because it contains i and l).
abbceffg meets the third requirement (because it repeats bb and ff) but fails the first requirement.
abbcegjk fails the third requirement, because it only has one double letter (bb).
The next password after abcdefgh is abcdffaa.
The next password after ghijklmn is ghjaabcc, because you eventually skip all the passwords that start with ghi..., since i is not allowed.
Given Santa's current password (your puzzle input), what should his next password be?

Your puzzle input is cqjxjnds.
    """

    val alphabet: CharArray = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    fun nextPass(input: String): String {
        require(input.length == 8)
        require(input.all { it.isLowerCase() })
        var pass = input
        do {
            pass = incrementPass(pass)
            require(pass.length == 8)
            val hasIncStraight = hasIncreasingStraight(pass)
            val hasNoBanned = !hasBanned(pass)
            val hasTwoPairs = hasTwoPairs(pass)
            if (hasIncStraight && hasNoBanned && hasTwoPairs) {
                return pass
//            } else {
//                var msg = "nope on pass=$pass"
//                if (!hasIncStraight) msg += " does not have inc straight"
//                if (!hasNoBanned) msg += " has banned"
//                if (!hasTwoPairs) msg += " does not have two pairs"
//                println(msg)
            }
        } while (true)
        throw IllegalStateException("whaaaaa")
    }

    // I don't want to work backwards, so I'm going to reverse the string and the reverse when I'm done
    fun incrementPass(input: String): String {
        val rev = input.reversed()

        val sb = StringBuilder()
        var idx = 0
        do {
            val c = rev[idx]
            val again = c == 'z'
            if (again) {
                sb.append('a')
                idx += 1
            } else {
                val cIdx = alphabet.indexOf(c)
                require(cIdx != -1)
                sb.append(alphabet[cIdx + 1])
            }
        } while (again)
        val next = sb.toString()

        val pass = next + rev.substring(next.length)
        return pass.reversed()
    }

    fun hasIncreasingStraight(pass: String): Boolean {
        for (i in 0..pass.lastIndex - 2) {
            val a = pass[i]
            val b = pass[i + 1]
            val c = pass[i + 2]

            val aIdx = alphabet.indexOf(a)
            val bIdx = alphabet.indexOf(b)
            val cIdx = alphabet.indexOf(c)
            require(aIdx != -1 && bIdx != -1 && cIdx != -1)

            if (bIdx == aIdx + 1 && cIdx == bIdx + 1) {
                return true
            }
        }
        return false
    }

    val banned = setOf('i', 'o', 'l')

    fun hasBanned(pass: String): Boolean = pass.any { banned.contains(it) }

    fun hasTwoPairs(pass: String): Boolean {
        val set = HashSet<Char>()
        for (c in pass) {
            if (pass.contains("$c$c")) {
                set.add(c)
            }
        }
        return set.size >= 2
    }

}