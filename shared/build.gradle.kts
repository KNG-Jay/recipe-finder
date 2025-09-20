plugins {
    alias(libs.plugins.kotlinMultiplatform)

}

group = "com.example"
version = "0.0.1"
val ktorVersion: String by project

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            implementation(project.dependencies.platform("io.ktor:ktor-bom:${ktorVersion}"))
            implementation("io.ktor:ktor-client-core:${ktorVersion}")
            implementation("io.ktor:ktor-client-cio:${ktorVersion}")
            implementation("io.ktor:ktor-client-content-negotiation:${ktorVersion}")
            implementation("io.ktor:ktor-client-logging:${ktorVersion}")
            implementation("com.sksamuel.hoplite:hoplite-core:2.7.5")
            implementation("com.sksamuel.hoplite:hoplite-hocon:2.7.5")
            implementation(libs.ktor.serialization.jackson)
            //implementation(projects.shared)
            implementation(libs.logback.classic)
            implementation(libs.ktor.serverCore)
            implementation(libs.ktor.serverNetty)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.testJunit)
        }
    }
}

