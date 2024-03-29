package com.hyeonwoo.compose_cocktail_recipes.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyeonwoo.compose_cocktail_recipes.util.PaletteGenerator
import com.hyeonwoo.compose_cocktail_recipes.util.ParsedColor

@Preview(showBackground = true)
@Composable
fun Chip(
    name: String = "Chip",
    isSelected: Boolean = false,
    onSelectionChanged: (String) -> Unit = {},
    parsedColor: ParsedColor = ParsedColor()
) {
    Surface(
        modifier = Modifier.padding(4.dp),
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) PaletteGenerator.fromHex(parsedColor.mutedSwatch) else PaletteGenerator.fromHex(parsedColor.lightMutedSwatch)
    ) {
        Row(modifier = Modifier
            .toggleable(
                value = isSelected,
                onValueChange = {
                    onSelectionChanged(name)
                }
            )
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                color = PaletteGenerator.fromHex(parsedColor.mutedSwatchBody),
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}