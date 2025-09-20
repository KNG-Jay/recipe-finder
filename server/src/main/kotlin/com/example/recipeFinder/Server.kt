package com.example.recipeFinder

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receiveText
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.module() {
    val client = createClient()
    val mapper = jacksonObjectMapper()

    routing {
        get("/api/con") {
            call.respondText("STATUS: API_ACTIVE")

        }

        post("/api/recipe") {
            try {
                val ingredients: String = call.receiveText()
                val response: List<ApiResponseItem> = getResponse(client, ingredients.split(" "))
                val jsonArray: String = mapper.writeValueAsString(response)
                call.respondText(jsonArray)

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

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}
