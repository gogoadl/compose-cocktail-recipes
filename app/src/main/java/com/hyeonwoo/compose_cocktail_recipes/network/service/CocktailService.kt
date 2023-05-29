package com.hyeonwoo.compose_cocktail_recipes.network.service

import com.hyeonwoo.compose_cocktail_recipes.model.Cocktail
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailService {
    @GET
    suspend fun fetchCocktailList(
        @Query("s") s: String = "s"
    ): Cocktail
}