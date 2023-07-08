package com.hyeonwoo.compose_cocktail_recipes.ui

import androidx.lifecycle.ViewModel
import com.hyeonwoo.compose_cocktail_recipes.network.repository.CocktailRepository
import com.hyeonwoo.compose_cocktail_recipes.ui.state.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cocktailRepository: CocktailRepository
): ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _searchState = MutableStateFlow(false)
    val searchState = _searchState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val cocktailState = searchText.flatMapLatest {
        if (it.isEmpty())
            cocktailRepository.getRandomCocktail()
        else
            cocktailRepository.getCocktails(it)
    }

    fun updateSearchText(searchText: String) {
        Timber.i("searchText : $searchText")
        _searchText.value = searchText
    }

    fun updateSearchActive(isActive: Boolean) {
        _searchState.value = isActive
    }
}