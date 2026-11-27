package com.example.lab12.ui

import androidx.lifecycle.ViewModel
import com.example.lab12.data.Juice
import com.example.lab12.data.JuiceColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicLong

class JuiceViewModel : ViewModel() {
    private val nextId = AtomicLong(4)

    private val _juiceList = MutableStateFlow<List<Juice>>(
        listOf(
            Juice(
                id = 1,
                name = "Sunrise Orange Boost",
                description = "Freshly squeezed oranges with a dash of ginger and carrot.",
                color = JuiceColor.Orange,
                rating = 5
            ),
            Juice(
                id = 2,
                name = "Green Detox Cleanse",
                description = "Crisp green apple, celery stalks, spinach, and cucumber.",
                color = JuiceColor.Green,
                rating = 4
            ),
            Juice(
                id = 3,
                name = "Berry Bliss Sparkle",
                description = "Wild strawberries, blueberries, and pomegranate splash.",
                color = JuiceColor.Red,
                rating = 5
            )
        )
    )
    val juiceList: StateFlow<List<Juice>> = _juiceList.asStateFlow()

    fun addJuice(name: String, description: String, color: JuiceColor, rating: Int) {
        val newJuice = Juice(
            id = nextId.getAndIncrement(),
            name = name.ifBlank { "Untitled Juice" },
            description = description.ifBlank { "No description provided." },
            color = color,
            rating = rating.coerceIn(1, 5)
        )
        _juiceList.value = _juiceList.value + newJuice
    }
}
