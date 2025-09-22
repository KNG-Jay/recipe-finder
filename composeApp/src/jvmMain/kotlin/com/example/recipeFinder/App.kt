package com.example.recipeFinder

import com.example.recipeFinder.logic.*
import com.example.recipeFinder.server.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.request.ImageRequest
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.time.LocalDate


@Composable
fun HomeScreen(navController: NavController) {
    var userInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Header()
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
                        navController.navigate("confirm/${userInput.trim()}")
                        true
                    } else {
                        false
                    }
                },
        )
        Button(onClick = { navController.navigate("confirm/${userInput.trim()}") }) {
            Text("Submit")
        }
        Footer()

    }
}

@Composable
fun DetailScreen(navController: NavController, data: String?) {
    var recipesList by remember { mutableStateOf<List<ApiResponseItem>?>(null) }

    Row {
        Button(onClick = { navController.popBackStack() }){
            Text("Back")
        }
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primaryContainer)
        .safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,) {
        Text("Here Are Some Suggestions From The Ingredients Listed")
        if (!data.isNullOrEmpty()) {
            displayRecipes(data)
        } else {
            Text("Error: No Ingredients Were Found In List...")
        }
    }

}

@Composable
fun CheckerScreen(navController: NavController, inputData: String) {
    if (inputData.matches(Regex(".*[0-9!@#$%^&*()_+=-].*"))) {
        return Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Text("PLEASE ONLY USE LETTERS IN YOUR QUERY...")
            Button(onClick = { navController.popBackStack() }) {
                Text("Back")
            }
        }
    } else {
        val inputList: List<String> = inputData.split(" ")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Ingredients Listed:")
            CheckerCard(inputList)
            Row {
                Button(onClick = { navController.popBackStack() }) {
                    Text("Back")
                }
                Button(onClick = { navController.navigate("detail/${inputData}") }) {
                    Text("Get")
                }
            }
        }
    }
}

@Composable
fun CheckerCard(inputList: List<String>?) {
    if (inputList != null) {
        for (item in inputList) {
            Text("-> $item")
        }
    } else Text("FAILED TO GET INPUT LIST...")
}

@Composable
fun Header() {
    Row {
        Text(APP_NAME)
        Text(CheckCon(), modifier = Modifier
            .wrapContentWidth(Alignment.End))
    }
}

@Composable
fun Footer() {
    Row {
        Text("©${LocalDate.now().year}${OWNER}")
    }
}

@Composable
fun CheckCon(): String {
    val result = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    coroutineScope.launch {
        while (true) {
            result.value = desktopCheckActive()
            delay(5000)
        }
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
            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
                ) {
                Text(text = "ID: ${recipe.id}")
                ImageDisplay(recipe.image)
                Text(text = "Missed Ingredients [${recipe.missedIngredientCount}]: " +
                        recipe.missedIngredients.joinToString(", ") { it.name })
                Text(text = "Unused Ingredients [${recipe.usedIngredients.size}]: " +
                        recipe.usedIngredients.joinToString(", ") { it.name })
                Text(text = "Used Ingredients [${recipe.usedIngredientCount}]: " +
                        recipe.usedIngredients.joinToString(", ") { it.name })
                Spacer(Modifier
                    .height(5.dp))
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

    return AsyncImage(
        model = ImageRequest.Builder(context)
            .data(url)
            .build(),
        contentDescription = "Image of Ingredient",
        modifier = Modifier.size(128.dp),
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

            composable("confirm/{data}") { backStackEntry: NavBackStackEntry ->
                val savedStateHandle = backStackEntry.savedStateHandle
                val data = savedStateHandle.get<String>("data") ?: "error"
                CheckerScreen(navController, data)
            }
            composable("detail/{data}") { backStackEntry: NavBackStackEntry ->
                val savedStateHandle = backStackEntry.savedStateHandle
                val data = savedStateHandle.get<String>("data") ?: "error"
                DetailScreen(navController, data)
            }
        }
    }

}
