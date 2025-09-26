
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvmToolchain(21)

    jvm()
    androidLibrary {
        namespace = "com.example.recipeFinder.composeApp"
        compileSdk = 36
        minSdk = 26
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":shared"))
            // Compose
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.ui)
            api(compose.components.resources)
            api(compose.components.uiToolingPreview)
            // Coil
            api(libs.bundles.coil)
            // Androidx - common utils
            api(libs.bundles.androidx.common)

        }
        commonTest.dependencies {
            implementation(libs.bundles.common.test)
        }
        jvmMain.dependencies {
            api(compose.desktop.currentOs)
            api(libs.ktor.client.cio)
        }
        androidMain.dependencies {
            api(libs.bundles.androidx.test)
            api(libs.ktor.client.okhttp)
            api(libs.androidx.kts.core)
        }
    }
}
