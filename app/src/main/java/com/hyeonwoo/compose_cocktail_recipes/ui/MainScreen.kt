package com.hyeonwoo.compose_cocktail_recipes.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hyeonwoo.compose_cocktail_recipes.R
import com.hyeonwoo.compose_cocktail_recipes.model.Cocktail
import com.hyeonwoo.compose_cocktail_recipes.model.Drink
import com.hyeonwoo.compose_cocktail_recipes.ui.detail.CocktailDetails
import com.hyeonwoo.compose_cocktail_recipes.ui.theme.ComposecocktailrecipesTheme
import com.hyeonwoo.compose_cocktail_recipes.util.SearchAppBar


@Composable
fun Main() {
    MainScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val cocktails = mainViewModel.cocktailState.collectAsState(initial = Cocktail(emptyList<Drink?>())).value

    val searchText by mainViewModel.searchText.collectAsState()
    val searchState by mainViewModel.searchState.collectAsState()

    NavHost(navController = navController, startDestination = NavScreen.Home.route) {
        composable(
            route = NavScreen.Home.route
        ) {
            Scaffold(topBar = {
                SearchAppBar(
                    title = stringResource(id = R.string.app_name),
                    query = searchText,
                    placeholder = "",
                    targetState = searchState,
                    onSearch = { mainViewModel.updateSearchActive(searchState.not()) },
                    onQueryChange = { mainViewModel.updateSearchText(it) },
                    onQueryDone = { mainViewModel.updateSearchActive(searchState.not()) } ,
                    onClose = { mainViewModel.updateSearchText("") })
                }
            ) {
                Column(modifier = Modifier.padding(it)) {
                    Cards(
                        cocktails = cocktails,
                        searchText = searchText,
                        selectCocktail = {
                            navController.navigate("${NavScreen.CocktailDetails.route}/$it")
                    })
                }
            }
        }
        composable(
            route = NavScreen.CocktailDetails.routeWithArgument,
            arguments = listOf(
                navArgument(NavScreen.CocktailDetails.argument0) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val cocktailId =
                backStackEntry.arguments?.getString(NavScreen.CocktailDetails.argument0) ?: return@composable

            CocktailDetails(cocktailId = cocktailId, viewModel = hiltViewModel()) {
                navController.navigateUp()
            }
        }
    }
}
@Composable
fun Cards(
    cocktails: Cocktail,
    searchText: String,
    selectCocktail: (String) -> Unit
) {
    if (!cocktails.drinks.isNullOrEmpty()) {
        cocktails.drinks.let {
            val isSingleItem = it.size == 1

            if (isSingleItem) {
                val drink = it.first()
                Box(modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = {
                        selectCocktail(drink?.idDrink!!)
                    }),
                    contentAlignment = Center) {
                    SingleImageCard(
                        imageUrl = drink?.strDrinkThumb ?: "",
                        contentDescription = drink?.strDrink ?: "description",
                        title = drink?.strDrink ?: "title"
                    )
                }

            } else {
                LazyVerticalGrid(
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        top = 16.dp,
                        end = 12.dp,
                        bottom = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    columns = GridCells.Adaptive(128.dp)
                ) {
                    items(it.size) { index ->
                        val drink = it[index]
                        ImageCard(
                            imageUrl = drink!!.strDrinkThumb ?: "",
                            contentDescription = drink.strDrink ?: "description",
                            title = drink.strDrink ?: "title",
                            modifier = Modifier
                                .clickable(onClick = { selectCocktail(drink.idDrink!!) })
                        )
                    }
                }
            }
        }
    } else {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row() {
                Text(text = stringResource(id = R.string.no_result),
                    fontSize = 20.sp)
                Text(text = "\"${searchText}\"",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp)
            }
        }

    }
}

sealed class NavScreen(val route: String) {

    object Home : NavScreen("Home")

    object CocktailDetails : NavScreen("CocktailDetails") {

        const val routeWithArgument: String = "CocktailDetails/{cocktailId}"

        const val argument0: String = "cocktailId"
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultPreview() {
    ComposecocktailrecipesTheme {
//        MainScreen()
    }
}