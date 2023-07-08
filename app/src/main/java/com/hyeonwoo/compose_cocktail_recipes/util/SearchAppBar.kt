package com.hyeonwoo.compose_cocktail_recipes.util

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    modifier: Modifier = Modifier,
    title: String,
    query: String,
    placeholder: String,
    targetState: Boolean,
    onSearch: () -> Unit,
    onQueryChange: (String) -> Unit,
    onQueryDone: (KeyboardActionScope.() -> Unit)?,
    onClose: () -> Unit,
    backgroundColor: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors(),
    textFieldColor: Color = textFieldBackgroundColor2
) {
    val focusManager = LocalFocusManager.current
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    TopAppBar(
        modifier = modifier.addFocusCleaner(focusManager = focusManager),
        colors = backgroundColor,
        actions = {
            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = searchIconContentDescription,
                )
            }
        },
        title = {
            SearchAppBarTitle(
                modifier = modifier,
                targetState = targetState,
                title = title,
                placeholder = placeholder,
                query = query,
                focusRequester = focusRequester,
                onQueryChange = onQueryChange,
                onQueryDone = onQueryDone,
                onClose = onClose,
                textFieldColor = textFieldColor
            )
        })

}

const val textFieldWidthWeight = 0.7f
const val textFieldHeightWeight = 0.05f

const val searchIconContentDescription = "Search Icon"
const val closeIconContentDescription = "Close Icon"

val textFieldCornerShape = 24.dp
val textFieldBackgroundColor = Color.LightGray
val textFieldBackgroundColor2 = Color(0xFFD5D5D5)
val textFieldPaddingStart = 15.dp

@Composable
private fun SearchTextField(
    modifier: Modifier = Modifier,
    placeholder: String,
    focusRequester: FocusRequester,
    onQueryChange: (String) -> Unit,
    onQueryDone: (KeyboardActionScope.() -> Unit)?,
    query: String
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .padding(start = textFieldPaddingStart)
            .focusRequester(focusRequester = focusRequester),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = onQueryDone),
        decorationBox = { innerTextField ->
            if (query.isEmpty()) {
                Text(
                    text = placeholder,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            }
            innerTextField()
        }
    )
}
@Composable
private fun SearchTextFieldWrapper(
    modifier: Modifier,
    title: String,
    placeholder: String,
    isSearch: Boolean,
    query: String,
    focusRequester: FocusRequester,
    onQueryChange: (String) -> Unit,
    onQueryDone: (KeyboardActionScope.() -> Unit)?,
    onClose: () -> Unit,
    textFieldColor: Color
) {
    if (isSearch) {
        Row(
            modifier = modifier
                .fillMaxWidth(textFieldWidthWeight)
                .fillMaxHeight(textFieldHeightWeight)
                .border(
                    border = BorderStroke(1.dp, textFieldColor),
                    shape = RoundedCornerShape(textFieldCornerShape)
                )
                .clip(shape = RoundedCornerShape(textFieldCornerShape))
                .background(textFieldColor)
        ) {
            SearchTextField(
                modifier = modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                query = query,
                placeholder = placeholder,
                focusRequester = focusRequester,
                onQueryChange = onQueryChange,
                onQueryDone = onQueryDone
            )
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = closeIconContentDescription,
                )
            }
        }
    } else {
        Text(text = title)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SearchAppBarTitle(
    modifier: Modifier,
    targetState: Boolean,
    title: String,
    placeholder: String,
    query: String,
    focusRequester: FocusRequester,
    onQueryChange: (String) -> Unit,
    onQueryDone: (KeyboardActionScope.() -> Unit)?,
    onClose: () -> Unit,
    textFieldColor: Color
) {
    Row() {
        AnimatedContent(
            targetState = targetState,
            transitionSpec = {
                if ( targetState > initialState ){
                    ContentTransform(
                        targetContentEnter = slideInHorizontally { height -> height } + fadeIn(),
                        initialContentExit = slideOutHorizontally { height -> -height } + fadeOut()
                    )
                } else {
                    ContentTransform(
                        targetContentEnter = slideInHorizontally { height -> -height } + fadeIn(),
                        initialContentExit = slideOutHorizontally { height -> height } + fadeOut()
                    )
                }
            }
        ) {isSearch ->
            SearchTextFieldWrapper(
                modifier = modifier,
                title = title,
                placeholder = placeholder,
                query = query,
                isSearch = isSearch,
                focusRequester = focusRequester,
                onQueryChange = onQueryChange,
                onQueryDone = onQueryDone,
                onClose = onClose,
                textFieldColor = textFieldColor
            )
        }
    }
}

// Reference : https://blog.yena.io/studynote/2022/04/30/Jetpack-Compose-TextField-Focus.html
fun Modifier.addFocusCleaner(focusManager: FocusManager, doOnClear: () -> Unit = {}): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            doOnClear()
            focusManager.clearFocus()
        })
    }
}