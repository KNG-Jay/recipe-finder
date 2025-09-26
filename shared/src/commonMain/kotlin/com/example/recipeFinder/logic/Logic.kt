package com.example.recipeFinder.logic

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceOrFileSource
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*


data class Config(val apiKey: Key)

data class Key(val key: String)

@Serializable
data class ApiResponseItem(
    val id: String,
    val image: String,
    val imageType: String,
    val likes: String,
    val missedIngredientCount: String,
    val missedIngredients: List<Ingredient>,
    val title: String,
    val unusedIngredients: List<Ingredient>,
    val usedIngredientCount: String,
    val usedIngredients: List<Ingredient>
) : Iterable<String> {
    override fun iterator(): Iterator<String> {
        return object : Iterator<String> {
            var index = 0
            val props = listOf(
                id,
                image,
                imageType,
                likes,
                missedIngredientCount,
                missedIngredients.joinToString(" ") { it.component1() },
                title,
                unusedIngredients.joinToString(" ") { it.component1() },
                usedIngredientCount,
                usedIngredients.joinToString(" ") { it.component1() },
                )

            override fun hasNext(): Boolean {
                return index < props.size
            }

            override fun next(): String {
                if (!hasNext()) throw NoSuchElementException()
                return props[index++]
            }
        }
    }
}

@Serializable
data class Ingredient(
    val aisle: String,
    val amount: String,
    val id: String,
    val image: String,
    val meta: List<String>,
    val name: String,
    val original: String,
    val originalName: String,
    val extendedName: String? = null,
    val unit: String,
    val unitLong: String,
    val unitShort: String
)


internal fun getAPIKey(): String {
    try {
        val config =  ConfigLoaderBuilder.default()
            .addResourceOrFileSource(API_KEY_FILE, optional = false, allowEmpty = false)
            .build()
            .loadConfigOrThrow<Config>()
        val key: String = config.apiKey.key
        println("KEY RETRIEVED  ::  $key")
        return key
    } catch (err: Exception) {
        println("ERROR RETRIEVING API-KEY -- ERROR::MESSAGE:  ${err.message}")
        return "MISSING"
    }
}

fun createClient(): HttpClient? {
    try {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
            }
            expectSuccess = true
            engine {
                maxConnectionsCount = 1000
                endpoint {
                    maxConnectionsPerRoute = 100
                    pipelineMaxSize = 20
                    keepAliveTime = 5000
                    connectTimeout = 5000
                    connectAttempts = 5
                }
            }
        }
    } catch (err: ClientRequestException) {
        println("Client Failed To Initialize -- ERROR::MESSAGE:  ${err.message}")
        return null
    } catch (err: Exception) {
        println("CLIENT FAILED TO START -- UNKNOWN_ERROR:  ${err.message}")
        return null
    }
}

suspend fun getSourceResponse(client: HttpClient?, ingredientsList: List<String>): List<ApiResponseItem> {
    val baseUrl = API_SOURCE_SPOONACULAR
    val key: String = "apiKey=".plus(getAPIKey())
    val ingredients: String = buildString {
        append("ingredients=")
        for (item in ingredientsList) {
            append("+")
            append(item)
        }
    }
    // MAX NUMBER OF RECIPES TO SHOW  --  DEFAULT: 10
    val number = 25
    // (1) Maximize Used Ingredients || (2) Minimize Missing Ingredients
    val ranking = 2
    // Ignore Pantry Staples (Flour, Water, Salt, etc.)
    val ignorePantry = true

    val url: String = buildString {
        append(baseUrl)
        append("&${key}")
        append("&${ingredients}")
        append("&number=${number}")
        append("&ranking=${ranking}")
        append("&ignorePantry=${ignorePantry}")
    }

    try {
        val response: List<ApiResponseItem> = client!!.get(url).body()
        return response
    } catch (err: Exception) {
        println("ERROR GETTING RESPONSE DATA  --  ERROR::MESSAGE:  ${err.message}")
        return emptyList()
    }
}

suspend fun checkActive(): String {
    try {
        val client = createClient()!!
        val response: HttpResponse = client.get("${SERVER_ADDRESS}${SERVER_PORT}${API_SERVER_CON}")
        val check: String = response.body()
        client.close()
        return if (response.status.value in 200..299) "STATUS:  $check"
        else "STATUS:  API_OFFLINE"
    } catch (err: Exception) {
        println("FAILED TO CONNECT TO KTOR SERVER  --  ERROR::MESSAGE:  ${err.message}")
        return "STATUS:  API_OFFLINE"
    }
}

suspend fun getResponse(ingList: String): List<ApiResponseItem> {
    try {
        val client = createClient()!!
        val response: HttpResponse = client.post("${SERVER_ADDRESS}${SERVER_PORT}${API_SERVER_POST}") {
            contentType(ContentType.Application.Json)
            setBody(ingList)
        }
        client.close()
        println("RESPONSE :: APP RECEIVED:\n${response.body() as String}")
        return response.body()
    } catch (err: Exception) {
        println("FAILED TO CONNECT TO KTOR SERVER  --  ERROR::MESSAGE:  ${err.message}")
        return emptyList()
    }
}