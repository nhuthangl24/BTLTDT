package com.example.kotlinquickguide

/**
 * Bài 3 — Kotlin Quick Guide:
 * Tập hợp (Set, Map), Phép toán trên tập hợp (forEach, map, filter), Lambda, Hàm phạm vi (let, apply) & Toán tử Elvis (?:).
 */
fun runBai3(): LessonResult {
    val out = StringBuilder()

    // 1. Set (Nhóm)
    val numbers = listOf(0, 3, 8, 4, 0, 5, 5, 8, 9, 2)
    val setOfNumbers = numbers.toSet()
    val set1 = setOf(1, 2, 3)
    val set2 = mutableSetOf(3, 4, 5)
    out.appendLine("toSet(): $setOfNumbers")
    out.appendLine("intersect: ${set1.intersect(set2)}, union: ${set1.union(set2)}")

    // 2. Map (Sơ đồ) & Phép toán trên tập hợp
    val peopleAges = mutableMapOf(
        "Fred" to 30,
        "Ann" to 23
    )
    peopleAges["Fred"] = 31
    peopleAges.put("Barbara", 42)
    peopleAges["Joe"] = 51

    val mapped = peopleAges.map { "${it.key} is ${it.value}" }.joinToString(", ")
    out.appendLine("map(): $mapped")

    val filteredNames = peopleAges.filter { it.key.length < 4 }
    out.appendLine("filter(len < 4): $filteredNames")

    val words = listOf("about", "acute", "balloon", "best", "brief", "class")
    val filteredWords = words
        .filter { it.startsWith("b", ignoreCase = true) }
        .shuffled()
        .take(2)
        .sorted()
    out.appendLine("words chain (b*, shuffled, take 2, sorted): $filteredWords")

    // 3. Lambda, Hàm phạm vi (let, apply) & Elvis (?:)
    val triple: (Int) -> Int = { a: Int -> a * 3 }
    out.appendLine("lambda triple(5) = ${triple(5)}")

    val nullableTitle: String? = "Kotlin"
    nullableTitle?.let {
        out.appendLine("let(): length of '$it' = ${it.length}")
    }

    val sb = StringBuilder().apply {
        append("apply(): ")
        append("Configured fluently")
    }
    out.appendLine(sb.toString())

    var quantity: Int? = null
    val q1 = quantity ?: 0
    quantity = 4
    val q2 = quantity ?: 0
    out.appendLine("Elvis (?:) -> null: $q1, assigned 4: $q2")

    val sample = """
val set1 = setOf(1, 2, 3)
val set2 = setOf(3, 4, 5)
println(set1.intersect(set2)) // [3]

val peopleAges = mutableMapOf("Fred" to 31, "Ann" to 23)
println(peopleAges.filter { it.key.length < 4 })

val words = listOf("about", "acute", "balloon", "best", "brief")
val result = words.filter { it.startsWith("b") }
    .shuffled().take(2).sorted()

val triple: (Int) -> Int = { a -> a * 3 }
var quantity: Int? = null
println(quantity ?: 0)
    """.trimIndent()

    return LessonResult(
        tabTitle = "Bài 3",
        title = "Bài 3: Set, Map, Phép toán tập hợp & Scope Functions",
        topics = listOf(
            "Set: toSet(), intersect(), union()",
            "MutableMap, forEach, map(), filter()",
            "Chuỗi phép toán filter -> shuffled -> take -> sorted",
            "Lambda, let, apply & toán tử Elvis (?:)"
        ),
        sampleCode = sample,
        output = out.toString().trimEnd()
    )
}
