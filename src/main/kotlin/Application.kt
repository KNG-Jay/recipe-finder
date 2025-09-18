package com.example

import io.ktor.client.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import com.fasterxml.jackson.databind.*
import io.ktor.serialization.jackson.*


data class APIKey(val key: String)


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
        println("Client Failed To Initialize -- ERROR::MESSAGE: ${err.message}")
        return null
    } catch (err: Exception) {
        println("CLIENT FAILED TO START -- UNKNOWN_ERROR: ${err.message}")
        return null
    }

    return client
}

suspend fun getRecipe(client: HttpClient, request: List<String>): HttpResponse {

}

suspend fun main() {
    val client = createClient()

    val result: HttpResponse? = client?.get("https://ktor.io/")
    println("${result?.status}")

    TUI()

    client?.close()
}
