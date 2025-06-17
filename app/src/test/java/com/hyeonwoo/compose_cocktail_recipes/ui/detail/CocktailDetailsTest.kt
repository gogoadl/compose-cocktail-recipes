package com.hyeonwoo.compose_cocktail_recipes.ui.detail

import com.hyeonwoo.compose_cocktail_recipes.model.DummyDrink
import org.junit.Assert.assertEquals
import org.junit.Test

class CocktailDetailsTest {

    private val drink = DummyDrink()

    @Test
    fun ingredients_returnsExpectedList() {
        val expected = listOf("Gin", "Dry Vermouth", "Olive")
        val result = ingredients(drink)
        assertEquals(expected, result)
    }

    @Test
    fun measures_returnsExpectedList() {
        val expected = listOf("1 2/3 oz", "1/3 oz", "1")
        val result = measures(drink)
        assertEquals(expected, result)
    }
}
