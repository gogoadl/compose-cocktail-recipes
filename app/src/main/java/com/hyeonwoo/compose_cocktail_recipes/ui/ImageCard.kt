package com.hyeonwoo.compose_cocktail_recipes.ui

import LoadingAnimation
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest


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
                    .alpha(StrictMath.min(1f, transition / .2f))
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
                Text(title, style = MaterialTheme.typography.titleSmall, color = Color.White)
            }

        }
    }
}

@Composable
fun SingleImageCard(
    imageUrl: String,
    contentDescription: String,
    title: String
) {
    Card(shape = RoundedCornerShape(15.dp), modifier = Modifier.fillMaxSize(0.8f)) {
        Box(contentAlignment = Alignment.Center) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build())
            val state = painter.state

            if (state is AsyncImagePainter.State.Loading) {
                LoadingAnimation()
            }
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
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
                Text(title, style = MaterialTheme.typography.titleSmall, color = Color.White)
            }
        }
    }
}