package com.hyeonwoo.compose_cocktail_recipes.network.repository

import com.hyeonwoo.compose_cocktail_recipes.model.Cocktail
import com.hyeonwoo.compose_cocktail_recipes.model.Drink
import com.hyeonwoo.compose_cocktail_recipes.network.service.CocktailService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CocktailRepositoryImpl @Inject constructor(
    private val cocktailService: CocktailService
): CocktailRepository {
    private val ioDispatcher = Dispatchers.IO
    override fun getCocktails(s: String): Flow<Cocktail> = flow {
        val cocktails = cocktailService.fetchCocktailList(s = s)
        emit(cocktails) // flow 를 방출
    }.flowOn(ioDispatcher) // flow는 현재 코루틴 컨텍스트에서 호출되므로, IO 컨텍스트에서 실행되게 설정

    override fun getCocktailById(id: String): Flow<Cocktail> = flow {
        val cocktail = cocktailService.getCocktail(id)
        emit(cocktail)
    }

    override fun getRandomCocktail(): Flow<Cocktail> = flow {
        val cocktail = cocktailService.getRandomCocktail()
        emit(cocktail)
    }
}