plugins {
    alias(libs.plugins.stocksviewercmp.kmp.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.feature.favorites.api"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:navigation"))
            implementation(libs.kotlinx.serialization.core)
        }
    }
}
