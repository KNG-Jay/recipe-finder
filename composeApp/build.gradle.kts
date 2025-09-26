
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
            api(libs.coil.core)
            api(libs.coil.compose)
            api(libs.coil.network)
            // Androidx
            api(libs.androidx.navigation)
            api(libs.androidx.lifecycle.viewmodelCompose)
            api(libs.androidx.lifecycle.runtimeCompose)

        }
        commonTest.dependencies {
            implementation(libs.bundles.common.test)
        }
        jvmMain.dependencies {
            api(compose.desktop.currentOs)
            api(libs.ktor.client.java)
        }
        androidMain.dependencies {
        }
    }
}

//tasks.named("jvmRun") { enabled = false }
tasks.named("jvmJar") { enabled = false }
tasks.named("jvmTest") { enabled = false }
