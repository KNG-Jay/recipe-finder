package com.example.recipeFinder.desktop

import com.example.recipeFinder.app.App

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application


fun main() = application {
    AppWindow()

}

@Composable
fun AppWindow() {
    var isOpen by remember { mutableStateOf(true) }

    if (isOpen) {
        Window(
            onCloseRequest = { isOpen = false },
            title = "Recipe Finder",
        ) {
            App(closeApp = { isOpen = false })
        }
    }
}
