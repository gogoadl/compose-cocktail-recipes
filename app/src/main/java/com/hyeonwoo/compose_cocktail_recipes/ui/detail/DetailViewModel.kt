package com.hyeonwoo.compose_cocktail_recipes.ui.detail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.hyeonwoo.compose_cocktail_recipes.network.repository.CocktailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val cocktailRepository: CocktailRepository
) : ViewModel() {
    private val cocktailIdSharedFlow: MutableSharedFlow<String> = MutableSharedFlow(replay = 1)

    val cocktailDetailsFlow = cocktailIdSharedFlow.flatMapLatest {
        cocktailRepository.getCocktailById(it)
    }

    fun loadCocktailById(id: String) = cocktailIdSharedFlow.tryEmit(id)

    suspend fun loadCocktailThumb(context: Context, imagePath: String): Bitmap? {
        val job = viewModelScope.async {
            loadCocktailThumbJob(context = context, imagePath = imagePath)
            }
        return job.await()
    }
    private suspend fun loadCocktailThumbJob(context: Context, imagePath: String): Bitmap? {
        val loader = ImageLoader(context = context)
        val request = ImageRequest.Builder(context = context)
            .data(imagePath)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        val bitmap = (result as BitmapDrawable).bitmap
        return bitmap
    }
}

