package com.example.recipeFinder.server

import com.example.recipeFinder.logic.*

import io.ktor.http.ContentType
import io.ktor.serialization.JsonConvertException
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receiveText
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.*

// TODO:( THIS MODULE IS REDUNDANT AND ADDS UNNECESSARY COMPLEXITY,
//      IT CAN, HOWEVER, BE TURNED INTO A SERVER FOR SAVED DATA IF A DB IS INTRODUCED)
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
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    val client = createClient()!!

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
                val response: List<ApiResponseItem> = getSourceResponse(client, ingList)
                log.info("SERVER - RESPONSE :: SERVER RECEIVED:\n${response}")
                val jsonArray: String = Json.encodeToString(response)
                log.info("SERVER - STAGED :: DATA PREPPED FOR CLIENT:\n${jsonArray}")
                call.respondText(jsonArray, ContentType.Application.Json)

            } catch (err: InvalidBodyException) {
                call.respondText("ERROR COULD NOT GET POST BODY -- ERROR::MESSAGE:  ${err.message}")
            } catch (err: BadRequestException) {
                call.respondText("ERROR COULD NOT MAKE GET REQUEST TO DATA SOURCE -- ERROR::MESSAGE:  ${err.message}")
            } catch (err: JsonConvertException) {
                call.respondText("ERROR COULD PROCESS RESPONSE DATA INTO JSON -- ERROR::MESSAGE:  ${err.message}")
            } catch (err: Exception) {
                call.respondText("ERROR :: UNKNOWN ERROR -- ERROR::MESSAGE:  ${err.message}")
            }
        }
    }

}
