import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val ktorVersion = libs.versions.ktor.get()
val androidxVersion = libs.versions.androidx.asProvider().get()

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            //implementation("androidx.compose.ui:ui:${androidxVersion}")
            //implementation("androidx.compose.material:material:${androidxVersion}")
            //implementation("androidx.compose.ui:ui-tooling-preview:${androidxVersion}")
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.0")
            implementation(project(":shared"))
            implementation(project(":server"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation("io.ktor:ktor-client-java:${ktorVersion}")
        }
    }
}


compose.desktop {
    application {
        mainClass = "com.example.recipeFinder.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.example.recipeFinder"
            packageVersion = "1.0.0"
        }
    }
}
