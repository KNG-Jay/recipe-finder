package com.example.recipeFinder.logic

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

actual fun createPlatformHttpClientEngine(): HttpClient {
    return HttpClient(OkHttp)
}
