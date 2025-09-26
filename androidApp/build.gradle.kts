
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
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
    testImplementation(libs.bundles.common.test)
    testImplementation(libs.bundles.androidx.test)
}
