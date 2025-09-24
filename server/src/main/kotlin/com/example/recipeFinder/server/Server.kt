package com.example.recipeFinder.server

import com.example.recipeFinder.logic.*

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receiveText
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun startServer() {
        embeddedServer(
            Netty,
            port = SERVER_PORT,
            host = SERVER_HOST,
            module = Application::module
            ).start(wait = true)

}

fun Application.module() {

    install(ContentNegotiation) {
        jackson()
    }

    val client = createClient()!!
    val mapper = jacksonObjectMapper()

    routing {
        get(API_SERVER_CON) {
            call.respondText("API_ACTIVE")
            log.info("SERVER - RESPONSE :: ACTIVE")

        }

        post(API_SERVER_POST) {
            try {
                val ingredients: String = call.receiveText()
                val ingList: List<String> = ingredients
                    .split(",", ", ", " ")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() && it != ","}
                val response: List<ApiResponseItem> = getResponse(client, ingList)
                log.info("SERVER - RESPONSE :: SERVER RECEIVED:\n${response}")
                val jsonArray: String = mapper.writeValueAsString(response)
                log.info("SERVER - STAGED :: DATA PREPPED FOR CLIENT:\n${jsonArray}")
                call.respondText(jsonArray, ContentType.Application.Json)

            } catch (err: InvalidBodyException) {
                call.respondText("ERROR COULD NOT GET POST BODY -- ERROR::MESSAGE:  ${err.message}")
            } catch (err: BadRequestException) {
                call.respondText("ERROR COULD NOT MAKE GET REQUEST TO DATA SOURCE -- ERROR::MESSAGE:  ${err.message}")
            } catch (err: JsonProcessingException) {
                call.respondText("ERROR COULD PROCESS RESPONSE DATA INTO JSON -- ERROR::MESSAGE:  ${err.message}")
            } catch (err: Exception) {
                call.respondText("ERROR :: UNKNOWN ERROR -- ERROR::MESSAGE:  ${err.message}")
            }
        }
    }

}

suspend fun desktopCheckActive(): String {
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

suspend fun desktopGetResponse(ingList: String): List<ApiResponseItem> {
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
