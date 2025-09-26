import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.implementation
import org.gradle.kotlin.dsl.project

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

tasks.withType<Tar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
tasks.withType<Zip> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

application {
    mainClass = "io.ktor.server.netty.EngineMain"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}


dependencies {
    implementation(project(":shared"))
    implementation(libs.bundles.ktor.server)
    implementation(libs.netty.epoll) {
        artifact {
            classifier = "linux-x86_64"
        }
    }

    testImplementation(libs.bundles.common.test)
    testImplementation(libs.ktor.serverTestHost)
}
