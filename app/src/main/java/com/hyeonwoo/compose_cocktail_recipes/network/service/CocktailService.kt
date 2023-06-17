package com.hyeonwoo.compose_cocktail_recipes.network.service

import com.hyeonwoo.compose_cocktail_recipes.model.Cocktail
import com.hyeonwoo.compose_cocktail_recipes.model.Drink
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailService {
    @GET("search.php")
    suspend fun fetchCocktailList(
        @Query("s") s: String = "s"
    ): Cocktail

    @GET("lookup.php")
    suspend fun getCocktail(
        @Query("i") id: String
    ): Cocktail

    @GET("random.php")
    suspend fun getRandomCocktail(): Cocktail
}