package com.hyeonwoo.compose_cocktail_recipes.ui

import LoadingAnimation
import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hyeonwoo.compose_cocktail_recipes.R
import com.hyeonwoo.compose_cocktail_recipes.model.Cocktail
import com.hyeonwoo.compose_cocktail_recipes.model.Drink
import com.hyeonwoo.compose_cocktail_recipes.ui.detail.CocktailDetails
import com.hyeonwoo.compose_cocktail_recipes.ui.state.SearchState
import com.hyeonwoo.compose_cocktail_recipes.ui.theme.ComposecocktailrecipesTheme
import kotlinx.coroutines.flow.flatMapLatest
import timber.log.Timber
import java.lang.StrictMath.min


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main() {
    MainScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel(),
) {
    val navController = rememberNavController()
    val cocktails = mainViewModel.cocktailState.collectAsState(initial = Cocktail(emptyList<Drink?>())).value
    val searchState by mainViewModel.searchState.collectAsState()
    NavHost(navController = navController, startDestination = NavScreen.Home.route) {
        composable(
            route = NavScreen.Home.route
        ) {
            Scaffold(topBar = {
                TopAppBar(title = { Text(text = stringResource(id = R.string.app_name) )})
                }
            ) {
                Column(modifier = Modifier.padding(it)) {
                    SearchBar(
                        query = searchState.currentCocktailText,
                        onQueryChange = { mainViewModel.updateSearchText(it) },
                        onSearch = { mainViewModel.updateSearchActive(false) },
                        active = searchState.isActive,
                        onActiveChange = {
                            mainViewModel.updateSearchActive(it)
                        },
                        placeholder = { Text(stringResource(id = R.string.search_hint)) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    ) {}
                    Cards(
                        cocktails = cocktails,
                        searchState = searchState,
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
    searchState: SearchState,
    selectCocktail: (String) -> Unit,
    context: Context = LocalContext.current.applicationContext
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
                        title = drink?.strDrink ?: "title",
                        modifier = Modifier
                            .fillMaxSize()
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
        Column(modifier = Modifier.fillMaxSize().padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row() {
                Text(text = stringResource(id = R.string.no_result),
                    fontSize = 20.sp)
                Text(text = "\"${searchState.currentCocktailText}\"",
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