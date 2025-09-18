package com.example

import io.ktor.client.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceOrFileSource
import io.ktor.serialization.jackson.*
import com.fasterxml.jackson.databind.*
import java.util.Scanner


data class Config(val apiKey: Key)

data class Key(val key: String)

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
)

data class Ingredient(
    val aisle: String,
    val amount: String,
    val id: String,
    val image: ContentType.Image,
    val meta: List<String>,
    val name: String,
    val original: String,
    val originalName: String,
    val unit: String,
    val unitLong: String,
    val unitShort: String
)


private fun getAPIKey(): String {
    try {
        val config =  ConfigLoaderBuilder.default()
            .addResourceOrFileSource("api-key.conf", optional = false, allowEmpty = false)
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
                jackson()
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
// TODO:(CHECK IF WORKING)
suspend fun getResponse(client: HttpClient, ingredientsList: List<String>): HttpResponse {
    val baseUrl = "https://api.spoonacular.com/recipes/findByIngredients?"
    val key: String = getAPIKey()
    val ingredients: String = buildString {
        append("ingredients=")
        for (item in ingredientsList) {
            append("+")
            append(item)
        }
    }
    // MAX NUMBER OF RECIPES TO SHOW  --  DEFAULT: 10
    val number: Int = 10
    // (1) Maximize Used Ingredients || (2) Minimize Missing Ingredients
    val ranking: Int = 2
    // Ignore Pantry Staples (Flour, Water, Salt, etc.)
    val ignorePantry: Boolean = true

    val url: String = buildString {
        append(baseUrl)
        append("&${key}")
        append("&${ingredients}")
        append("&${number}")
        append("&${ranking}")
        append("%${ignorePantry}")
    }

    return client.get(url)

}
// TODO:("PROCESS RESPONSE DATA INTO USABLE FORMAT (ApiResponse)")
private fun processResponse(response: HttpResponse): ApiResponseItem {
    TODO("PROCESS RESPONSE DATA INTO USABLE FORMAT (ApiResponse)")
}

private fun initialize(client: HttpClient?, key: String) {
    val scanner = Scanner(System.`in`)
    var ch: Char? = null

    while (ch != 'Q') {
        println("\n\n\n\n\nWelcome To Recipe Finder!")
        println("Please Enter A Valid Input -->\t\tKEY : $key")
        println("\nt - Launch Text Interface")
        println("g - Launch Graphical Interface")
        println("\nq - Quit\n")

        ch = scanner.next()[0].uppercaseChar()

        when (ch) {
            'Q' -> {
                break
            }
            'T' -> TUI(client)
            'G' -> {
                println("\nERROR :: NOT IMPLEMENTED YET\t:P\n")
                continue
            }
            else -> {
                println("\nUNKNOWN KEY ENTERED '${ch}' ---  PLEASE ENTER A VALID KEY.\n")
                continue
            }

        }
    }
}

suspend fun main() {
    val client: HttpClient? = createClient()
    val key: String = getAPIKey()

    initialize(client, key)

    println("\n\nThank You For Using Recipe Finder!!")

    client?.close()

}
