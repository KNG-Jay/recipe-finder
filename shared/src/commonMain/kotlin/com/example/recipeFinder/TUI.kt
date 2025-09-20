package com.example.recipeFinder

import io.ktor.client.HttpClient
import java.util.Scanner


suspend fun tui(client: HttpClient?) {
    suspend fun process(client: HttpClient?, list: List<String>) {
        val response: List<ApiResponseItem> = getResponse(client, list)
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
