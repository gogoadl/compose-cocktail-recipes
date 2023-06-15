package com.hyeonwoo.compose_cocktail_recipes.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyeonwoo.compose_cocktail_recipes.model.Cocktail
import com.hyeonwoo.compose_cocktail_recipes.network.repository.CocktailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cocktailRepository: CocktailRepository
): ViewModel() {
    private val _cocktailState = MutableStateFlow<Cocktail>(Cocktail(listOf()))
    val cocktailState: StateFlow<Cocktail> = _cocktailState.asStateFlow()
    init {
        loadCocktails()
    }
    private fun loadCocktails() {
        viewModelScope.launch {
            cocktailRepository.getCocktails().catch {

            }.collectLatest {
                _cocktailState.value = it
            }
        }
    }
}