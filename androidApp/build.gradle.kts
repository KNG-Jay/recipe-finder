plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.androidLint)
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
        androidMain.dependencies {
            implementation(project(":shared"))
            implementation(project(":server"))

        }
    }
}
