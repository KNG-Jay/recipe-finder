package com.example.recipeFinder.logic

import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking
import java.util.Scanner


fun initialize(client: HttpClient?, key: String) {
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
            'T' -> {
                runBlocking { tui(client) }
                continue
            }
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

suspend fun tui(client: HttpClient?) {
    suspend fun process(client: HttpClient?, list: List<String>) {
        val response: List<ApiResponseItem> = getSourceResponse(client, list)
        val display = processResponse(response)
        return display
    }

    var ch: Char? = '@'
    val scanner = Scanner(System.`in`)

    while (ch != 'Q') {
        println("\nWelcome To The Text Interface!\n\n")
        println("Enter Your List Of Available Ingredients, Separated By Spaces:\nENTER 'q' TO QUIT!\n")
        val listStr: String = scanner.nextLine()

        if (listStr.length == 1 && listStr == "q") break
        val list: List<String> = listStr.split(" ").toMutableList().map { it.replaceFirstChar(Char::uppercaseChar) }

        println("\nItems Entered:")
        list.forEach { println("\t--> $it") }
        println("\nIs That Correct?\t'y' || 'n'")
        ch = scanner.next()[0].uppercaseChar()

        when (ch) {
            'Y' -> {
                process(client, list)
                break
            }
            else -> {
                println("\n\nRemoving List And Restarting...")
                break
            }
        }

    }

}

fun processResponse(response: List<ApiResponseItem>) {
    for (items in response) {
        println("\n${items.title} ------->" +
                "\n\tID: ${items.id}" +
                "\n\tImage: ${items.image}" +
                "\n\tMissed Ingredients Count: ${items.missedIngredientCount}" +
                "\n\tMissed Ingredients: ${items.missedIngredients.joinToString(", ") { it.name }}" +
                "\n\tUnused Ingredients: ${items.usedIngredients.joinToString(", ") { it.name }}" +
                "\n\tUsed Ingredients Count: ${items.usedIngredientCount}" +
                "\n\tUsed Ingredients: ${items.usedIngredients.joinToString(", ") { it.name }}"
        )

    }
    println("\n\nEND OF RESPONSE ------->  Press ENTER To Continue")
    readlnOrNull()
    println("\nGoing Home....")

}

fun main() {
    val client = createClient()!!
    val key: String = getAPIKey()

    initialize(client, key)

    println("\n\nThank You For Using Recipe Finder!!")

    client.close()

}
