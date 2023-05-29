package com.hyeonwoo.compose_cocktail_recipes

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CocktailApplication : Application(){
    override fun onCreate() {
        super.onCreate()
    }
}