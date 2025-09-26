package com.example.recipeFinder.logic

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

actual fun createPlatformHttpClientEngine(): HttpClient {
    return HttpClient(CIO) {
        engine {
            maxConnectionsCount = 1000
            /*
            endpoint {
                maxConnectionsPerRoute = 100
                pipelineMaxSize = 20
                keepAliveTime = 5000
                connectTimeout = 5000
                connectAttempts = 5
            }
            */
        }
    }
}
