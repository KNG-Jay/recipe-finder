import org.jetbrains.compose.desktop.application.dsl.TargetFormat

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
            implementation(project(":shared"))
            implementation(project(":server"))

        }
        commonTest.dependencies {

        }
        jvmMain.dependencies {

        }
    }
}


compose.desktop {
    application {
        mainClass = "com.example.recipeFinder.desktop.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.example.recipeFinder.desktop"
            packageVersion = "1.0.0"

        }
    }
}
