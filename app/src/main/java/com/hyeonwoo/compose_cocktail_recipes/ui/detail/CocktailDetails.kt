package com.hyeonwoo.compose_cocktail_recipes.ui.detail

import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ErrorResult
import coil.request.SuccessResult
import com.hyeonwoo.compose_cocktail_recipes.R
import com.hyeonwoo.compose_cocktail_recipes.extensions.paletteColorList
import com.hyeonwoo.compose_cocktail_recipes.model.Cocktail
import com.hyeonwoo.compose_cocktail_recipes.model.Drink
import com.hyeonwoo.compose_cocktail_recipes.ui.Chip
import com.hyeonwoo.compose_cocktail_recipes.util.PaletteGenerator
import com.hyeonwoo.compose_cocktail_recipes.util.ParsedColor
import com.hyeonwoo.compose_cocktail_recipes.util.RequestStatus
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailDetails(
    cocktailId: String,
    viewModel: DetailViewModel,
    pressOnBack: () -> Unit
) {
    val context = LocalContext.current

    var parsedColor by remember {
        mutableStateOf<ParsedColor>(ParsedColor())
    }
    val drink: Drink? = viewModel.cocktailDetailsFlow.collectAsState(initial = null).value?.drinks?.get(0)

    val painter = rememberImagePainter(data = drink?.strDrinkThumb)
    var imageResult by remember {
        mutableStateOf<RequestStatus>(RequestStatus.Loading)
    }

    LaunchedEffect(key1 = cocktailId) {
        Timber.d("LaunchedEffect called. key : cocktailId = $cocktailId")
        viewModel.loadCocktailById(cocktailId)
    }

    LaunchedEffect(key1 = drink?.strDrinkThumb) {
        Timber.d("LaunchedEffect called. key : drink?.strDrinkThumb = ${drink?.strDrinkThumb}")
        imageResult = RequestStatus.Loading
        val result = PaletteGenerator.convertImageUrlToBitmap(
            drink?.strDrinkThumb?: "",
            context
        )
        if (result is SuccessResult) {
            Timber.d("result is success")
            val bitmap = (result.drawable as BitmapDrawable).bitmap
            if (bitmap != null) {
                parsedColor = PaletteGenerator.extractColorsFromBitmapToParsedColors(
                    bitmap = bitmap
                )
            }
            imageResult = RequestStatus.Success
        }

        if (result is ErrorResult) {
            Timber.d("result is Error")
            imageResult = RequestStatus.Error
        }

        Timber.d("parsed Colors : ${parsedColor.toString()}")
    }
    BackHandler(onBack = pressOnBack)

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = drink?.strDrink ?: stringResource(R.string.default_cocktail)) },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = PaletteGenerator.fromHex(parsedColor.darkVibrant)),
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
            drink?.let {
                PosterDetailsBody(viewModel, it, parsedColor, pressOnBack)
            }
        }
    }

}

@Composable
private fun PosterDetailsBody(
    viewModel: DetailViewModel,
    drink: Drink,
    parsedColor: ParsedColor,
    pressOnBack: () -> Unit,
) {

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(PaletteGenerator.fromHex(parsedColor.darkVibrant))
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

        if (drink.strTags != null) {
            val tags = drink.strTags.split(",")
            Timber.d("$tags")
            LazyRow(modifier = Modifier.padding(12.dp)) {
                items(tags) {
                    Chip(
                        name = it,
                        isSelected = false
                    )
                }
            }
        }

        Text(
            text = drink.strDrink ?: stringResource(id = R.string.default_cocktail),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(20.dp),
            color = PaletteGenerator.fromHex(parsedColor.lightVibrant)
        )

        Text(
            text = drink.strInstructions ?: stringResource(id = R.string.default_description),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(20.dp),
            color = PaletteGenerator.fromHex(parsedColor.lightMuted)
        )
        Text(
            text = stringResource(id = R.string.default_ingredients),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 20.dp)
                .padding(20.dp),
            color = PaletteGenerator.fromHex(parsedColor.darkMuted)
        )
        val ingredients: List<String?> = with(drink) {
            listOf(strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
                strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
                strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15,
            )
        }.filterNotNull()
        val measures: List<String?> = with(drink) {
            listOf(strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
                strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
                strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15,
            )
        }.filterNotNull()
        for (i in ingredients.indices) {
            Row(modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxWidth()) {
                val ingredient = ingredients.getOrNull(i)
                val measure = measures.getOrNull(i)
                Text(text = ingredient ?: stringResource(id = R.string.default_ingredient),
                    color = PaletteGenerator.fromHex(parsedColor.muted))
                Text(text = measure ?: stringResource(id = R.string.default_measures),
                    color = PaletteGenerator.fromHex(parsedColor.darkMuted))

            }
        }

        Text(
            text = stringResource(id = R.string.default_glass),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 20.dp)
                .padding(20.dp),
            color = (PaletteGenerator.fromHex(parsedColor.vibrant))
        )

        Row(modifier = Modifier
            .padding(start = 20.dp)
            .fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.serve_on)
            )
            Text(
                text = drink.strGlass ?: stringResource(id = R.string.default_glass),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}

@Composable
private fun ColorPalettes(
    palette: Palette?,
    modifier: Modifier
) {

    val colorList: List<Int> = palette.paletteColorList()
    LazyRow(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        items(colorList) { color ->
            Crossfade(
                targetState = color,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(45.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(color = Color(it))
                        .fillMaxSize()
                )
            }
        }
    }
}