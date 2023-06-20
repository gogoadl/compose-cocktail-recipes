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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.hyeonwoo.compose_cocktail_recipes.ui.detail.CocktailDetails
import com.hyeonwoo.compose_cocktail_recipes.ui.theme.ComposecocktailrecipesTheme
import kotlinx.coroutines.flow.flatMapLatest
import java.lang.StrictMath.min


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main() {
    var expanded by remember { mutableStateOf(false) }
    var textState by remember { mutableStateOf("") }
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = stringResource(R.string.app_name))},
            actions = {
                IconButton(onClick = { expanded = !expanded }) {
                    if (expanded)
                    TextField(
                        value = textState,
                        onValueChange = { },
                    )
                    Icon(
                        contentDescription = "Search",
                        imageVector = Icons.Default.Search,
                        modifier = Modifier.padding(end = 10.dp),
                    )
                }
            })
    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel()
) {
    val navController = rememberNavController()
    val cocktails = mainViewModel.cocktailState.collectAsState().value
//

    NavHost(navController = navController, startDestination = NavScreen.Home.route) {
        composable(
            route = NavScreen.Home.route
        ) {
            Cards(
                cocktails = cocktails,
                selectCocktail = {
                    navController.navigate("${NavScreen.CocktailDetails.route}/$it")
                })
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
    selectCocktail: (String) -> Unit,
    context: Context = LocalContext.current.applicationContext
) {
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
    )
    {
        if (!cocktails.drinks.isNullOrEmpty()) {
            cocktails.drinks.let {
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
}

@Composable
fun ImageCard(
    imageUrl: String,
    contentDescription: String,
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(15.dp),
    ) {
        Box(modifier = modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build())
            val state = painter.state

            val transition by animateFloatAsState(
                targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f
            )
            if (state is AsyncImagePainter.State.Loading) {
                LoadingAnimation()
            }
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .alpha(transition)
                    .scale(.8f + (.2f * transition))
                    .alpha(min(1f, transition / .2f))
                    .align(Alignment.Center)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black,
                            ),
                            startY = 300f
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(title, style = TextStyle(color = Color.White, fontSize = 16.sp))
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPreview() {
    var expanded by remember { mutableStateOf(false) }
    var textState by remember { mutableStateOf("") }
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = stringResource(R.string.app_name))},
            actions = {
                IconButton(onClick = { expanded = !expanded }) {
                    if (expanded)
                        TextField(
                            value = textState,
                            onValueChange = { },
                        )
                    Icon(
                        contentDescription = "Search",
                        imageVector = Icons.Default.Search,
                        modifier = Modifier.padding(end = 10.dp),
                    )
                }
            })
    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colorScheme.background
        ) {
        }
    }
}
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultMainPreview() {
    ComposecocktailrecipesTheme {
        MainPreview()
    }
}

//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DefaultPreview() {
//    ComposecocktailrecipesTheme {
//        MainScreen()
//    }
//}