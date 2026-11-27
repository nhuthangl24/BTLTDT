package com.example.kotlinquickguide

import kotlin.math.PI
import kotlin.math.sqrt

/**
 * Bài 2 — Kotlin Quick Guide:
 * Lớp trừu tượng (abstract), Kế thừa (open/override), Danh sách (List/MutableList), Vòng lặp & Chuỗi.
 */
abstract class Dwelling(private var residents: Int) {
    abstract val buildingMaterial: String
    abstract val capacity: Int

    fun hasRoom(): Boolean = residents < capacity
    abstract fun floorArea(): Double
}

open class RoundHut(residents: Int, val radius: Double) : Dwelling(residents) {
    override val buildingMaterial: String = "Straw"
    override val capacity: Int = 4
    override fun floorArea(): Double = PI * radius * radius
}

class SquareCabin(residents: Int, val length: Double, val floors: Int) : Dwelling(residents) {
    override val buildingMaterial: String = "Wood"
    override val capacity: Int = 6 * floors
    override fun floorArea(): Double = length * length * floors
}

class RoundTower(residents: Int, radius: Double, val floors: Int = 2) : RoundHut(residents, radius) {
    override val buildingMaterial: String = "Stone"
    override val capacity: Int = 4 * floors
    override fun floorArea(): Double = super.floorArea() * floors
}

fun runBai2(): LessonResult {
    val out = StringBuilder()

    // 1. Lớp trừu tượng, kế thừa & hàm phạm vi with()
    val squareCabin = SquareCabin(residents = 6, length = 50.0, floors = 1)
    val roundHut = RoundHut(residents = 3, radius = 10.0)
    val roundTower = RoundTower(residents = 4, radius = 15.5, floors = 2)

    with(squareCabin) {
        out.appendLine("SquareCabin -> Material: $buildingMaterial, Capacity: $capacity, Has room: ${hasRoom()}, Area: ${"%.2f".format(floorArea())}")
    }
    with(roundHut) {
        out.appendLine("RoundHut    -> Material: $buildingMaterial, Capacity: $capacity, Has room: ${hasRoom()}, Area: ${"%.2f".format(floorArea())}")
    }
    with(roundTower) {
        out.appendLine("RoundTower  -> Material: $buildingMaterial, Capacity: $capacity, Has room: ${hasRoom()}, Area: ${"%.2f".format(floorArea())}")
    }

    // 2. Danh sách read-only & mutable
    val numbers = listOf(1, 2, 3, 4, 5, 6)
    out.appendLine("numbers.size=${numbers.size}, first=${numbers[0]}, reversed=${listOf("red", "blue", "green").reversed()}")

    val entrees = mutableListOf<String>()
    entrees.add("spaghetti")
    entrees.add("ravioli")
    entrees[0] = "lasagna"
    entrees.remove("ravioli")
    out.appendLine("MutableList entrees: $entrees")

    // 3. Vòng lặp for & while
    val myList = listOf("Android", "Kotlin", "Compose")
    val forItems = mutableListOf<String>()
    for (element in myList) {
        forItems.add(element)
    }
    var index = 0
    val whileItems = mutableListOf<String>()
    while (index < myList.size) {
        whileItems.add("${index}:${myList[index]}")
        index++
    }
    out.appendLine("for loop: $forItems")
    out.appendLine("while loop: $whileItems")

    // 4. Chuỗi & biểu thức trong chuỗi
    val name = "Android"
    val number = 10
    val groups = 5
    out.appendLine("name='$name' (length=${name.length}), total=${number * groups} people, sqrt(2)=${"%.3f".format(sqrt(2.0))}")

    val sample = """
abstract class Dwelling(private var residents: Int) {
    abstract val buildingMaterial: String
    abstract fun floorArea(): Double
}

with(squareCabin) {
    println("Capacity: ${'$'}capacity, Material: ${'$'}buildingMaterial")
}

val entrees = mutableListOf<String>()
entrees.add("spaghetti")
for (item in entrees) println(item)
    """.trimIndent()

    return LessonResult(
        tabTitle = "Bài 2",
        title = "Bài 2: Lớp, Danh sách, Vòng lặp & Chuỗi",
        topics = listOf(
            "abstract class, open class, override & super",
            "Hàm phạm vi with(...) & kotlin.math.PI",
            "listOf(...) và mutableListOf(...)",
            "Vòng lặp for, while & biểu thức chuỗi"
        ),
        sampleCode = sample,
        output = out.toString().trimEnd()
    )
}
