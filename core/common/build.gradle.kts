plugins {
    alias(libs.plugins.stocksviewercmp.kmp.library.compose)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.core.common"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(compose.runtime)
            implementation(compose.components.resources)
            implementation(project(":core:commonresources"))
        }
    }
}
