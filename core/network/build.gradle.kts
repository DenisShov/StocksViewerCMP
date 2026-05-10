plugins {
    alias(libs.plugins.stocksviewercmp.kmp.library)
    alias(libs.plugins.stocksviewercmp.secrets.buildconfig)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.core.network"
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":core:common"))

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.koin.core)
            api(libs.arrow.core)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.timber)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}
