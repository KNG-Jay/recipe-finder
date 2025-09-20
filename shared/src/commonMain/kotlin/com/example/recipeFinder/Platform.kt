package com.example.recipeFinder

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform