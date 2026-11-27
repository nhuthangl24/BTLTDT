package com.example.kotlinquickguide

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/**
 * Bài 4 — Kotlin Quick Guide:
 * Coroutine (suspend, async/await, runBlocking), object Singleton, enum class & try/catch.
 */
enum class Direction {
    NORTH, SOUTH, WEST, EAST
}

object DataProviderManager {
    fun getSourceName(): String = "KotlinQuickGuideProvider"
}

suspend fun getValue(seed: Int): Double {
    delay(20L)
    return seed * 12.5
}

suspend fun processValue(seed: Int): String {
    val value = getValue(seed)
    return "Processed(${"%.1f".format(value)})"
}

fun runBai4(): LessonResult {
    val out = StringBuilder()

    // 1. Coroutine: runBlocking + async/await + suspend
    runBlocking {
        val deferred = async { getValue(4) }
        val output = deferred.await()
        val processed = processValue(8)
        out.appendLine("Coroutine async/await -> getValue(4) = $output")
        out.appendLine("suspend processValue(8) = $processed")
    }

    // 2. Singleton object
    out.appendLine("object DataProviderManager: ${DataProviderManager.getSourceName()}")

    // 3. enum class + when
    val direction = Direction.NORTH
    val dirDesc = when (direction) {
        Direction.NORTH -> "Heading North (Bắc)"
        Direction.SOUTH -> "Heading South (Nam)"
        Direction.WEST -> "Heading West (Tây)"
        Direction.EAST -> "Heading East (Đông)"
    }
    out.appendLine("enum $direction -> $dirDesc")

    // 4. try / catch xử lý ngoại lệ
    val parsed = try {
        "abc".toInt()
    } catch (e: NumberFormatException) {
        out.appendLine("try/catch caught: ${e::class.simpleName} (${e.message})")
        -1
    }
    out.appendLine("Fallback value after catch: $parsed")

    val sample = """
enum class Direction { NORTH, SOUTH, WEST, EAST }

object DataProviderManager {
    fun getSourceName() = "KotlinQuickGuideProvider"
}

suspend fun getValue(): Double {
    delay(20L)
    return 50.0
}

runBlocking {
    val output = async { getValue() }
    println("Output is ${'$'}{output.await()}")
}

try {
    "abc".toInt()
} catch (e: Exception) {
    println("Handled: ${'$'}{e.message}")
}
    """.trimIndent()

    return LessonResult(
        tabTitle = "Bài 4",
        title = "Bài 4: Coroutine, Enum, Object & Try/Catch",
        topics = listOf(
            "Hàm suspend, runBlocking, async & await()",
            "Singleton với từ khóa object",
            "enum class Direction & nhánh when",
            "Xử lý ngoại lệ try / catch"
        ),
        sampleCode = sample,
        output = out.toString().trimEnd()
    )
}
