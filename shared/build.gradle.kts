plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.ktor)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm()
    androidLibrary {
        namespace = "com.example.recipeFinder.android"
        compileSdk = 36
        minSdk = 24
    }

    sourceSets {
        commonMain.dependencies {
            // Multiplatform Dependencies
            // Ktor
            api(project.dependencies.platform(libs.ktor.bom))
            api(libs.ktor.serialization.jackson)
            api(libs.ktor.client.core)
            api(libs.ktor.client.cio)
            api(libs.ktor.client.contentNegotiation)
            api(libs.ktor.client.logging)
            // Misc
            api(libs.hoplite.core)
            api(libs.hoplite.hocon)
            api(libs.coil.core)
            api(libs.coil.compose)
            api(libs.coil.network)
            api(libs.logback.classic)
            api(libs.kotlin.test)
            api(libs.kotlin.testJunit)
            // Compose
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.ui)
            api(compose.components.resources)
            api(compose.components.uiToolingPreview)
            // Androidx
            api(libs.androidx.navigation)
            api(libs.androidx.lifecycle.viewmodelCompose)
            api(libs.androidx.lifecycle.runtimeCompose)

        }
        jvmMain.dependencies {
            api(compose.desktop.currentOs)
            api(libs.kotlinx.coroutinesSwing)
            api(libs.ktor.client.java)

        }
        androidMain.dependencies {
            /*
            ANDROID ONLY TOOLING
            implementation("androidx.compose.ui:ui:${androidxVersion}")
            implementation("androidx.compose.material:material:${androidxVersion}")
            implementation("androidx.compose.ui:ui-tooling-preview:${androidxVersion}")
            */
            api(libs.androidx.kts.core)
            api(libs.androidx.navigation)
            api(libs.androidx.lifecycle.viewmodelCompose)
            api(libs.androidx.lifecycle.runtimeCompose)
            api(libs.kotlinx.coroutinesCore)
            api(libs.ktor.client.android)

            // Testing Tools
            api(libs.kotlin.test)
            api(libs.kotlin.testJunit)
            api(libs.androidx.test.core)
            api(libs.androidx.test.runner)
            api(libs.androidx.test.junit)

        }
    }

}
