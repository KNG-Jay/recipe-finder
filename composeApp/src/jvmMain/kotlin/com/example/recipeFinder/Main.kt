package com.example.recipeFinder

import com.example.recipeFinder.server.startServer

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main() = application {

    GlobalScope.launch {
        startServer()

    }

    Window(
        onCloseRequest = { exitApplication() },
        title = "Recipe Finder",
    ) {
        App()
    }

}
