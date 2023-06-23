package com.hyeonwoo.compose_cocktail_recipes.ui.state

data class SearchState(
    val currentCocktailText: String = "",
    val isActive: Boolean = false
)