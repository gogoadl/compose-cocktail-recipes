package com.hyeonwoo.compose_cocktail_recipes.network.repository

import com.hyeonwoo.compose_cocktail_recipes.network.service.CocktailService
import javax.inject.Inject

class CocktailRepositoryImpl @Inject constructor(
    private val cocktailService: CocktailService
): CocktailRepository{
    override suspend fun getCocktails(s: String) {
        kotlin.runCatching { cocktailService.fetchCocktailList(s = s) }
    }

}