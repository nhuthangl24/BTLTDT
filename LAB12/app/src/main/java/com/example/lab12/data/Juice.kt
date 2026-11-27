package com.example.lab12.data

enum class JuiceColor(val label: String, val colorInt: Int, val composeColorHex: Long) {
    Red("Red (Watermelon / Berry)", 0xFFE57373.toInt(), 0xFFE57373),
    Orange("Orange (Carrot / Citrus)", 0xFFFFB74D.toInt(), 0xFFFFB74D),
    Yellow("Yellow (Pineapple / Lemon)", 0xFFFFF176.toInt(), 0xFFFFF176),
    Green("Green (Celery / Apple)", 0xFF81C784.toInt(), 0xFF81C784),
    Purple("Purple (Grape / Beet)", 0xFFBA68C8.toInt(), 0xFFBA68C8)
}

data class Juice(
    val id: Long,
    val name: String,
    val description: String,
    val color: JuiceColor,
    val rating: Int // 1..5
)
