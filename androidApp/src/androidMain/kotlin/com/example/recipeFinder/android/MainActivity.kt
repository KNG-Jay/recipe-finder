package com.example.recipeFinder.android

import com.example.recipeFinder.app.*

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(closeApp = {
                finish()
                }
            )
        }
    }
}

