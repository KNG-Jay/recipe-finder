package com.example.recipeFinder.logic

class JVMPlatform : Platform {
    //override val name: String = "Java ${System.getProperty("java.version")}"
    override val name: String = "JVM"
}

actual fun getPlatform(): Platform = JVMPlatform()