package com.example.kotlinquickguide

/**
 * Bài 1 — Kotlin Quick Guide:
 * Chương trình Kotlin, Kiểu dữ liệu, Hàm, Luồng điều khiển (if/else, when, repeat) và Lớp (Dice).
 */
class Dice(val numSides: Int) {
    fun roll(): Int = (1..numSides).random()
}

fun runBai1(): LessonResult {
    val out = StringBuilder()

    // 1. Chương trình Kotlin, biến val/var, chuỗi mẫu
    out.appendLine("Hello, world!")
    val age = "5"
    val name = "Rover"
    var roll = 6
    val rolledValue: Int = 4
    out.appendLine("You are already $age days old, $name! (roll=$roll, rolledValue=$rolledValue)")

    // 2. Hàm với tham số và giá trị trả về
    fun buildBorder(border: String, timesToRepeat: Int): String = border.repeat(timesToRepeat)
    out.appendLine(buildBorder("=", 23))

    // 3. Vẽ bánh sinh nhật bằng repeat lồng nhau
    fun buildCakeBottom(cakeAge: Int, layers: Int): String {
        val sb = StringBuilder()
        repeat(layers) {
            repeat(cakeAge + 2) { sb.append("@") }
            sb.appendLine()
        }
        return sb.toString().trimEnd()
    }
    out.appendLine("Cake (age=4, layers=2):")
    out.appendLine(buildCakeBottom(4, 2))

    // 4. Luồng điều khiển: if/else và when
    val num = 4
    val ifResult = if (num > 4) {
        "The variable is greater than 4"
    } else if (num == 4) {
        "The variable is equal to 4"
    } else {
        "The variable is less than 4"
    }
    out.appendLine("if/else (num=$num): $ifResult")

    // 5. Lớp Dice & câu lệnh when
    val myFirstDice = Dice(6)
    val rollResult = myFirstDice.roll()
    val luckyNumber = 4
    val whenResult = when (rollResult) {
        luckyNumber -> "You won!"
        1 -> "So sorry! You rolled a 1. Try again!"
        2 -> "Sadly, you rolled a 2. Try again!"
        3 -> "Unfortunately, you rolled a 3. Try again!"
        5 -> "Don't cry! You rolled a 5. Try again!"
        else -> "Apologies! You rolled a $rollResult. Try again!"
    }
    out.appendLine("Dice(${myFirstDice.numSides}).roll() -> $rollResult: $whenResult")

    val sample = """
fun main() {
    val name = "Rover"
    val age = "5"
    println("You are already ${'$'}age days old, ${'$'}name!")

    val myFirstDice = Dice(6)
    val rollResult = myFirstDice.roll()
    when (rollResult) {
        4 -> println("You won!")
        else -> println("Rolled ${'$'}rollResult. Try again!")
    }
}

class Dice(val numSides: Int) {
    fun roll(): Int = (1..numSides).random()
}
    """.trimIndent()

    return LessonResult(
        tabTitle = "Bài 1",
        title = "Bài 1: Chương trình Kotlin, Luồng điều khiển & Lớp",
        topics = listOf(
            "Biến val / var & String template",
            "Kiểu Int, String, IntRange, Boolean",
            "Hàm, repeat(), if/else & when",
            "Lớp Dice(numSides) và phương thức roll()"
        ),
        sampleCode = sample,
        output = out.toString().trimEnd()
    )
}
