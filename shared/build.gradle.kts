plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.serialization)
}

kotlin {
    jvmToolchain(21)

    jvm()
    androidLibrary {
        namespace = "com.example.recipeFinder.shared"
        compileSdk = 36
        minSdk = 26
    }

    sourceSets {
        commonMain.dependencies {
            // Multiplatform Dependencies
            // Ktor
            implementation(project.dependencies.platform(
                libs.ktor.bom))
            api(libs.ktor.client.core)
            api(libs.ktor.client.contentNegotiation)
            api(libs.ktor.client.logging)
            api(libs.ktor.kotlinx.json)
            // Kotlinx
            api(libs.kotlinx.serialization.json)
            api(libs.kotlinx.coroutinesCore)
            api(libs.kotlinx.coroutinesSwing)
            // Misc
            api(libs.hoplite.core)
            api(libs.hoplite.hocon)
            api(libs.logback.classic)

        }
        commonTest.dependencies {
            api(libs.bundles.common.test)
        }
        jvmMain.dependencies {
            api(libs.ktor.client.cio)
        }
        androidMain.dependencies {
            api(libs.ktor.client.okhttp)
        }
    }

}
