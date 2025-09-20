package com.example.recipeFinder

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "demo",
    ) {
        _root_ide_package_.com.example.recipeFinder.App()
    }
}