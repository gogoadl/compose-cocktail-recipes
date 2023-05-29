package com.hyeonwoo.compose_cocktail_recipes.ui

import androidx.lifecycle.ViewModel
import com.hyeonwoo.compose_cocktail_recipes.network.repository.CocktailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cocktailRepository: CocktailRepository
): ViewModel() {
}