package com.hyeonwoo.compose_cocktail_recipes.network.repository

import com.hyeonwoo.compose_cocktail_recipes.model.Cocktail
import kotlinx.coroutines.flow.Flow

interface CocktailRepository {
    fun getCocktails(s: String = "s") : Flow<Cocktail>
}