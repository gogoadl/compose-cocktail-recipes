package com.hyeonwoo.compose_cocktail_recipes.network.repository

import com.hyeonwoo.compose_cocktail_recipes.model.Cocktail
import com.hyeonwoo.compose_cocktail_recipes.model.Drink
import kotlinx.coroutines.flow.Flow

interface CocktailRepository {
    fun getCocktails(s: String = "s") : Flow<Cocktail>
    fun getCocktailById(id: String) : Flow<Cocktail>
    fun getRandomCocktail() : Flow<Cocktail>
}