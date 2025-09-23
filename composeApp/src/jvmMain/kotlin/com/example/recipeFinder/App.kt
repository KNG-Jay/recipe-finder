package com.example.recipeFinder

import com.example.recipeFinder.logic.*
import com.example.recipeFinder.server.*

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
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


@Composable
fun HomeScreen(navController: NavController, closeApp: () -> Unit) {
    var userInput by remember { mutableStateOf("") }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Header(closeApp)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 16.dp)
                    .safeContentPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Enter Your Ingredients:  Separated By Spaces")
                Spacer(Modifier.padding(20.dp))
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

            }

            Footer()
        }
    }
}

@Composable
fun DetailScreen(navController: NavController, data: String?) {
    var recipeList: List<ApiResponseItem> by remember { mutableStateOf(emptyList()) }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(horizontalArrangement = Arrangement.Start) {
                Button(onClick = { navController.navigate("home") }) {
                    Text("Back")
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .safeContentPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Here Are Some Suggestions From The Ingredients Listed")
                if (!data.isNullOrEmpty()) {
                    val (list, display) = displayRecipes(data)
                    recipeList = list
                    return display
                } else {
                    Text("Error: No Ingredients Were Found In List...")
                }
            }
            Footer()
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
            Spacer(Modifier.padding(10.dp))
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
            verticalArrangement = Arrangement.Center
        ) {
            Text("Ingredients Listed:")
            Spacer(Modifier.padding(10.dp))
            CheckerCard(inputList)
            Column(
                modifier = Modifier
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("Is This Correct?", Modifier.padding(bottom = 2.dp))
                Row {
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Back")
                    }
                    Spacer(Modifier.padding(horizontal = 5.dp))
                    Button(onClick = { navController.navigate("detail/${inputData}") }) {
                        Text("Go")
                    }
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
fun Header(closeApp: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { closeApp() }
        ) { Text("Exit") }
        Spacer(Modifier.weight(1f))
        Text(text = APP_NAME, style = MaterialTheme.typography.titleMedium)
        Text(text = CheckCon(), Modifier.weight(1f).wrapContentWidth(Alignment.End))
    }
}

@Composable
fun Footer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = COPYRIGHT, style = MaterialTheme.typography.bodyMedium)
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
fun displayRecipes(ingList: String): Pair<List<ApiResponseItem>, Unit> {
    val lazyListState = rememberLazyListState()
    val result = remember { mutableStateOf<List<ApiResponseItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        result.value = desktopGetResponse(ingList.trim())
    }
    return Pair(result.value,
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
                items(result.value) { recipe: ApiResponseItem ->
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                    ) {
                        //Text(text = "ID: ${recipe.id}")
                        ImageDisplay(recipe.image)
                        Text(text = "Name: ${recipe.title}", Modifier.padding(2.dp, 7.dp))
                        Text(
                            text = "Missed Ingredients [${recipe.missedIngredientCount}]: " +
                                    recipe.missedIngredients.joinToString(", ") { it.name })
                        Text(
                            text = "Unused Ingredients [${recipe.usedIngredients.size}]: " +
                                    recipe.usedIngredients.joinToString(", ") { it.name })
                        Text(
                            text = "Used Ingredients [${recipe.usedIngredientCount}]: " +
                                    recipe.usedIngredients.joinToString(", ") { it.name })
                        Spacer(
                            Modifier
                                .height(8.dp)
                        )
                    }
                }
            }
            VerticalScrollbar(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState = lazyListState)
            )
        }

    )
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
fun App(closeApp: () -> Unit) {
    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }
    var showContent by remember { mutableStateOf(false) }
    val navController = rememberNavController()

    MaterialTheme {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") { HomeScreen(navController, closeApp) }

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
