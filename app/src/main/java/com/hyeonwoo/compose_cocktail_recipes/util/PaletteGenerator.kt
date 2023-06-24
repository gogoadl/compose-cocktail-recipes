package com.hyeonwoo.compose_cocktail_recipes.util

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.ImageResult

object PaletteGenerator {

    suspend fun convertImageUrlToBitmap(imageUrl: String, context: Context): ImageResult {

        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .build()

        return loader.execute(request)
    }


    fun extractColorsFromBitmapToParsedColors(bitmap: Bitmap): ParsedColor {
        return ParsedColor(
            vibrantSwatch = parseColorSwatch(color = Palette.from(bitmap).generate().vibrantSwatch),
            lightVibrantSwatch = parseColorSwatch(
                color = Palette.from(bitmap)
                    .generate().lightVibrantSwatch
            ),
            lightMutedSwatch = parseColorSwatch(
                color = Palette.from(bitmap)
                    .generate().lightMutedSwatch
            ),
            mutedSwatch = parseColorSwatch(
                color = Palette.from(bitmap)
                    .generate().mutedSwatch
            ),
            vibrantSwatchBody = parseBodyColor(
                color = Palette.from(bitmap)
                    .generate().vibrantSwatch?.bodyTextColor
            ),
            lightVibrantSwatchBody = parseBodyColor(
                color = Palette.from(bitmap)
                    .generate().lightVibrantSwatch?.bodyTextColor
            ),
            lightMutedSwatchBody = parseBodyColor(
                color = Palette.from(bitmap)
                    .generate().lightMutedSwatch?.bodyTextColor
            ),
            mutedSwatchBody = parseBodyColor(
                color = Palette.from(bitmap)
                    .generate().mutedSwatch?.bodyTextColor
            )
        )

    }

    private fun parseColorSwatch(color: Palette.Swatch?): String {
        return if (color != null) {
            val parsedColor = Integer.toHexString(color.rgb)
            return "#$parsedColor"
        } else {
            "#000000"
        }
    }

    private fun parseBodyColor(color: Int?): String {
        return if (color != null) {
            val parsedColor = Integer.toHexString(color)
            return "#$parsedColor"
        } else {
            "#FFFFFF"
        }
    }

    fun fromHex(color: String) = Color(android.graphics.Color.parseColor(color))
}
