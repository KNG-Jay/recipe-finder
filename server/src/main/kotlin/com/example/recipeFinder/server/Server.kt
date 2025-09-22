package com.example.recipeFinder.server

import com.example.recipeFinder.logic.*

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.ContentType
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.BadRequestException
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
                val response: List<ApiResponseItem> = getResponse(client, ingredients.split(" "))
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
