package com.example.recipeFinder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.request.ImageRequest
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.compose.rememberAsyncImagePainter
import coil3.request.crossfade
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun HomeScreen(navController: NavController) {
    var recipesList by remember { mutableStateOf<List<ApiResponseItem>?>(null) }
    var userInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CheckCon()
        Text("Enter Your Ingredients, Separated By Spaces")
        TextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("Ingredients: ") },
            modifier = Modifier
                .background(Color.Magenta)
                //.onClick()
                .onKeyEvent { event ->
                    if (event.key == Key.Enter) {
                        navController.navigate("detail/${userInput.trim()}")
                        true
                    } else {
                        false
                    }
                },
        )
        Button(onClick = { navController.navigate("detail/${userInput.trim()}") }) {
            Text("Submit")
        }

    }

}

@Composable
fun DetailScreen(data: String?) {
    Text("Here Are Some Suggestions From The Ingredients Listed")
    if (!data.isNullOrEmpty()) {
        displayRecipes(data)
    } else {
        Text("Error: No Ingredients Were Found In List...")
    }

}

@Composable
fun CheckCon() {
    val result = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    coroutineScope.launch {
        while (true) {
            result.value = desktopCheckActive()
            delay(3000)
        }
    }
    return Text(text = result.value)

}

@Composable
fun GetRecipesList(ingList: String): List<ApiResponseItem> {
    val result = remember { mutableStateOf<List<ApiResponseItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        result.value = desktopGetResponse(ingList.trim())
    }
    return result.value

}

@Composable
fun displayRecipes(ingList: String) {
    val result = remember { mutableStateOf<List<ApiResponseItem>>(emptyList()) }
    val recipesList = GetRecipesList(ingList)

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
    val navController = rememberNavController()

    MaterialTheme {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") { HomeScreen(navController) }
            composable("detail/{data}") { backStackEntry ->
                val data = backStackEntry.arguments?.toString() ?: "error"
                DetailScreen(data)
            }
        }

    }

}
