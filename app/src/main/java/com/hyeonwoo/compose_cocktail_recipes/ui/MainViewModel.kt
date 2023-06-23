package com.hyeonwoo.compose_cocktail_recipes.ui

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyeonwoo.compose_cocktail_recipes.model.Cocktail
import com.hyeonwoo.compose_cocktail_recipes.model.Drink
import com.hyeonwoo.compose_cocktail_recipes.network.repository.CocktailRepository
import com.hyeonwoo.compose_cocktail_recipes.ui.state.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cocktailRepository: CocktailRepository
): ViewModel() {
    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val cocktailState = searchState.flatMapLatest {
        if (it.currentCocktailText.isEmpty())
            cocktailRepository.getRandomCocktail()
        else
            cocktailRepository.getCocktails(it.currentCocktailText)
    }

    fun updateSearchText(searchText: String) {
        Timber.i("searchText : $searchText")
        _searchState.update {
            it.copy(currentCocktailText = searchText)
        }
    }

    fun updateSearchActive(isActive: Boolean) {
        _searchState.update {
            it.copy(isActive = isActive)
        }
    }
}