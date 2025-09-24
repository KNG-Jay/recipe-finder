package com.example.recipeFinder.android

import androidx.compose.runtime.Composable
import com.example.recipeFinder.server.startServer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//TODO:(IMPLEMENT ANDROID LAUNCHER)
/*
fun main() = application {

    GlobalScope.launch {
        startServer()
    }
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

*/