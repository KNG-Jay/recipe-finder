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
            api(libs.ktor.client.cio)
            api(libs.ktor.client.contentNegotiation)
            api(libs.ktor.client.logging)
            api(libs.ktor.serialization.jackson)
            implementation(libs.ktor.kotlinx.json)
            // Misc
            implementation(libs.kotlinx.serialization.json)
            api(libs.kotlinx.coroutinesCore)
            api(libs.kotlinx.coroutinesSwing)
            api(libs.hoplite.core)
            api(libs.hoplite.hocon)
            api(libs.logback.classic)

        }
        commonTest.dependencies {
            implementation(libs.bundles.common.test)
        }
        jvmMain.dependencies {

        }
        androidMain.dependencies {

        }
    }

}
