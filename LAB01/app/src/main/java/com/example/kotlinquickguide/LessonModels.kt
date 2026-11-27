package com.example.kotlinquickguide

data class LessonResult(
    val tabTitle: String,
    val title: String,
    val topics: List<String>,
    val sampleCode: String,
    val output: String
)
