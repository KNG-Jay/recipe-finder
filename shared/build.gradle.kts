plugins {
    alias(libs.plugins.kotlinMultiplatform)

}

kotlin {
    jvm()
    //androidTarget()

    sourceSets {
        commonMain.dependencies {
                // Multiplatform Dependencies
                /*api(project.dependencies.platform(
                    "io.ktor:ktor-bom:${libs.versions.ktor.get()}"))
                */
                api(libs.ktor.client.core)
                api(libs.ktor.client.cio)
                api(libs.ktor.client.contentNegotiation)
                api(libs.ktor.client.logging)
                api(libs.hoplite.core)
                api(libs.hoplite.hocon)
                api(libs.coil.core)
                api(libs.coil.compose)
                api(libs.coil.network)
                api(libs.ktor.serialization.jackson)
                api(libs.logback.classic)
                api(libs.kotlin.test)
                api(libs.kotlin.testJunit)

        }
    }

}
