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
import kotlin.system.exitProcess
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
    var client: HttpClient? = null
    try {
        client = HttpClient(CIO) {
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

    return client
}
// TODO("GET RECIPES: FILTERED BY INGREDIENTS LISTED")
suspend fun getRecipe(client: HttpClient, request: List<String>): HttpResponse {
    val key: String = getAPIKey()
    return TODO("Provide the return value")
}

private fun processResponse(response: HttpResponse): ApiResponseItem {
    TODO("PROCESS RESPONSE DATA INTO USABLE FORMAT (ApiResponse)")
}

suspend fun main() {
    val client: HttpClient? = createClient()
    val key: String = getAPIKey()
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
                //println("\n\nThank You For Using Recipe Finder!!\n\n")
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

    println("\n\nThank You For Using Recipe Finder!!")

    client?.close()

}
