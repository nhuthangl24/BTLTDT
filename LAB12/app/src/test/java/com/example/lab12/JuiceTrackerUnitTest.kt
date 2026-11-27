package com.example.lab12

import com.example.lab12.data.JuiceColor
import com.example.lab12.ui.JuiceViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class JuiceTrackerUnitTest {

    @Test
    fun defaultJuices_arePopulated() {
        val viewModel = JuiceViewModel()
        val juices = viewModel.juiceList.value

        assertEquals(3, juices.size)
        assertEquals("Sunrise Orange Boost", juices[0].name)
        assertEquals(JuiceColor.Orange, juices[0].color)
        assertEquals(5, juices[0].rating)
    }

    @Test
    fun addJuice_incrementsListAndSetsProperties() {
        val viewModel = JuiceViewModel()
        val initialSize = viewModel.juiceList.value.size

        viewModel.addJuice(
            name = "Watermelon Cooler",
            description = "Fresh watermelon with lime and mint",
            color = JuiceColor.Red,
            rating = 5
        )

        val updatedList = viewModel.juiceList.value
        assertEquals(initialSize + 1, updatedList.size)

        val lastJuice = updatedList.last()
        assertEquals("Watermelon Cooler", lastJuice.name)
        assertEquals(JuiceColor.Red, lastJuice.color)
        assertEquals(5, lastJuice.rating)
        assertTrue(lastJuice.id > 3)
    }

    @Test
    fun addJuice_withBlankFields_usesDefaults() {
        val viewModel = JuiceViewModel()
        viewModel.addJuice(
            name = "",
            description = "",
            color = JuiceColor.Purple,
            rating = 7 // Should coerce to 5
        )

        val lastJuice = viewModel.juiceList.value.last()
        assertEquals("Untitled Juice", lastJuice.name)
        assertEquals("No description provided.", lastJuice.description)
        assertEquals(5, lastJuice.rating)
    }
}
