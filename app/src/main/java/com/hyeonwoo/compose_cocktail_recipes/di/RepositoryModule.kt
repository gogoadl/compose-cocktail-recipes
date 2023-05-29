package com.hyeonwoo.compose_cocktail_recipes.di

import com.hyeonwoo.compose_cocktail_recipes.network.repository.CocktailRepository
import com.hyeonwoo.compose_cocktail_recipes.network.repository.CocktailRepositoryImpl
import com.hyeonwoo.compose_cocktail_recipes.network.service.CocktailService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideCocktailRepository(
        cocktailService: CocktailService
    ): CocktailRepository = CocktailRepositoryImpl(cocktailService = cocktailService)
}