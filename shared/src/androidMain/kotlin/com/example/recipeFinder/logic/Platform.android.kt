package com.example.recipeFinder.logic

class AndroidPlatform(): Platform {
    override val name: String = "Android"
}

actual fun getPlatform(): Platform = AndroidPlatform()