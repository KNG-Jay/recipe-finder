plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "com.example.recipeFinder"
version = "1.0.0"

application {
    mainClass = "io.ktor.server.netty.EngineMain"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(variantOf(libs.netty.epoll) { classifier("linux-x86_64") })

    testImplementation(libs.ktor.serverTestHost)
}