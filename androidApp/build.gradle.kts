
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvmToolchain(21)

    androidTarget {
        android {
            namespace = "com.example.recipeFinder.android"
            compileSdk = 36
            defaultConfig {
                minSdk = 26
            }
            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1,INDEX.LIST}"
                }
            }
        }
    }
}

dependencies {
    implementation(project(":composeApp"))
    implementation(libs.ktor.client.android)
    implementation(libs.androidx.kts.core)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.runner)
    testImplementation(libs.androidx.test.junit)
}
