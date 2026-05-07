plugins {
    alias(libs.plugins.stocksviewercmp.android.library)
    alias(libs.plugins.stocksviewercmp.android.koin)
    alias(libs.plugins.stocksviewercmp.android.library.jacoco)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.core.common"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)
}