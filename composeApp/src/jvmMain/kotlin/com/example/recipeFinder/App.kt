package com.example.recipeFinder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.request.ImageRequest
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.compose.rememberAsyncImagePainter
import coil3.request.crossfade
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import recipe_finder.composeapp.generated.resources.Res
import recipe_finder.composeapp.generated.resources.compose_multiplatform


@Composable
fun CheckConnection() {
    val result = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        result.value = desktopCheckActive()
    }

    return Text(text = result.value)
}

@Composable
fun GetRecipes(ingList: String): List<ApiResponseItem> {
    val result = remember { mutableStateOf<List<ApiResponseItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        result.value = desktopGetResponse(ingList.trim())
    }
    return result.value

}

@Composable
fun displayRecipes(ingList: String) {
    val result = remember { mutableStateOf<List<ApiResponseItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        result.value = desktopGetResponse(ingList.trim())
    }
    LazyColumn {
        items(result.value) { recipe: ApiResponseItem ->
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "ID: ${recipe.id}")
                ImageDisplay(recipe.image)
                Text(text = "Missing Ingredients Count: ${recipe.missedIngredientCount}")
                Text(text = "Missed Ingredients: ${recipe.missedIngredients.joinToString(", ") { it.name }}")
                Text(text = "Unused Ingredients: ${recipe.usedIngredients.joinToString(", ") { it.name }}")
                Text(text = "Used Ingredients Count: ${recipe.usedIngredientCount}")
                Text(text = "Used Ingredients: ${recipe.usedIngredients.joinToString(", ") { it.name }}")
            }
        }
    }
}

fun getAsyncImageLoader(context: PlatformContext): ImageLoader {
    return ImageLoader.Builder(context)
        .crossfade(true)
        .build()
}

@Composable
fun ImageDisplay(url: String) {
    val context: PlatformContext = LocalPlatformContext.current
    val imageLoader = remember { ImageLoader(context) }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(url)
            .build(),
        imageLoader = imageLoader,
    )

    return AsyncImage(
        model = painter,
        contentDescription = "Image of Ingredient",
        modifier = Modifier.size(128.dp)
    )

}

@Composable
@Preview
fun App() {
    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }
    var showContent by remember { mutableStateOf(false) }
    var recipeData by remember { mutableStateOf<List<ApiResponseItem>?>(null)}

    MaterialTheme {

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
        }

    }

}