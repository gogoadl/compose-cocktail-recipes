package com.hyeonwoo.compose_cocktail_recipes.ui.detail

import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ErrorResult
import coil.request.SuccessResult
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hyeonwoo.compose_cocktail_recipes.R
import com.hyeonwoo.compose_cocktail_recipes.model.Cocktail
import com.hyeonwoo.compose_cocktail_recipes.model.Drink
import com.hyeonwoo.compose_cocktail_recipes.model.DummyDrink
import com.hyeonwoo.compose_cocktail_recipes.ui.Chip
import com.hyeonwoo.compose_cocktail_recipes.ui.NavScreen
import com.hyeonwoo.compose_cocktail_recipes.util.PaletteGenerator
import com.hyeonwoo.compose_cocktail_recipes.util.ParsedColor
import com.hyeonwoo.compose_cocktail_recipes.util.RequestStatus
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailDetails(
    cocktailId: String,
    viewModel: DetailViewModel,
    pressOnBack: () -> Unit = { }
) {
    val drink: Drink? = viewModel.cocktailDetailsFlow.collectAsState(initial = null).value?.drinks?.get(0)
    var isLoading = true
    var currentRotation by remember { mutableFloatStateOf(0f) }
    val rotation = remember { Animatable(currentRotation) }
    var parsedColor by remember { mutableStateOf<ParsedColor>(ParsedColor()) }
    val context = LocalContext.current
    var imageResult by remember {
        mutableStateOf<RequestStatus>(RequestStatus.Loading)
    }

    LaunchedEffect(key1 = drink?.strDrinkThumb) {
        imageResult = RequestStatus.Loading
        val result = PaletteGenerator.convertImageUrlToBitmap(
            drink?.strDrinkThumb?: "",
            context
        )
        if (result is SuccessResult) {
            Timber.d("result is success")
            val bitmap = (result.drawable as BitmapDrawable).bitmap
            if (bitmap != null) {
                parsedColor = PaletteGenerator.extractColorsFromBitmapToParsedColors(bitmap = bitmap)
            }
            imageResult = RequestStatus.Success
        }

        if (result is ErrorResult) {
            Timber.d("result is Error")
            imageResult = RequestStatus.Error
        }

        Timber.d("parsed Colors : ${parsedColor.toString()}")
    }

    LaunchedEffect(key1 = cocktailId) {
        viewModel.loadCocktailById(cocktailId)
    }
    BackHandler(onBack = pressOnBack)
    RotationEffect(isLoading, currentRotation, rotation)
    StatusBarColorChangeEffect(parsedColor)

    if (drink == null) {
        LoadingBox(rotation)
    } else {
        isLoading = false
        CocktailDetailsScreen(viewModel, drink, parsedColor, pressOnBack)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailDetailsScreen(
    viewModel: DetailViewModel,
    drink: Drink,
    parsedColor: ParsedColor,
    pressOnBack: () -> Unit,
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = drink.strDrink ?: stringResource(R.string.default_cocktail)) },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = PaletteGenerator.fromHex(parsedColor.vibrantSwatch)),
            navigationIcon = {
                IconButton(onClick = pressOnBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            }
        )},
    ) {
        Box(modifier = Modifier.padding(it)) {
            CocktailDetailsBody(drink, parsedColor)
        }
    }
}

@Composable
private fun CocktailDetailsBody(
    drink: Drink,
    parsedColor: ParsedColor,
) {

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(PaletteGenerator.fromHex(parsedColor.vibrantSwatch))
            .fillMaxHeight()
    ) {

        Card(shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)) {
            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = drink.strDrinkThumb,
                contentDescription = drink.strDrink,
                contentScale = ContentScale.FillWidth,

            )
        }

        Tags(drink = drink, parsedColor = parsedColor)

        Text(
            text = drink.strDrink ?: stringResource(id = R.string.default_cocktail),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(20.dp),
            color = PaletteGenerator.fromHex(parsedColor.vibrantSwatchBody)
        )

        Text(
            text = drink.strInstructions ?: stringResource(id = R.string.default_description),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(20.dp),
            color = PaletteGenerator.fromHex(parsedColor.mutedSwatchBody)
        )

        Text(
            text = stringResource(id = R.string.default_ingredients),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(20.dp),
            color = PaletteGenerator.fromHex(parsedColor.lightMutedSwatchBody)
        )

        val ingredients = ingredients(drink)
        val measures = measures(drink)

        for (i in ingredients.indices) {
            Row(modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxWidth()) {
                val ingredient = ingredients.getOrNull(i)
                val measure = measures.getOrNull(i)
                Text(text = ingredient ?: stringResource(id = R.string.default_ingredient),
                    style = MaterialTheme.typography.titleMedium,
                    color = PaletteGenerator.fromHex(parsedColor.lightVibrantSwatchBody),
                    modifier = Modifier.padding(end = 10.dp))
                Text(text = measure ?: stringResource(id = R.string.default_measures),
                    style = MaterialTheme.typography.titleMedium,
                    color = PaletteGenerator.fromHex(parsedColor.mutedSwatchBody))

            }
        }

        Text(
            text = stringResource(id = R.string.default_glass),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(20.dp),
            color = (PaletteGenerator.fromHex(parsedColor.lightMutedSwatchBody))
        )

        Row(modifier = Modifier
            .padding(start = 20.dp)
            .fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.serve_on),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = drink.strGlass ?: stringResource(id = R.string.default_glass),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 10.dp),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LoadingBox(rotation: Animatable<Float, AnimationVector1D>) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Center)) {
            Image(
                painter = rememberAsyncImagePainter(model = R.drawable.app_logo2),
                contentDescription = "logo",
                modifier = Modifier
                    .rotate(rotation.value)
                    .size(100.dp)
            )
            Text(
                text = stringResource(id = R.string.loading),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.align(CenterHorizontally)
            )
        }
    }
}

@Composable
fun RotationEffect(isLoading: Boolean, currentRotation: Float, rotation: Animatable<Float, AnimationVector1D>) {
    LaunchedEffect(key1 = isLoading) {
        rotation.animateTo(
            targetValue = currentRotation + 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ))
    }
}

@Composable
fun StatusBarColorChangeEffect(parsedColor: ParsedColor) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    val defaultSystemBarColor = Color(colorScheme.primary.toArgb())

    DisposableEffect(parsedColor, useDarkIcons) {
        systemUiController.setSystemBarsColor(
            color = PaletteGenerator.fromHex(parsedColor.vibrantSwatch),
            darkIcons = useDarkIcons
        )
        onDispose {
            systemUiController.setSystemBarsColor(
                color = defaultSystemBarColor,
                darkIcons = useDarkIcons
            )
        }
    }
}

@Composable
fun Tags(drink: Drink?, parsedColor: ParsedColor) {
    if (drink?.strTags != null) {
        val tags = drink.strTags.split(",")

        LazyRow(modifier = Modifier.padding(12.dp)) {
            items(tags) {
                Chip(
                    name = it,
                    isSelected = false,
                    parsedColor = parsedColor
                )
            }
        }
    }
}

fun ingredients(drink: Drink): List<String?> {
    with(drink) {
        return listOfNotNull(
            strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
            strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
            strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15,
        )
    }
}

fun measures(drink: Drink): List<String?> {
    with(drink) {
        return listOfNotNull(
            strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
            strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
            strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15,
        )
    }
}