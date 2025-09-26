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
        jvmMain.dependencies {
            implementation(project(":composeApp"))

        }
        jvmTest.dependencies {
            implementation(libs.bundles.common.test)
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
