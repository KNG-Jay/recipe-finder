package com.example.recipeFinder.logic

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform