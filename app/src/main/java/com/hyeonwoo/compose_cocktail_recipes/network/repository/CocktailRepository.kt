package com.hyeonwoo.compose_cocktail_recipes.network.repository

interface CocktailRepository {
    suspend fun getCocktails(s: String)
}