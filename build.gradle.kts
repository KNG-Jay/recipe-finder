plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
}

group = "com.example"
version = "0.0.1"
val ktorVersion: String by project
val logbackVersion: String by project

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

dependencies {
    implementation(platform("io.ktor:ktor-bom:${ktorVersion}"))
    implementation("io.ktor:ktor-client-core:${ktorVersion}")
    implementation("io.ktor:ktor-client-cio:${ktorVersion}")
    implementation("io.ktor:ktor-client-content-negotiation:${ktorVersion}")
    implementation("io.ktor:ktor-client-logging:${ktorVersion}")
    implementation("ch.qos.logback:logback-classic:${logbackVersion}")
    implementation("com.sksamuel.hoplite:hoplite-core:2.7.5")
    implementation(libs.ktor.serialization.jackson)
    /*
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    testImplementation(libs.ktor.server.test.host)
    */
    testImplementation(libs.kotlin.test.junit)
}
