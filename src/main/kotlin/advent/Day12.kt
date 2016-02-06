package advent

import java.io.File
import java.util.*

/**
 * Created by a-jotsai on 2/4/16.
 */
object Day12 {
    private val part1 = """
    --- Day 12: JSAbacusFramework.io ---

Santa's Accounting-Elves need help balancing the books after a recent order. Unfortunately, their accounting software uses a peculiar storage format. That's where you come in.

They have a JSON document which contains a variety of things: arrays ([1,2,3]), objects ({"a":1, "b":2}), numbers, and strings. Your first job is to simply find all of the numbers throughout the document and add them together.

For example:

[1,2,3] and {"a":2,"b":4} both have a sum of 6.
[[[3]]] and {"a":{"b":4},"c":-1} both have a sum of 3.
{"a":[-1,1]} and [-1,{"a":1}] both have a sum of 0.
[] and {} both have a sum of 0.
You will not encounter any strings containing numbers.

What is the sum of all numbers in the document?
    """
    val input: String by lazy { File("src/main/resources/input12.txt").readText() }

    private val part2 = """
    --- Part Two ---

Uh oh - the Accounting-Elves have realized that they double-counted everything red.

Ignore any object (and all of its children) which has any property with the value "red". Do this only for objects ({...}), not arrays ([...]).

[1,2,3] still has a sum of 6.
[1,{"c":"red","b":2},3] now has a sum of 4, because the middle object is ignored.
{"d":"red","e":[1,2,3,4],"f":5} now has a sum of 0, because the entire structure is ignored.
[1,"red",5] has a sum of 6, because "red" in an array has no effect.

    """

    sealed class JsonElement(val start: Int, val end: Int) {
        class JsonArray(start: Int, end: Int, val list: List<JsonElement>) : JsonElement(start, end)
        class JsonObject(start: Int, end: Int, val map: Map<String, JsonElement>) : JsonElement(start, end)
        class JsonNum(start: Int, end: Int, val num: Int) : JsonElement(start, end)
        class JsonString(start: Int, end: Int, val str: String) : JsonElement(start, end)
    }

    fun elementToString(json: JsonElement): String {
        return when (json) {
            is JsonElement.JsonString -> "str(${json.str})"
            is JsonElement.JsonNum -> "num(${json.num})"
            is JsonElement.JsonArray -> "arr(${json.list.map { elementToString(it) }.joinToString()})"
            is JsonElement.JsonObject -> "obj(${json.map.map { "${it.key} -> ${elementToString(it.value)}" }.joinToString()})"
        }
    }

    fun parseElement(input: String, idx: Int = 0): JsonElement {
        // do some empty check? trim?
        val char = input[idx]
        return when (char) {
            '[' -> parseArray(input, idx)
            '"' -> parseString(input, idx)
            '{' -> parseObject(input, idx)
            else -> parseNum(input, idx)
        }
    }

    // assuming string starts at quote char and ends at next quote char
    fun parseString(input: String, start: Int): JsonElement.JsonString {
        //println("parsing string")
        require(input[start] == '"')
        val end = input.indexOf('"', startIndex = start + 1)
        require(end != -1)
        return JsonElement.JsonString(start, end + 1, input.substring(start + 1, end))
    }

    val regexNum = Regex("[-]?[0-9]+")

    fun parseNum(input: String, start: Int): JsonElement.JsonNum {
        val match: MatchResult = regexNum.find(input, start) ?: throw IllegalStateException("expected to find a number")
        return JsonElement.JsonNum(start, start + match.value.length, match.value.toInt())
    }

    fun parseArray(input: String, start: Int): JsonElement.JsonArray {
        require(input[start] == '[')
        var elemIdx = start + 1
        val list = ArrayList<JsonElement>()
        do {
            val char = input[elemIdx]
            when (char) {
                ',' -> elemIdx += 1 // kinda buggy since you can have [,,,,]
                ']' -> return JsonElement.JsonArray(start, elemIdx+1, list)
                else -> {
                    val json = parseElement(input, elemIdx)
                    list.add(json)
                    elemIdx = json.end
                }
            }
        } while (elemIdx <= input.lastIndex)
        throw IllegalStateException("no end of array")
    }

    fun parseObject(input: String, start: Int): JsonElement.JsonObject {
        require(input[start] == '{') { "didn't start"}
        var elemIdx = start + 1
        val map = HashMap<String, JsonElement>()
        do {
            val char = input[elemIdx]
            when (char) {
                ',' -> elemIdx += 1 // kinda buggy since you can have [,,,,]
                '}' -> return JsonElement.JsonObject(start, elemIdx+1, map)
                '"' ->  {
                    val keyStart = elemIdx + 1
                    val keyEnd = input.indexOf('"', startIndex = keyStart)
                    require(keyEnd != -1) { "no key end" }
                    val key = input.substring(keyStart, keyEnd)
                    require(input[keyEnd + 1] == ':') { "no colon = ${input[keyEnd + 1]} index = ${keyEnd+1}" }
                    val json = parseElement(input, keyEnd + 2)
                    map.put(key, json)
                    //println("added key=$key value=${elementToString(json)}")
                    elemIdx = json.end
                }
                else -> throw IllegalStateException("unexpected char for object = $char")
            }
        } while (elemIdx <= input.lastIndex)
        throw IllegalStateException("no end of object")
    }
}